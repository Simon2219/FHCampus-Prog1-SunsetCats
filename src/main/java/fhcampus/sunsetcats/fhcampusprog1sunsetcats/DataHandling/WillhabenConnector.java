package fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Immobilie;
import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Search;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.Double.parseDouble;

     /*
        Inhertis from DataConnector
        Immobilien Factory
        Specifies Website Specific behaviour for processing Search Results and Search Filters
     */

public class WillhabenConnector extends DataConnector
{
    private static final Logger Debug = Logger.getLogger(WillhabenConnector.class.getName()); //Debug Logger

    public WillhabenConnector()
    {
        super(HttpClient.newHttpClient());
    }


    //===========================================================================|| MAIN FUNCTIONS ||===========================================================================================================

    // Startpunkt der Suche - Muss ein befülltes Search Objekt übergeben werden
    public ArrayList<Immobilie> startSearch(Search currentSearch) throws IllegalStateException
    {
        WillhabenScraper scraper = new WillhabenScraper(this, currentSearch); // Jede Suche = Neuer Scraper
        scraper.start();

        lastSearch = currentSearch; // Speichere zuletzt durchgeführte Suche

        // Ergebnisse verarbeiten
        ArrayList<Immobilie> results = processSearchResults(currentSearch);
        if (results.isEmpty()) {
            throw new IllegalStateException("Search failed or returned no results.");
        }
        return results;
    }


/*
    Immo Factory
    Wandle Suchergebnisse in Immobilien Objekte um - Spezifisch zu jeder Website
    searchResult.get( KEY ) - KEY muss JSON Response der Website entnommen werden
*/

