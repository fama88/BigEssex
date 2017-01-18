/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package essex.bigessexnew.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Fabio
 */
public class Params {

    
    private static Properties properties;
    
    public static void initialize(String fileName) {
    /*    
        Properties defaults = initialize();
        FileInputStream in = null;
        properties = new Properties(defaults);
        
        try {
            in = new FileInputStream(fileName);
            properties.load(in);
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Params.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Params.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    public static Properties initialize() {
        //Properties defaultProperties = new Properties();
        properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream("default.properties");
            properties.load(in);
            in.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Params.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Params.class.getName()).log(Level.SEVERE, null, ex);
        }
        return properties;
    }
    
    public static String getProperty(String name) {
        return properties.getProperty(name);
    }
    
    
}
