package fhcampus.sunsetcats.fhcampusprog1sunsetcats.DataHandling;


import fhcampus.sunsetcats.fhcampusprog1sunsetcats.Search;

import java.net.http.HttpClient;



public abstract class DataConnector
{

    protected final HttpClient httpClient; //Der Client mit dem der Connector sich verbindet
    protected final String BASE_URL; //Die Grund URL der Website z.B https://willhaben.at/


    //Constructor - wird immer von den Child Classes aufgerufen
    protected DataConnector(String BASE_URL)
    {
        if (BASE_URL == null || BASE_URL.isEmpty()) {
            throw new IllegalArgumentException("Base URL cannot be null or empty");
        }

        //Initialisiere Variablen
        this.BASE_URL = BASE_URL;
        this.httpClient = HttpClient.newHttpClient();
    }


    //===========================================================================|| ABSTRACT FUNCTIONS ||===========================================================================================================

    /*
    Diese Funktionen müssen von den Child Classes implementiert werden
     */

     //public abstract String getStartURL();
     //public abstract void setStartURL(String START_URL);

    public abstract Search getLastSearch(); //Zwingt Child Klasses zur Variable 'Search lastSearch'



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



}

