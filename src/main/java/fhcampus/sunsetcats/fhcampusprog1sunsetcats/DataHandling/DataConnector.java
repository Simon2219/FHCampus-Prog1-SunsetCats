package fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;


import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Search;

import java.net.http.HttpClient;



public abstract class DataConnector
{

    protected final HttpClient httpClient; //Der Client mit dem der Connector sich verbindet
    protected Search lastSearch;



    //Constructor - wird immer von den Child Classes aufgerufen
    protected DataConnector(HttpClient httpClient)
    {
        this.httpClient = httpClient;
    }


    //===========================================================================|| ABSTRACT FUNCTIONS ||===========================================================================================================

    /*
    Diese Funktionen müssen von den Child Classes implementiert werden
     */




    //===========================================================================|| SIDE FUNCTIONS ||===========================================================================================================


    //Base Getter Functions



    public HttpClient getHttpClient()
    {
        return this.httpClient;
    }

    public Search getLastSearch()
    {
        return this.lastSearch;
    }



}