    private ArrayList<Immobilie> processSearchResults(Search currentSearch)
    {
        Debug.info("Anzahl der Suchergebnisse: " + currentSearch.getRawSearchResults().size());
        ArrayList<Immobilie> results = new ArrayList<>();

        for(HashMap<String,String> searchResult : currentSearch.getRawSearchResults().values())
        {
            String roomBucket = searchResult.get("NUMBER_OF_ROOMS"); // Willhaben-Daten

            Immobilie neueImmobilie = new Immobilie();

            neueImmobilie.setAttribute(Immobilie.AttributeKey.ID, searchResult.get("id"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.VERTICAL_ID, searchResult.get("verticalId"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.AD_TYPE_ID, searchResult.get("adTypeId"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PRODUCT_ID, searchResult.get("productId"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.DESCRIPTION, searchResult.get("description"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.IMMO_TYPE, searchResult.get("PROPERTY_TYPE"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.PRICE, searchResult.get("PRICE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PRICE_DISPLAY, searchResult.get("PRICE_FOR_DISPLAY"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.RENT_MONTH, searchResult.get("RENT_PER_MONTH_LETTINGS"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PRICE_SUGGESTION, searchResult.get("ESTATE_PRICE_PRICE_SUGGESTION"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PROJECT_UNIT_PRICE_FROM, searchResult.get("PROJECT_UNIT_PRICE_FROM"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PROJECT_UNIT_RENT_FROM, searchResult.get("PROJECT_UNIT_RENT_FROM"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.POSTCODE, searchResult.get("POSTCODE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.DISTRICT, searchResult.get("DISTRICT"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ADDRESS, searchResult.get("ADDRESS"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.LOCATION, searchResult.get("LOCATION"));neueImmobilie.setAttribute(Immobilie.AttributeKey.STATE, searchResult.get("STATE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.COUNTRY, searchResult.get("COUNTRY"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.COORDINATES, searchResult.get("COORDINATES"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.UNIT_NUMBER, searchResult.get("UNIT_NUMBER"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.ESTATE_SIZE_TOTAL, searchResult.get("ESTATE_SIZE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ESTATE_SIZE_LIVING_AREA, searchResult.get("ESTATE_SIZE_LIVING_AREA"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ESTATE_SIZE_USEABLE_AREA, searchResult.get("ESTATE_SIZE_USEABLE_AREA"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.FREE_AREA_TOTAL, searchResult.get("FREE_AREA_FREE_AREA_AREA_TOTAL"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.FREE_AREA_TYPE, searchResult.get("FREE_AREA_TYPE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.FREE_AREA_TYPE_NAME, searchResult.get("FREE_AREA_TYPE_NAME"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.NUMBER_OF_ROOMS, roomBucket);
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ROOMS, searchResult.get("ROOMS"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.FLOOR, searchResult.get("FLOOR"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.NUMBER_OF_CHILDREN, searchResult.get("NUMBER_OF_CHILDREN"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.BODY_DYN, searchResult.get("BODY_DYN"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.PROPERTY_TYPE_ID, searchResult.get("PROPERTY_TYPE_ID"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PROPERTY_TYPE_HOUSE, searchResult.get("PROPERTY_TYPE_HOUSE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PROPERTY_TYPE_FLAT, searchResult.get("PROPERTY_TYPE_FLAT"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.VIRTUAL_VIEW_LINK, searchResult.get("VIRTUAL_VIEW_LINK"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.IMAGE_DESCRIPTION, searchResult.get("IMAGE_DESCRIPTION"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ALL_IMAGE_URLS, searchResult.get("ALL_IMAGE_URLS"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.ORGNAME, searchResult.get("ORGNAME"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ORGID, searchResult.get("ORGID"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ORG_UUID, searchResult.get("ORG_UUID"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.LOCATION_ID, searchResult.get("LOCATION_ID"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.LOCATION_QUALITY, searchResult.get("LOCATION_QUALITY"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.HEADING, searchResult.get("HEADING"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PUBLISHED, searchResult.get("PUBLISHED"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PUBLISHED_STRING, searchResult.get("PUBLISHED_STRING"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.SEO_URL, searchResult.get("SEO_URL"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ADID, searchResult.get("ADID"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ADVERTISER_REF, searchResult.get("ADVERTISER_REF"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.UPSELLING_AD_SEARCHRESULT, searchResult.get("UPSELLING_AD_SEARCHRESULT"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.ESTATE_PREFERENCE, searchResult.get("ESTATE_PREFERENCE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.CATEGORY_TREE_IDS, searchResult.get("CATEGORY_TREE_IDS"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.IS_BUMPED, searchResult.get("IS_BUMPED"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.MMO, searchResult.get("MMO"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.AD_UUID, searchResult.get("AD_UUID"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.IS_PRIVATE, searchResult.get("IS_PRIVATE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.UNIT_TITLE, searchResult.get("UNIT_TITLE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.AD_SEARCHRESULT_LOGO, searchResult.get("AD_SEARCHRESULT_LOGO"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PROJECT_ID, searchResult.get("PROJECT_ID"));

            results.add(neueImmobilie);
            }

        return results;
    }

    private boolean matchesRooms(String roomBucket, Integer userInputRooms) {
        if (userInputRooms == null) {
            return true; // Kein Filter -> immer Treffer
        }
        try {
            int rooms = parseRoomsBucket(roomBucket); // Analysiere `roomBucket`
            return rooms == userInputRooms; // Vergleiche mit Benutzereingabe
        } catch (IllegalArgumentException e) {
            Debug.warning("Ungültiger Raumwert in `roomBucket`: " + roomBucket);
            return false; // Ungültige Daten -> Kein Treffer
        }
    }

    private int parseRoomsBucket(String bucketValue) {
        if (bucketValue == null || !bucketValue.matches("\\d+X\\d+")) {
            throw new IllegalArgumentException("Ungültiger Wert für NUMBER_OF_ROOMS: " + bucketValue);
        }
        String[] parts = bucketValue.split("X");
        return Integer.parseInt(parts[0]); // Gebe die Raumanzahl zurück
    }


    /*
    // Build Search URL´s from Search Filters
    private String buildURL(Search search) {
        StringBuilder url = new StringBuilder();

        // Start-URL hinzufügen
        url.append(search.getSearchStartURL());

        // Filter aus dem Search-Objekt hinzufügen
        if (!search.getSearchFilters().isEmpty()) {
            // Ersten Filter mit '?' anhängen
            url.append("?");
            url.append(search.getSearchFilters().get(0));

            // Alle weiteren Filter mit '&' anhängen
            for (int i = 1; i < search.getSearchFilters().size(); i++) {
                url.append("&").append(search.getSearchFilters().get(i));
            }
        }

        // Logging der generierten URL
        Debug.info("Generated Search URL: " + url.toString());
        return url.toString();
    }

    private String buildURL()
    {
        StringBuilder url = new StringBuilder();

        url.append(URLTAG_WILLHABEN_IMMO_DEFAULT);
        url.append(searchCategory);
        url.append("?rows=").append(searchCount);


        if (!searchCondition.isEmpty())
        {
            searchCondition.forEach(cond -> url.append("&treeAttributes=").append(cond));
        }
        if (!searchTransferType.isEmpty())
        {
            searchTransferType.forEach(trans -> url.append("&treeAttributes=").append(trans));
        }
        if (searchPayLivery)
        {
            url.append("&paylivery=true");
        }
        if (searchKeyword != null)
        {
            url.append("&keyword=").append(searchKeyword.replace(" ", "+"));
        }


        return url.toString();
    }
    */


    //===========================================================================|| SIDE FUNCTIONS ||===========================================================================================================




}
