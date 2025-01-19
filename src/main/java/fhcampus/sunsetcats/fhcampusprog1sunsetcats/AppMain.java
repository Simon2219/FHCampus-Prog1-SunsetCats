package fhcampus.sunsetcats.fhcampusprog1sunsetcats;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling.WillhabenConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.*;

public class AppMain extends Application
{
    static final WillhabenConnector willhabenConnector = new WillhabenConnector();
    private static final Logger Debug = Logger.getLogger(AppMain.class.getName());

    @Override
    public void start(Stage stage) throws IOException
    {
        // Schriftart laden
        Font.loadFont(getClass().getResource("Fonts/Geist/static/Geist-Regular.ttf").toExternalForm(), 10);

        FXMLLoader fxmlLoader = new FXMLLoader(AppMain.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // CSS laden
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // Fenstergröße festlegen
        stage.setWidth(1200); // Breite
        stage.setHeight(800); // Höhe

        // Fenster-Titel
        stage.setTitle("Sunset Cats Immobilien");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        initializeDebugLogger(); // ! HAS TO RUN FIRST
        launch();

        // Debug.info("Application started");
    }

    /*
    private static void runSearch()
    {
        try
        {
            Search searchImmo = new Search("https://www.willhaben.at/iad/immobilien/mietwohnungen/mietwohnung-angebote",
                    true);

            // Search searchImmo = new Search("https://www.willhaben.at/iad/immobilien/haus-kaufen/haus-angebote?sfId=4d397651-95f3-402c-8fd4-8eb4c15d49a8&isNavigation=true&rows=30&areaId=900&NO_OF_ROOMS_BUCKET=5X5", false );
            // Search searchImmo = new Search("https://www.willhaben.at/iad/immobilien/");

            ArrayList<Immobilie> results = willhabenConnector.startSearch(searchImmo);

            printResults(results);
        }
        catch (IllegalStateException emptyResults)
        {
            Debug.severe("SearchResults were empty!");
            Debug.info(emptyResults.getMessage());
        }
        catch (IllegalArgumentException e)
        {
            Debug.severe("Wrong URL Format");
            Debug.info(e.getMessage());
        }
        catch (Exception e)
        {
            Debug.severe("Unexpected error during search!");
            Debug.info(e.getMessage());
        }
    }*/


    private static void printResults(ArrayList<Immobilie> searchResults)
    {
        for (Immobilie currentImmobilie : searchResults)
        {
            Double immoPrice = currentImmobilie.getAttribute(Immobilie.AttributeKey.PRICE);
            Debug.info(String.format("ID: %s | PRICE: %f  -  %s", currentImmobilie.getAttribute(Immobilie.AttributeKey.ID),
                    immoPrice, currentImmobilie.getAttribute(Immobilie.AttributeKey.DESCRIPTION)));
        }
        System.out.println(searchResults.size());
    }

    private static void initializeDebugLogger() {
        try (InputStream inputStream = AppMain.class.getClassLoader().getResourceAsStream("logging.properties")) {
            if (inputStream == null) {
                System.err.println("Could not find logging.properties file.");
                return;
            }

            // Load and apply the logging configuration
            LogManager.getLogManager().readConfiguration(inputStream);

            // Retrieve properties for programmatic application
            Properties props = new Properties();
            props.load(AppMain.class.getClassLoader().getResourceAsStream("logging.properties"));

            Logger rootLogger = Logger.getLogger("");

            // Apply the global log level
            Level globalLevel = Level.parse(props.getProperty(".level", "INFO"));
            rootLogger.setLevel(globalLevel);

            // Apply configuration for handlers
            for (Handler handler : rootLogger.getHandlers()) {
                // Set handler-specific log levels
                if (handler instanceof ConsoleHandler) {
                    String consoleHandlerLevel = props.getProperty("java.util.logging.ConsoleHandler.level", "INFO");
                    handler.setLevel(Level.parse(consoleHandlerLevel));

                    // Set the formatter for ConsoleHandler
                    String consoleFormatter = props.getProperty("java.util.logging.ConsoleHandler.formatter");
                    if (consoleFormatter != null) {
                        try {
                            Formatter formatter = (Formatter) Class.forName(consoleFormatter).getDeclaredConstructor().newInstance();
                            handler.setFormatter(formatter);
                        } catch (Exception e) {
                            System.err.println("Failed to set formatter: " + e.getMessage());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to load logging configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}