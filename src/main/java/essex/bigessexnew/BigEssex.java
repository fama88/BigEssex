/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package essex.bigessexnew;

import essex.bigessexnew.Utils.Params;

/**
 *
 * @author Fabio
 */
public class BigEssex {
    
    public static void main(String[] args) {
        parseInput(args);
        OplogListener ml = new OplogListener(Params.getProperty("host"), Params.getProperty("port"), Params.getProperty("listenedDB"), Params.getProperty("listenedCollection"));
        ml.listen(false, "o._id", "op");
        
        
        new Thread(new TwitterStreamHandler(Params.getProperty("mongosHost"), Params.getProperty("mongosPort"),
                Params.getProperty("shardedDB"), Params.getProperty("shardedCollection"))).start();
    }
    
    public static void parseInput(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("-config") || args[0].equals("--c"))
                Params.initialize(args[1]);
            else
                printUsage();
        }
        else {
            Params.initialize();
        }
    
    }
    
    public static void printUsage() {
        System.out.println("Usage: java BigEssex <-config|--c propertyFileName>");
    }
}
