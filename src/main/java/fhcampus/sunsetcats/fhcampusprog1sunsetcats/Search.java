package fhcampus.sunsetcats.fhcampusprog1sunsetcats;


import java.util.ArrayList;
import java.util.HashMap;

public class Search
{
    //Map of ArrayLists | 1 List of Items per Category searched
    public String searchStartURL;
    private final ArrayList<String> searchFilters;
    private final boolean continueScrape;

    private final String searchTarget;
    private HashMap<String,HashMap<String,String>> rawSearchResults = new HashMap<>();

    public Integer duplicates = 0;

    // =============================== Konstruktoren ===============================

    public Search(String startURL, ArrayList<String> searchFilters, boolean continueScrape)
    {
        this.searchStartURL = startURL;
        this.searchFilters = searchFilters;
        this.continueScrape = continueScrape;

        if(searchStartURL.startsWith("https://www.willhaben.at"))
        {
            this.searchTarget = "willhaben";
        }
        else
        {
            this.searchTarget = "no target";
        }
    }

    public Search(String startURL, ArrayList<String> searchFilters)
    {
        this.searchStartURL = startURL;
        this.searchFilters = searchFilters;

        this.continueScrape = false;

        if(searchStartURL.startsWith("https://www.willhaben.at"))
        {
            this.searchTarget = "willhaben";
        }
        else
        {
            this.searchTarget = "no target";
        }
    }

    public Search(String startURL, boolean continueScrape)
    {
        this.searchStartURL = startURL;
        this.searchFilters = new ArrayList<>();

        this.continueScrape = continueScrape;

        if(searchStartURL.startsWith("https://www.willhaben.at"))
        {
            this.searchTarget = "willhaben";
        }
        else
        {
            this.searchTarget = "no target";
        }
    }


    public Search(String startURL)
    {
        this.searchStartURL = startURL;
        this.searchFilters = new ArrayList<>();

        this.continueScrape = false;

        if(searchStartURL.startsWith("https://www.willhaben.at"))
        {
            this.searchTarget = "willhaben";
        }
        else
        {
            this.searchTarget = "no target";
        }
    }


    // =============================== Methoden ===============================

    public void addResult(String id, HashMap<String,String> result)
    {
        rawSearchResults.put(id, result);
    }

    public boolean containsResult(String id)
    {
        if(rawSearchResults.containsKey(id))
        {
            return true;
        }
        return false;
    }

    public HashMap<String, HashMap<String,String>> getRawSearchResults()
    {
        return rawSearchResults;
    }


    public String getSearchStartURL()
    {
        return this.searchStartURL;
    }

    public boolean continueScrape()
    {
        return this.continueScrape;
    }
}


