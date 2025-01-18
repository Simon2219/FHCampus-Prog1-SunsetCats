package fhcampus.sunsetcats.fhcampusprog1sunsetcats;


import java.util.ArrayList;
import java.util.HashMap;

public class Search
{
    //Map of ArrayLists | 1 List of Items per Category searched
    private final String searchStartURL;
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

    // Methoden zum Hinzufügen von Filtern zu der Liste der Suchfilter.
    public void addSearchFilter(String filter) {
        if (filter != null && !filter.isEmpty()) {
            searchFilters.add(filter);
        }
    }

    // Methode zum Löschen von Filtern aus der Liste der Suchfilter.
    public void removeSearchFilter(String filter) {
        searchFilters.remove(filter);
    }

    // GETTER-Methode für Suchfilter.
    public ArrayList<String> getSearchFilters() {
        return searchFilters;
    }


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


