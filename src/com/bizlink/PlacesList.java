package com.bizlink;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

//List of places
public class PlacesList implements Serializable {
	 @Key
	    public String status;
	 
	    @Key
	    public List<Place> results;

}
