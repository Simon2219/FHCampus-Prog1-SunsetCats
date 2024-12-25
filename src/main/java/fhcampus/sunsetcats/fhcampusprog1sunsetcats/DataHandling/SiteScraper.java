package fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Search;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public abstract class SiteScraper
{

    protected final DataConnector connector;
    protected final Search searchObject;

    public SiteScraper(DataConnector connector, Search searchObject)
    {
        this.connector = connector;
        this.searchObject = searchObject;
    }

    // Main scraping entry point
    public void start() throws IOException, InterruptedException
    {
        String startUrl = searchObject.getSearchStartURL();
        if (searchObject.continueScrape())
        {
            scrapeAllCategories(startUrl);
        } else
        {
            scrapeSearchPages(startUrl);
        }
    }

    // Scrape all categories
    private void scrapeAllCategories(String url) throws IOException, InterruptedException
    {
        HttpResponse<String> response = sendRequest(url);
        if (response == null) return;

        scrapeSearchPages(url);
        Document document = Jsoup.parse(response.body());
        for (Element link : document.select(getCssQueryTag()))
        {
            String categoryUrl = connector.getBaseURL() + link.attr("href");
            if (!categoryUrl.contains("javascript") && !categoryUrl.contains("sfId"))
            {
                scrapeSearchPages(categoryUrl);
            }
        }
        System.err.println("No new Categories found!");
    }

    // Scrape over all pages of a specific search
    private void scrapeSearchPages(String url) throws IOException, InterruptedException
    {
        System.err.println("Currently Scraping those Pages:     " + url);
        if (!url.startsWith(connector.getBaseURL() + getCategoryTag()))
        {
            System.err.println("Invalid category URL: " + url);
            return;
        }
        int count = 0;
        String currentUrl = url;
        while (currentUrl != null)
        {
            HttpResponse<String> response = sendRequest(currentUrl);
            if (response == null) break;

            Optional<JSONArray> validatedResponse = validateResponse(response);
            if (validatedResponse.isEmpty()) break;

            scrapeSearchResults(validatedResponse.get());
            currentUrl = getNextPageUrl(response.body());
            count++;
        }
        System.err.println("Page Count:   " + count);
        System.err.println("Item Total:  " + searchObject.rawSearchResults.size());
    }


    private void scrapeSearchResults(JSONArray searchResults)
    {
        for (int i = 0; i < searchResults.length(); i++)
        {
            JSONObject result = searchResults.getJSONObject(i);
            extractDataFromResult(result);
        }
    }



    // Send HTTP request
    private HttpResponse<String> sendRequest(String url) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder().uri(java.net.URI.create(url)).GET().build();
        HttpResponse<String> response = connector.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200)
        {
            System.err.println("Failed to fetch URL: " + url);
            return null;
        }
        return response;
    }

    protected String getNextPageUrl(String html)
    {
        try
        {
            // Parse the HTML using Jsoup
            Document document = Jsoup.parse(html);

            // Get the pagination button selector from the child class
            String paginationSelector = getPaginationButtonSelector();

            // Find the pagination button element
            Element paginationButton = document.selectFirst(paginationSelector);

            if (paginationButton != null)
            {
                // Extract the href attribute and build the next page URL
                String nextPagePath = paginationButton.attr("href");
                return connector.getBaseURL() + nextPagePath;
            }
        } catch (Exception e)
        {
            System.err.println("Error retrieving next page URL: " + e.getMessage());
        }

        return null; // No next page found
    }

    // Abstract methods for site-specific behavior

    protected abstract String getPaginationButtonSelector();

    protected abstract void extractDataFromResult(JSONObject searchResults);

    protected abstract String getCategoryTag();

    protected abstract String getCssQueryTag();

    protected abstract Optional<JSONArray> validateResponse(HttpResponse<String> response);

}
