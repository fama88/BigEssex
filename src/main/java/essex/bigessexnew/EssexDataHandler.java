/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package essex.bigessexnew;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import essex.bigessexnew.Utils.Params;

/**
 *
 * @author Fabio
 */
public class EssexDataHandler implements DataHandler {
    String url = "";
    public EssexDataHandler(String hostURL) {
        url = hostURL;
    }
    
    public void handle(String content, String opType){
        switch(opType) {
            case "u": handleUpdate();
                        break;
            case "d": handleDeletion();
                        break;
            case "i": 
            default: handleInsertion(content);
        }
    }
    
    private String getEssexData(String content) {
        URLConnection connection = null;
        try {
            connection = new URL(url).openConnection();
        } catch (MalformedURLException ex) {
            Logger.getLogger(EssexDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EssexDataHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        String ok = "{\"jsonrpc\": \"2.0\", \"method\": \"analyzexml\", \"params\":"
                + "{\"package\": \""+Params.getProperty("langPackagePath")+"\","
                + "\"data\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><REQUEST VERSION=\\\"13.9.0\\\">"
                + "<DOCUMENT TYPE=\\\"PLAIN\\\" ><![CDATA["+content+"]]>"
                + "</DOCUMENT><ANALYSIS NAME=\\\"SYNTHESIS\\\"><PARAM NAME=\\\"ENTITIES\\\" VALUE=\\\"PEOPLE\\\">"
                + "<OPT NAME=\\\"PROPS\\\" VALUE=\\\"ALL\\\"/><OPT NAME=\\\"TRACKS\\\" VALUE=\\\"FULL\\\"/>"
                + "</PARAM><PARAM NAME=\\\"TAGS\\\" VALUE=\\\"DATE\\\"><OPT NAME=\\\"TRACKS\\\" VALUE=\\\"FULL\\\"/>"
                + "</PARAM></ANALYSIS></REQUEST>\"}, \"id\": \"1\"}";
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Lenght", Integer.toString(ok.length()));
        connection.setRequestProperty("Accept", "application/json");
        
        OutputStream output;
            try {
                output = connection.getOutputStream();
                output.write(ok.getBytes());
                output.close();
            } catch (IOException ex) {
                Logger.getLogger(MongoListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        
            try {
                InputStream response = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(response));
                String data = reader.readLine();
                return data;
            } catch (IOException ex) {
                Logger.getLogger(MongoListener.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "";
    }
    
    private void handleInsertion(String content) {
        String response = getEssexData(content);
        System.out.println(response);   
    }
    
    
    private void handleDeletion(){
        
    }
    
    private void handleUpdate() {
        
    }
    
}
