package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling.WillhabenConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class AppMain extends Application
{

    private static final WillhabenConnector willhabenConnector = new WillhabenConnector();


    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("JavaFX Demo - Sunset Cats Immobilien");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        //launch();

        try
        {
            Search searchImmo = new Search("https://www.willhaben.at/iad/immobilien/haus-kaufen/haus-angebote?sfId=4d397651-95f3-402c-8fd4-8eb4c15d49a8&isNavigation=true&rows=30&areaId=900&NO_OF_ROOMS_BUCKET=5X5&page" +
             "=1", false );

            //Search searchImmo = new Search("https://www.willhaben.at/iad/immobilien/");

            Optional<ArrayList<Immobilie>> results = willhabenConnector.startSearch(searchImmo);

            if (results.isEmpty())
            {
                System.err.println("APPMAIN: Search Results are empty");
                return;
            }

            ArrayList<Immobilie>  searchResults = results.get();

            printResults(searchResults);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void printResults(ArrayList<Immobilie> searchResults)
    {
        for(Immobilie currentImmobilie : searchResults)
        {
            System.out.printf("ID: %s  -  %s %s", currentImmobilie.getAttribute(Immobilie.AttributeKey.ID), currentImmobilie.getAttribute(Immobilie.AttributeKey.DESCRIPTION), System.lineSeparator());
        }
    }
}
