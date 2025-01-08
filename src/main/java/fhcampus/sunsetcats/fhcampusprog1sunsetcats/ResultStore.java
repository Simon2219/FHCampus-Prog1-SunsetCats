package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import java.io.*;
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
        serializeResults(results, "previous-results.ser");
    }

    public void getPreviousResults() {
        this.searchResults = deserializeResults("previous-results.ser");
        UIController.getInstance().loadContent("result-view.fxml");
    }

    private static void serializeResults(List<Immobilie> results, String filename) {
        try (FileOutputStream fos = new FileOutputStream (filename);
             ObjectOutputStream oos = new ObjectOutputStream (fos)) {
            oos.writeObject (results);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Immobilie> deserializeResults(String filename) {
        try (FileInputStream fis = new FileInputStream (filename);
             ObjectInputStream ois = new ObjectInputStream (fis)) {
            return (List<Immobilie>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
