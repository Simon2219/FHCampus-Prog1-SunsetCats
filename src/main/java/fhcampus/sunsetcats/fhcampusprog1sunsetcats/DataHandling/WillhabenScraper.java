package fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Search;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public final class WillhabenScraper
{

    private final String START_URL;
    private final String BASE_URL;
    private final String CATEGORY_TAG;
    private final String QUERY_TAG;

    private final HttpClient httpClient;
    private final Search searchObject;


    // Constructor
    // Muss der connector & die durchzuführende Suche übergeben werden
    public WillhabenScraper(WillhabenConnector connector, Search currentSearch)
    {
        this.BASE_URL = connector.getBaseURL();
        this.httpClient = connector.getHttpClient();

        this.searchObject = currentSearch;
        this.START_URL = searchObject.getSearchStartURL();

        this.CATEGORY_TAG = connector.getCategoryTag();
        this.QUERY_TAG = connector.getCssQueryTag();
    }


    //===================================================================== || MAIN FUNCTIONS || =====================================================================


    // Hauptfunktion um Suche zu starten
    public void start() throws IOException, InterruptedException
    {
        System.out.println("Scraping from - " + START_URL);

        if (searchObject.continueScrape()) // ? Soll vom Link ausgehend weitergesucht werden oder nur die Ergebnisse
        {
            scrapeAllCategories(START_URL); // Scrape alle Kategorien
        } else
        {
            scrapeCategory(START_URL); // Scrape nur eine
        }
    }


    // Alle Kategorien ausgehend vom Link scrapen
    private void scrapeAllCategories(String url) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString()); // Sende Request an Website

        if (response.statusCode() != 200) // HTTP Response hat Status Codes - 200 = erfolgreich
        {
            System.err.println("Failed to fetch categories from: " + url);
            return;
        }

        String pageContent = response.body();
        Document document = Jsoup.parse(pageContent);
        Elements categoryLinks = document.select(QUERY_TAG); // Get All listed Links starting with /iad/immobilien

        for (Element link : categoryLinks) // Für alle Links der Kategorie
        {
            String categoryUrl = BASE_URL + link.attr("href"); // Build Category URL -> BASE + Category Link

            if (!categoryUrl.contains("javascript")) // Only follow navigable links
            {
                System.out.println("Scraping category: " + categoryUrl);

                scrapeCategory(categoryUrl);
            }
        }
    }

    // Spezielle Kategorie wird gescraped -> Alle Seiten durchgehen
    private void scrapeCategory(String url) throws IOException, InterruptedException
    {
        String currentUrl = url; // Start with the initial URL

        // URL muss der Kategorie entsprechen
        if (!url.startsWith(BASE_URL + CATEGORY_TAG))
        {
            System.err.println("Provided URL is not a category-specific URL: " + url);
            return;
        }

        int siteNR = 0;
        while (currentUrl != null) // Loop -> bis es keine weiteren Seiten mehr gibt
        {

            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(currentUrl)).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString()); // Neue Request an Website

            if (response.statusCode() != 200) // Nicht erfolgreich
            {
                System.err.println("Failed to fetch category page: " + currentUrl);
                return;
            }

            // Überprüfe ob die Request die erwartete JSON Struktur hat -> ansonsten Abbruch
            Optional<JSONObject> validatedResponse = validateResponse(response);
            if (validatedResponse.isEmpty())
            {
                System.err.println("WILLHABEN SCRAPER: Response invalid");
                return;
            }

            // Extract Data der Seite
            scrapeSearchResults(validatedResponse.get());


            // Nächste Seite ->
            // Parse the response to find the next page URL
            String nextURL = getNextPageUrl(response.body());

            if (nextURL != null && !nextURL.startsWith(BASE_URL + CATEGORY_TAG)) //Muss Seite selber Kategorie sein
            {
                System.err.println("Next page URL is not a valid category-specific URL: " + nextURL);
                return;
            }

            currentUrl = nextURL; //current URL zu neuer setzen & Loop wiederholen

            siteNR++; //Debug
            System.err.println(siteNR); //Debug
        }
    }


    private void scrapeSearchResults(JSONObject searchResults)
    {
        JSONArray resultsArray = searchResults.getJSONArray("advertSummary");

        for (int i = 0; i < resultsArray.length(); i++)
        {
            JSONObject result = resultsArray.getJSONObject(i);
            extractDataFromResult(result);
        }
    }




    private void extractDataFromResult(JSONObject currentItem)
    {
        HashMap<String, String> extractedResults = new HashMap<String, String>();

        // Extract high-level attributes
        extractedResults.put("id", currentItem.optString("id", "Unknown"));
        extractedResults.put("verticalId", currentItem.optString("verticalId", "Unknown"));
        extractedResults.put("adTypeId", currentItem.optString("adTypeId", "Unknown"));
        extractedResults.put("productId", currentItem.optString("productId", "Unknown"));
        extractedResults.put("description", currentItem.optString("description", "Unknown"));


        if (!currentItem.has("attributes") || !currentItem.getJSONObject("attributes").has("attribute"))
        {
            System.err.println("No attribute Array returned!");
            return;
        }
        JSONArray allAttributes = currentItem.getJSONObject("attributes").getJSONArray("attribute");


        for (int count = 0; count < allAttributes.length(); count++)
        {
            JSONObject currentAttribute = allAttributes.getJSONObject(count);
            String name = currentAttribute.optString("name", "Unknown");

            JSONArray valueArray = currentAttribute.optJSONArray("values"); //All Values of that certain attribute - mostly 1 but can be more
            if (valueArray == null || valueArray.isEmpty())
            {
                System.err.println("The Value Array is Empty! ");
                return;
            }

            String value = valueArray.getString(0); //TODO | Only Gets 1 Value right now, ignores others - TODO EXTRACT ALL
            extractedResults.put(name, value);
        }



        //Extract Floor Plan
        if (currentItem.has("floorPlans"))
        {
            JSONArray floorPlanArray = currentItem.getJSONObject("floorPlans").optJSONArray("values");

            extractedResults.put("floorPlan", floorPlanArray.getString(0));
        }

        // Extract poster info
        if (currentItem.has("advertiserInfo"))
        {
            JSONObject advertiserInfo = currentItem.getJSONObject("advertiserInfo");

            extractedResults.put("advertiserLabel", advertiserInfo.optString("label", "Unknown"));
        }

        // Extract image URLs
        if (currentItem.has("advertImageList") && currentItem.getJSONObject("advertImageList").has("advertImage"))
        {
            JSONArray advertImages = currentItem.getJSONObject("advertImageList").getJSONArray("advertImage");

            for (int i = 0; i < advertImages.length(); i++)
            {
                JSONObject image = advertImages.getJSONObject(i);

                extractedResults.put("image"+i,image.optString("mainImageUrl", "Unknown"));
            }
        }


        searchObject.rawSearchResults.put(extractedResults.get("id"), extractedResults);
    }


    private Optional<JSONObject> validateResponse(HttpResponse<String> response)
    {
        String pageContent = response.body();

        String jsonResponse = extractJsonFromHTML(pageContent);
        if (jsonResponse == null || jsonResponse.isEmpty())
        {
            System.err.println("JSON Response String empty!");
            return Optional.empty();
        }

        JSONObject jsonObject = new JSONObject(jsonResponse);
        if (!jsonObject.has("props") || !jsonObject.getJSONObject("props").has("pageProps") || !jsonObject.getJSONObject("props").getJSONObject("pageProps").has("searchResult"))
        {
            System.err.println("Invalid JSON structure");
            return Optional.empty();
        }

        JSONObject categoryResults = jsonObject.getJSONObject("props").getJSONObject("pageProps").getJSONObject("searchResult");
        if (!categoryResults.has("advertSummaryList"))
        {
            System.err.println("No 'advertSummaryList' found in 'searchResult'.");
            return Optional.empty();
        }

        JSONObject advertSummaryList = categoryResults.getJSONObject("advertSummaryList");
        if (!advertSummaryList.has("advertSummary"))
        {
            System.err.println("No 'advertSummary' found in 'advertSummaryList'.");
            return Optional.empty();
        }

        return Optional.of(advertSummaryList);
    }



    private String getNextPageUrl(String html)
    {
        try
        {
            // Parse the HTML using JSoup
            Document document = Jsoup.parse(html);

            // Find the pagination button with the specified data-testid
            // TODO Adjust to other websites
            Element paginationButton = document.selectFirst("a[data-testid='pagination-top-next-button']");

            if (paginationButton != null)
            {
                // Extract the href attribute and build the next page URL
                String nextPagePath = paginationButton.attr("href");
                return BASE_URL + nextPagePath;
            }
        }
        catch (Exception e)
        {
            System.err.println("Error retrieving next page URL: " + e.getMessage());
        }

        return null; // No next page found
    }



    private String extractJsonFromHTML(String html)
    {
        String startTag = "<script id=\"__NEXT_DATA__\" type=\"application/json\">";
        String endTag = "</script>";

        int startIndex = html.indexOf(startTag) + startTag.length();
        int endIndex = html.indexOf(endTag, startIndex);

        if (startIndex > startTag.length() && endIndex > startIndex)
        {
            return html.substring(startIndex, endIndex);
        }
        return null;
    }



}
