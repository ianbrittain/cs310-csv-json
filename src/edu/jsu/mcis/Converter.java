package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and other whitespace
        have been added for clarity).  Note the curly braces, square brackets, and double-quotes!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160","111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();    
           
            
            
            JSONObject jsonObject = new JSONObject();
            
            JSONArray colHeaders = new JSONArray();
            JSONArray rowHeaders = new JSONArray();
            JSONArray data = new JSONArray();
            JSONArray datarow;
            String[] csvrow;           
            csvrow = iterator.next();
            
            for(int i = 0; i < csvrow.length; ++i){
                colHeaders.add(csvrow[i]);
            }
            
            while(iterator.hasNext()){
                csvrow = iterator.next();
                datarow = new JSONArray();
                rowHeaders.add(csvrow[0]);
                
                for(int i = 1; i < csvrow.length; ++i){                    
                    datarow.add(Integer.parseInt(csvrow[i]));
                    
                }
                data.add(datarow);
            }
            jsonObject.put("colHeaders", colHeaders);
            jsonObject.put("rowHeaders", rowHeaders);
            jsonObject.put("data", data);
            results = JSONValue.toJSONString(jsonObject);
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {
            
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            JSONArray cols = (JSONArray) jsonObject.get("colHeaders");
            JSONArray rows = (JSONArray) jsonObject.get("rowHeaders");
            JSONArray data = (JSONArray) jsonObject.get("data");
            
            
            String[] colHeading = new String [cols.size()];
            
            for(int i =  0;  i < cols.size(); ++i){
               colHeading[i] = (String) cols.get(i);         
            }
            csvWriter.writeNext(colHeading);
                  
            
           for(int i = 0; i < rows.size(); ++i){
               String[] combinedData = new String[colHeading.length];
               combinedData[0] = (String) rows.get(i);
               ArrayList individualData = (ArrayList) data.get(i);
               for(int j = 0; j < individualData.size(); ++j){
                  combinedData[j+1] = (String) String.valueOf(individualData.get(j));                                   
                  
               }
               
               csvWriter.writeNext(combinedData);
           }
           results += writer.toString();             
            
        }
        
        catch(ParseException e) { return e.toString(); }
        
        return results.trim();
        
    }
	
}