package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import java.util.ArrayList;
import java.util.List;

public class ResultStore {
    private static ResultStore instance;
    private List<Immobilie> searchResults;

    private ResultStore() {
        searchResults = new ArrayList<>();
    }

    public static ResultStore getInstance() {
        if (instance == null) {
            instance = new ResultStore();
        }
        return instance;
    }

    public List<Immobilie> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<Immobilie> results) {
        this.searchResults = results;
    }
    }
