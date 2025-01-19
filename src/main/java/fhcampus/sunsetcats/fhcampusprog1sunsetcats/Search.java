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

    private Integer minRooms;
    private Integer maxRooms;



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

    public void setSearchStartURL(String searchStartURL) {
        this.searchStartURL = searchStartURL;
    }

    public boolean continueScrape()
    {
        return this.continueScrape;
    }



    // GETTER und SETTER für Räume
    public Integer getMinRooms() {
        return this.minRooms; // minRooms ist ein Attribut in der Klasse
    }

    public void setMinRooms(Integer minRooms) {
        this.minRooms = minRooms;
    }

    public Integer getMaxRooms() {
        return this.maxRooms; // maxRooms ist ein Attribut in der Klasse
    }

    public void setMaxRooms(Integer maxRooms) {
        this.maxRooms = maxRooms;
    }


}


