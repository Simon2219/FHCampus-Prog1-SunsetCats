package fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Search;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



public abstract class SiteScraper {

    protected static final Logger Debug = Logger.getLogger(SiteScraper.class.getName());

    protected final WillhabenConnector connector;
    protected final Search searchObject;

    protected final String BASE_URL;  //Die Grund URL der Website z.B https://willhaben.at/
    protected final String DEFAULT_START;

    protected final String categoryTag;  //Tag of current category to search
    protected final String cssQueryTag;
    protected final String paginationButtonMarker;


    protected final String JsonStartTag;
    protected final String JsonEndTag;


    public SiteScraper (WillhabenConnector connector, Search searchObject, String BASE_URL, String DEFAULT_START,String categoryTag, String cssQueryTag, String paginationButtonMarker, String JsonStartTag,
                        String JsonEndTag)
    {
        this.connector = connector;
        this.searchObject = searchObject;

        this.BASE_URL = BASE_URL;
        this.DEFAULT_START = DEFAULT_START;

        this.categoryTag = categoryTag;
        this.cssQueryTag = cssQueryTag;
        this.paginationButtonMarker = paginationButtonMarker;

        this.JsonStartTag = JsonStartTag;
        this.JsonEndTag = JsonEndTag;
    }



    //===================================================================== || ABSTRACT FUNCTIONS || =====================================================================



    // Abstract methods to be implemented by subclasses
    public abstract void start();

    protected abstract Optional<JSONArray> validateResponse(HttpResponse<String> response);

    protected abstract HashMap<String,String> extractDataFromResult(org.json.JSONObject currentItem);

    protected abstract void fillSearchObject(ArrayList<JSONObject> resultArray);


    //===================================================================== || BASE FUNCTIONS || =====================================================================



    // Scrape all categories
    protected ArrayList<JSONObject> scrapeAllCategories(String url) throws IllegalArgumentException
    {
        ArrayList<JSONObject> searchResults = new ArrayList<>();

        Optional<HttpResponse<String>> response = sendRequest(url);
        if (response.isEmpty()) throw new IllegalArgumentException();

        searchResults.addAll(scrapeSearchPages(url));

        Document document = Jsoup.parse(response.get().body());
        for (Element link : document.select(getCssQueryTag()))
        {
            String categoryUrl = getBaseURL() + link.attr("href");
            if (!categoryUrl.contains("javascript") && !categoryUrl.contains("sfId"))
            {
                searchResults.addAll(scrapeSearchPages(categoryUrl));
            }
        }

        Debug.severe("No new Categories found!");

        return searchResults;
    }



    // Scrape over all pages of a specific search & return Array of the Results
    protected ArrayList<JSONObject> scrapeSearchPages(String url) throws IllegalArgumentException
    {
        Debug.info("Currently Scraping those Pages:     " + url);

        if (!url.startsWith(getBaseURL() + getCategoryTag()))
        {
            Debug.info("Invalid category URL: " + url);
            throw new IllegalArgumentException();
        }

        ArrayList<JSONObject> searchResults = new ArrayList<>();
        int count = 0;
        int itemTotal = 0;

        String currentUrl = url;
        while (currentUrl != null)
        {
            Optional<HttpResponse<String>> response = sendRequest(currentUrl);
            if (response.isEmpty()) break;

            Optional<JSONArray> validatedResponse = validateResponse(response.get());
            if (validatedResponse.isEmpty()) break;

            JSONArray pageResults = validatedResponse.get();
            for (int i = 0; i < pageResults.length(); i++)
            {
                searchResults.add(pageResults.getJSONObject(i));
            }

            currentUrl = getNextPageUrl(response.get().body());
            count++;
            itemTotal += pageResults.length();
        }

        Debug.info(String.format("Page Count: %s  Item Total: %s ", count, itemTotal));

        return searchResults;
    }




    // Send HTTP request
    protected Optional<HttpResponse<String>> sendRequest(String url)
    {
        try
        {
            HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create(url)).GET().build();
            HttpResponse<String> response = connector.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            // Debugging der HTTP-Antwort
            Debug.info("HTTP-Anfrage an URL: " + url);
            Debug.info("HTTP-Statuscode: " + response.statusCode());
            Debug.info("HTTP-Header: " + response.headers());
            Debug.info("HTTP-Response-Inhalt (gekürzt): " + response.body().substring(0, Math.min(500, response.body().length())) + "...");

            if (response.statusCode() != 200)
            {
                Debug.warning("Failed to fetch URL: " + url);
                return Optional.empty();
            }

            return Optional.of(response);
        }
        catch (IOException e)
        {
            Debug.severe("IO Exception while sending Request " + e.getMessage());
            return Optional.empty();
        }
        catch (InterruptedException e)
        {
            Debug.severe("Interruption while sending Request " + e.getMessage());
            return Optional.empty();
        }

    }


    // Get URL of the next Result Page
    protected String getNextPageUrl(String html)
    {
        Document document = Jsoup.parse(html);
        Element paginationButton = document.selectFirst(getPaginationButtonSelector());

        if (paginationButton == null)
        {
            Debug.warning("No next Page Found " + html);
            return null;
        }

        // Extract the href attribute and build the next page URL
        String nextPagePath = paginationButton.attr("href");
        return getBaseURL() + nextPagePath;
    }



    protected String extractJsonFromHTML(String html)
    {
        String startTag = getJsonStartTag();
        String endTag = getJsonEndTag();

        int startIndex = html.indexOf(startTag) + startTag.length();
        int endIndex = html.indexOf(endTag, startIndex);

        if(startIndex > startTag.length() && endIndex > startIndex)
        {
            return html.substring(startIndex,endIndex);
        }

        return null;
    }



    protected String getBaseURL()
    {
        return this.BASE_URL;
    }

    protected String getCategoryTag()
    {
        return categoryTag;
    }

    protected String getCssQueryTag()
    {
        return cssQueryTag;
    }

    protected String getJsonStartTag()
    {
        return JsonStartTag;
    }

    protected String getJsonEndTag()
    {
        return JsonEndTag;
    }

    protected String getPaginationButtonSelector()
    {
        return paginationButtonMarker;
    }



}