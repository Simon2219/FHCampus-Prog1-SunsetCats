package fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.logging.Logger;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Search;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public final class WillhabenScraper extends SiteScraper
{

    private static final Logger Debug = Logger.getLogger(WillhabenScraper.class.getName());


    // Constructor
    // Muss der connector & die durchzuführende Suche übergeben werden
    public WillhabenScraper(WillhabenConnector connector, Search currentSearch)
    {
        super(
                connector,
                currentSearch,
                "https://www.willhaben.at",
                "https://www.willhaben.at/iad/immobilien/",
                "/iad/immobilien",
                "a[href^='/iad/immobilien/']",
                "a[data-testid='pagination-top-next-button']",
                "<script id=\"__NEXT_DATA__\" type=\"application/json\">",
                "</script>"
                );
    }



    //===================================================================== || MAIN FUNCTIONS || =====================================================================


    // Main scraping entry point
    @Override
    public void start()
    {
        try
        {
            String startUrl = searchObject.getSearchStartURL();
            if(startUrl == null) startUrl = DEFAULT_START;

            ArrayList<JSONObject> searchResults = new ArrayList<>();

            if (searchObject.continueScrape())
            {
                searchResults = scrapeAllCategories(startUrl);
            }
            else
            {
                searchResults = scrapeSearchPages(startUrl);
            }

            fillSearchObject(searchResults);
        }
        catch(IllegalArgumentException e)
        {
            Debug.severe("No Response for URL " + e.getMessage());
        }
    }



    protected void fillSearchObject(ArrayList<JSONObject> resultArray)
    {
        for(JSONObject result : resultArray)
        {
            HashMap<String, String> extractedResult = extractDataFromResult(result);

            searchObject.addResult(extractedResult.get("id"), extractedResult);
        }
    }



    @Override
    protected Optional<JSONArray> validateResponse(HttpResponse<String> response)
    {
        String pageContent = response.body();

        String jsonResponse = extractJsonFromHTML(pageContent);
        if (jsonResponse == null || jsonResponse.isEmpty())
        {
            Debug.warning("JSON Response String empty!");
            return Optional.empty();
        }

        JSONObject jsonObject = new JSONObject(jsonResponse);
        if (!jsonObject.has("props") || !jsonObject.getJSONObject("props").has("pageProps") || !jsonObject.getJSONObject("props").getJSONObject("pageProps").has("searchResult"))
        {
            Debug.warning("Invalid JSON structure");
            return Optional.empty();
        }

        JSONObject categoryResults = jsonObject.getJSONObject("props").getJSONObject("pageProps").getJSONObject("searchResult");
        if (!categoryResults.has("advertSummaryList"))
        {
            Debug.severe("No 'advertSummaryList' found in 'searchResult'.");
            return Optional.empty();
        }

        JSONObject advertSummaryList = categoryResults.getJSONObject("advertSummaryList");
        if (!advertSummaryList.has("advertSummary"))
        {
            Debug.severe("No 'advertSummary' found in 'advertSummaryList'.");
            return Optional.empty();
        }

        return Optional.of(advertSummaryList.getJSONArray("advertSummary"));
    }


    // Returns all Data from a specific Listing
    @Override
    protected HashMap<String,String> extractDataFromResult(JSONObject currentItem) throws IllegalArgumentException
    {
        HashMap<String, String> extractedResults = new HashMap<>();

        // Extract high-level attributes
        extractedResults.put("id", currentItem.optString("id", "Unknown"));
        extractedResults.put("verticalId", currentItem.optString("verticalId", "Unknown"));
        extractedResults.put("adTypeId", currentItem.optString("adTypeId", "Unknown"));
        extractedResults.put("productId", currentItem.optString("productId", "Unknown"));
        extractedResults.put("description", currentItem.optString("description", "Unknown"));


        if (!currentItem.has("attributes") || !currentItem.getJSONObject("attributes").has("attribute"))
        {
            Debug.severe("No attribute Array returned!");
            throw new IllegalArgumentException();
        }

        JSONArray allAttributes = currentItem.getJSONObject("attributes").getJSONArray("attribute");

        for (int count = 0; count < allAttributes.length(); count++)
        {
            JSONObject currentAttribute = allAttributes.getJSONObject(count);
            String name = currentAttribute.optString("name", "Unknown");

            JSONArray valueArray = currentAttribute.optJSONArray("values"); //All Values of that certain attribute - mostly 1 but can be more
            if (valueArray == null || valueArray.isEmpty())
            {
                Debug.severe("The Value Array is Empty! ");
                throw new IllegalArgumentException();
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

                extractedResults.put("image" + i, image.optString("mainImageUrl", "Unknown"));
            }
        }

        return extractedResults;
    }




//===================================================================== || SIDE FUNCTIONS || =====================================================================











}
