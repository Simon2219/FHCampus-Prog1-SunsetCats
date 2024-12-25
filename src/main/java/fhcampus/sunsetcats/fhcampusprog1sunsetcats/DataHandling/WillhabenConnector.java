package fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;

import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Immobilie;
import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;



public class WillhabenConnector extends DataConnector
{

    //Constructor
    public WillhabenConnector()
    {
        super("https://www.willhaben.at",
                "https://www.willhaben.at/iad/immobilien/",
                "/iad/immobilien",
                "a[href^='/iad/immobilien/']"); //Set Base Link

    }


    //===========================================================================|| MAIN FUNCTIONS ||===========================================================================================================


    //Beginnt eine Suche - Muss ein Search Objekt übergeben werden
    public Optional<ArrayList<Immobilie>> startSearch(Search currentSearch)
    {
        try
        {
            WillhabenScraper scraper = new WillhabenScraper(this, currentSearch); //Neue Scraper Instanz
            scraper.start();

            lastSearch = currentSearch;

            return Optional.of(processSearchResults(currentSearch)); //Wandelt die Suchergebnisse in Immobilien Objekte und gibt diese zurück
        }
        catch(IllegalArgumentException e)
        {
            System.err.println("WILLHABEN CONNECTOR: Wrong URL Format");
            System.err.println(e.getMessage());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return Optional.empty();
    }



    //Wandle Suchergebnisse in Immobilien Objekte um - Spezifisch zu jeder Website
    private ArrayList<Immobilie> processSearchResults(Search currentSearch)
    {
        ArrayList<Immobilie> results = new ArrayList<>();

        for(HashMap<String,String> searchResult : currentSearch.rawSearchResults.values())
        {
            Immobilie neueImmobilie = new Immobilie();

            neueImmobilie.setAttribute(Immobilie.AttributeKey.ID, searchResult.get("id"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.VERTICAL_ID, searchResult.get("verticalId"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.AD_TYPE_ID, searchResult.get("adTypeId"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PRODUCT_ID, searchResult.get("productId"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.DESCRIPTION, searchResult.get("description"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.IMMO_TYPE, searchResult.get("PROPERTY_TYPE"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.POSTCODE, searchResult.get("POSTCODE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.DISTRICT, searchResult.get("DISTRICT"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.ADDRESS, searchResult.get("ADDRESS"));

            neueImmobilie.setAttribute(Immobilie.AttributeKey.PRICE, searchResult.get("PRICE"));
            neueImmobilie.setAttribute(Immobilie.AttributeKey.PRICE_DISPLAY, searchResult.get("PRICE_FOR_DISPLAY"));



            results.add(neueImmobilie);

             /*
            // Assign to variables based on the attribute name
            switch (name)
            {
                case "POSTCODE":
                    String postcode = value;

                    break;
                case "DISTRICT":
                    String district = value;
                    break;
                case "LOCATION":
                    String locationName = value;
                    break;
                case "STATE":
                    String state = value;
                    break;
                case "COUNTRY":
                    String country = value;
                    break;
                case "ADDRESS":
                    String Adress = value;
                    break;
                case "COORDINATES":
                    String coordinates = value;
                    break;
                case "UNIT_NUMBER":
                    String unitNumberString = value;

                case "PRICE":
                    String price = value;
                    break;
                case "PRICE_FOR_DISPLAY":
                    String priceForDisplay = value;
                    break;
                case "RENT/PER_MONTH_LETTINGS":
                    Float rentPerMonth = Float.parseFloat(value);
                    break;
                case "ESTATE_PRICE/PRICE_SUGGESTION":
                    String priceSuggestion = value;
                    break;
                case "PROJECT/UNIT_PRICE_FROM":
                    String unitPrice = value;
                    break;
                case "PROJECT/UNIT_RENT_FROM":
                    String unitRent = value;
                    break;

                case "ESTATE_SIZE/LIVING_AREA":
                    String livingArea = value;
                    break;
                case "ESTATE_SIZE/USEABLE_AREA":
                    String useableArea = value;
                    break;
                case "ESTATE_SIZE":
                    String estateSize = value;
                    break;
                case "FREE_AREA/FREE_AREA_AREA_TOTAL":
                    String freeArea = value;
                    break;
                case "FREE_AREA_TYPE":
                    String freeAreaType = value;
                    break;
                case "FREE_AREA_TYPE_NAME":
                    String freeAreaTypeName = value;
                    break;
                case "NUMBER_OF_ROOMS":
                    String numberOfRooms = value;
                    break;
                case "ROOMS":
                    String rooms = value;
                    break;
                case "FLOOR":
                    String FloorNrString = value;
                    break;
                case "NUMBER_OF_CHILDREN":
                    String numberOfChildren = value;
                    break;

                case "BODY_DYN":
                    String bodyDescription = value;
                    break;
                case "PROPERTY_TYPE":
                    String propertyType = value;
                    break;
                case "PROPERTY_TYPE_ID":
                    String propertyTypeID = value;
                    break;
                case "PROPERTY_TYPE_HOUSE":
                    String propertyTypeHouse = value; //true - is Apartment  | false - is not Apartment
                    break;
                case "PROPERTY_TYPE_FLAT":
                    String isApartment = value; //true - is Apartment  | false - is not Apartment
                    break;


                case "VIRTUAL_VIEW_LINK":
                    String virtualView = value;
                    break;
                case "imagedescription":
                    String imageDescription = value;
                    break;
                case "ALL_IMAGE_URLS":
                    String allImageUrls = value;
                    break;


                case "ORGNAME":
                    String organizationName = value;
                    break;
                case "ORGID":
                    String orgID = value;
                    break;
                case "ORG_UUID":
                    String orgUUID = value;
                    break;


                case "LOCATION_ID":
                    String locationID = value;
                    break;
                case "LOCATION_QUALITY":
                    String locationQuality = value;
                    break;


                case "HEADING":
                    String heading = value;
                    break;
                case "PUBLISHED":
                    String published = value;
                    break;
                case "PUBLISHED_String":
                    String publishedString = value;
                    break;


                case "SEO_URL":
                    String seoURL = value;
                    break;
                case "ADTYPE_ID":
                    String adTypeID = value;
                    break;
                case "ADID":
                    String adID = value;
                    break;
                case "ADVERTISER_REF":
                    String advertiserRef = value;
                    break;
                case "UPSELLING_AD_SEARCHRESULT":
                    String upsellingAd = value;
                    break;


                case "ESTATE_PREFERENCE":
                    String estatePreference = value;
                    break;
                case "categorytreeids":
                    String categoryTreeIDs = value;
                    break;
                case "PRODUCT_ID":
                    String productID = value;
                    break;
                case "IS_BUMPED":
                    String isBumped = value;
                    break;
                case "MMO":
                    String mmo = value;
                    break;
                case "AD_UUID":
                    String adUUID = value;
                    break;
                case "ISPRIVATE":
                    String isPrivate = value;
                    break;
                case "UNIT_TITLE":
                    String unitTitle = value;
                    break;
                case "AD_SEARCHRESULT_LOGO":
                    String searchResultLogo = value;
                    break;
                case "PROJECT_ID":
                    String projectID = value;
                    break;


                default:
                    System.err.println("Unexpected Value in Array - " + name + ": " + value);
                    break;

            }*/
        }

        return results;
    }






    /*

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


    //Gibt die letzte durchgeführte Suche zurück




}
