/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import java.io.IOException;

/**
 *
 * @author Sam
 */
public class RouteDistance {
    private final String API_KEY = "AIzaSyDE0lTmdjNQQAfykaeW1cUI0Wzz5e_hu7E";
    private final double metreToMile = 0.000621371;
    private String errMsg;
    
    public double measure(String source, String destination){
        DistanceMatrix results;
        DistanceMatrixApiRequest req;
        GeoApiContext context;
        
        context = new GeoApiContext
                .Builder()
                .apiKey(API_KEY).build();
        
        req = DistanceMatrixApi.newRequest(context);
        
        try {
            // Query distance
            results = req.origins(source)
                    .destinations(destination)
                    .mode(TravelMode.DRIVING)
                    .language("en-UK")
                    .await();

            if (results.rows[0].elements[0].distance == null){
                // No results found
                return -1;
            }
            
            return results.rows[0].elements[0].distance.inMeters * metreToMile;
           
        } catch (ApiException | InterruptedException | IOException ex) {
            // Something unexpected happend
            errMsg = ex.toString();
            return -2;
        }
    }

    public String getErrMsg() {
        return errMsg;
    }
}
