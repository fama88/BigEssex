/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package essex.bigessexnew;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Bytes;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import essex.bigessexnew.Utils.Params;
import java.util.Map;
import org.bson.types.BSONTimestamp;

/**
 *
 * @author Fabio
 */
public abstract class MongoListener {
    private DBCollection collection;
    
    public MongoListener(String host, String port, String db, String collection) {
        Mongo mongo = new Mongo(host, Integer.getInteger(port));
        this.collection = mongo.getDB(db).getCollection(collection);
    }
    
    public abstract void listen(boolean processHistory);
    
    public DBCursor performQuery(BasicDBObject query, BasicDBObject fields) {
        DBCursor cursor = collection.find(query, fields);
        return cursor;
        
    }
   


}
