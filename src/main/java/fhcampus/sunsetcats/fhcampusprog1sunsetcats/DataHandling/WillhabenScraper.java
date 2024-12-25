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


public final class WillhabenScraper extends SiteScraper
{

    private final String paginationButton = "a[data-testid='pagination-top-next-button']";


    // Constructor
    // Muss der connector & die durchzuführende Suche übergeben werden
    public WillhabenScraper(WillhabenConnector connector, Search currentSearch)
    {
        super(connector, currentSearch);
    }



    //===================================================================== || MAIN FUNCTIONS || =====================================================================



    @Override
    protected Optional<JSONArray> validateResponse(HttpResponse<String> response)
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

        return Optional.of(advertSummaryList.getJSONArray("advertSummary"));
    }



    @Override
    protected void extractDataFromResult(JSONObject currentItem)
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

                extractedResults.put("image" + i, image.optString("mainImageUrl", "Unknown"));
            }
        }

        String resultID = extractedResults.get("id");
        String resultDescription = extractedResults.get("description");

        if(searchObject.rawSearchResults.containsKey(resultID))
        {
            if(!searchObject.rawSearchResults.get(resultID).get("description").equals(resultDescription))
            {
                System.err.println("WillhabenScraper: Duplicate ID for different Immo Objects");
            }
            return;
        }

        searchObject.rawSearchResults.put(extractedResults.get("id"), extractedResults);
    }



    private String extractJsonFromHTML(String html)
    {
        String startTag = "<script id=\"__NEXT_DATA__\" type=\"application/json\">";
        String endTag = "</script>";

        int startIndex = html.indexOf(startTag) + startTag.length();
        int endIndex = html.indexOf(endTag, startIndex);

        if(startIndex > startTag.length() && endIndex > startIndex)
        {
            return html.substring(startIndex,endIndex);
        }

        return null;
    }



//===================================================================== || SIDE FUNCTIONS || =====================================================================



    @Override
    protected String getCategoryTag()
    {
        return ((WillhabenConnector) connector).getCategoryTag();
    }

    @Override
    protected String getCssQueryTag()
    {
        return ((WillhabenConnector) connector).getCssQueryTag();
    }

    @Override
    protected String getPaginationButtonSelector() {
        return paginationButton;
    }





}
