/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.HashMap;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import model.DBConnector;
import model.RouteDistance;
import model.FareCalculator;
import model.JdbcReadOnly;


/**
 *
 * @author Sam
 */
@WebService(serviceName = "Fare")
@Stateless()
public class Fare {
    private static final String DB_PATH = "jdbc:derby://localhost:1527/alphacabsdb/";
    private static final String DB_USER = "ateam";
    private static final String DB_PASS = "ateam";

    /**
     * Web service operation
     */
    @WebMethod(operationName = "calculate")
    public String calculateFare(@WebParam(name = "source") String source, @WebParam(name = "destination") String destination) {
        JdbcReadOnly jdbc;
        RouteDistance rd;
        HashMap<String, String> response;
        double distance;
        double fare;
        
        
        response = new HashMap();
        
        jdbc = DBConnector.connect(DB_PATH, DB_USER, DB_PASS);
        
        if (jdbc == null){
            // Cant connect to DB
            response.put("status", "-1");
        }
        
        // Calculate distance  
        rd = new RouteDistance();
        distance = rd.measure(source, destination);
        
        if (distance == -1){
            response.put("status", "-1");
            return response.toString();
        }
        else if (distance == -2){
            response.put("status", "-2");
            response.put("errMsg", rd.getErrMsg());
            
            return response.toString();
        }
        
        response.put("distance", String.valueOf(distance));
        
        // Calculate fare
        fare = FareCalculator.calculate(distance, jdbc);
        response.put("fareNoVAT", String.valueOf(fare));
        response.put("fareWithVAT", String.valueOf(FareCalculator.addVAT(fare, jdbc)));
        
        response.put("status", "0");
        
        return response.toString();
    }
}
