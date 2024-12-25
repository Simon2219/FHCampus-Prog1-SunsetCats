package fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;


import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Search;

import java.net.http.HttpClient;



public abstract class DataConnector
{

    protected final HttpClient httpClient; //Der Client mit dem der Connector sich verbindet
    protected Search lastSearch;


    protected final String BASE_URL; //Die Grund URL der Website z.B https://willhaben.at/
    protected final String DEFAULT_START;

    protected final String categoryTag; //Tag of current category to search
    protected final String cssQueryTag;


    //Constructor - wird immer von den Child Classes aufgerufen
    protected DataConnector(String BASE_URL, String DEFAULT_START, String categoryTag, String cssQueryTag)
    {
        if (BASE_URL == null || BASE_URL.isEmpty()) {
            throw new IllegalArgumentException("Base URL cannot be null or empty");
        }

        //Initialisiere Variablen
        this.BASE_URL = BASE_URL;
        this.DEFAULT_START = DEFAULT_START;
        this.httpClient = HttpClient.newHttpClient();

        this.categoryTag = categoryTag;
        this.cssQueryTag = cssQueryTag;
    }


    //===========================================================================|| ABSTRACT FUNCTIONS ||===========================================================================================================

    /*
    Diese Funktionen müssen von den Child Classes implementiert werden
     */

     //public abstract String getStartURL();
     //public abstract void setStartURL(String START_URL);



    //===========================================================================|| SIDE FUNCTIONS ||===========================================================================================================


    //Base Getter Functions

    public String getBaseURL()
    {
        return this.BASE_URL;
    }

    public HttpClient getHttpClient()
    {
        return this.httpClient;
    }

    public Search getLastSearch()
    {
        return this.lastSearch;
    }

    public String getCategoryTag()
    {
        return categoryTag;
    }

    public String getCssQueryTag()
    {
        return cssQueryTag;
    }

}

