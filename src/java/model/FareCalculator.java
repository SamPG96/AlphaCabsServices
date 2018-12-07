/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Sam
 */
public class FareCalculator {
    private static final String CONFIG_TABLE = "Configurations";
    
    /*
    * Calculate the cost for a distance
    */
    public static double calculate(double distance, JdbcReadOnly jdbc){
        double shortDistance;
        double shortDistPrice;
        double pricePerMile;
        double charge;
        
        shortDistance = getShortDistance(jdbc);
        shortDistPrice = getShortDistancePrice(jdbc);
        
        // Charge a flat rate if the distance is under a set distance. Otherwise
        // charge the flat rate plus cost per mile.
        if (distance > shortDistance){
            pricePerMile = getPricePerMile(jdbc);
            charge = shortDistPrice + (pricePerMile * distance);
        }
        else{
            charge = shortDistPrice;
        }
        
        return charge;
    }

    /*
    * Add VAT to a value
    */
    public static double addVAT(double value, JdbcReadOnly jdbc){
        ArrayList<HashMap<String,String>> results;
        double vat;
        
        results = jdbc.retrieve(CONFIG_TABLE, "CONFIGNAME", "VAT");
        vat = Double.valueOf(results.get(0).get("CONFIGVALUE"));
        
        return value + (value * (vat / 100));
    }    
    
    /*
    * Retrieve price per mile
    */
    private static double getPricePerMile(JdbcReadOnly jdbc){
        ArrayList<HashMap<String,String>> results;
        
        results = jdbc.retrieve(CONFIG_TABLE, "CONFIGNAME", "PricePerMile");
        
        return Double.valueOf(results.get(0).get("CONFIGVALUE"));
    }    
    
    /*
    * Retrieve what is classed a 'short' distance from the DB
    */
    private static double getShortDistance(JdbcReadOnly jdbc){
        ArrayList<HashMap<String,String>> results;
        
        results = jdbc.retrieve(CONFIG_TABLE, "CONFIGNAME", "ShortDistance");
        
        return Double.valueOf(results.get(0).get("CONFIGVALUE"));
    }

    /*
    * Retrieve what the cost is for a short distance from the DB
    */
    private static double getShortDistancePrice(JdbcReadOnly jdbc){
        ArrayList<HashMap<String,String>> results;
        
        results = jdbc.retrieve(CONFIG_TABLE, "CONFIGNAME", "ShortDistPrice");
        
        return Double.valueOf(results.get(0).get("CONFIGVALUE"));
    }
}
