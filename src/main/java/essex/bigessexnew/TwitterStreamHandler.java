/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package essex.bigessexnew;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import essex.bigessexnew.Utils.Params;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/**
 *
 * @author Fabio
 */
public class TwitterStreamHandler implements StreamHandler, Runnable {
    public final DBCollection collection;
    public TwitterStreamHandler(String host, String port, String DBName, String collectionName) {
        Mongo mongoClient = new Mongo(host, Integer.parseInt(port));
        collection = mongoClient.getDB(DBName).getCollection(collectionName);
    }
    @Override
    public void run() {
        StatusListener sl = new StatusListener() {
            @Override
            public void onStatus(Status status) {
                    /*String s = TwitterObjectFactory.getRawJSON(status);
                    System.out.println(s);*/
                    BasicDBObjectBuilder documentBuilder = BasicDBObjectBuilder.start();
                    documentBuilder.add("content", status.getText());
                    collection.insert(documentBuilder.get());
                
                /*DBObject dbObject = (DBObject) JSON.parse(json);
                collection.insert(dbObject);*/
                //System.out.println(json.toString());
                
            
            }
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                
            }
 
            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
               
            }
 
            @Override
            public void onScrubGeo(long l, long l2) {
                
            }
 
            @Override
            public void onStallWarning(StallWarning stallWarning) {
                
            }
 
            @Override
            public void onException(Exception ex) {
 
            }
        };
        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.addListener(sl);
        twitterStream.filter(Params.getProperty("hashtag"));
    }

}

