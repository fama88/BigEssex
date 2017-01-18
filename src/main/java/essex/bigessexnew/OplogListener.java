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
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.util.JSON;
import essex.bigessexnew.Utils.Params;
import org.bson.types.BSONTimestamp;
import org.bson.types.ObjectId;
import org.json.JSONObject;


/**
 *
 * @author Fabio
 */
public class OplogListener {
    private DBCollection collection;
    
    public OplogListener(String host, String port, String db, String collection) {
        
        Mongo mongo = new Mongo(host, Integer.parseInt(port));
        this.collection = mongo.getDB(db).getCollection(collection);
    }
    
    
    public void listen(boolean processHistory, String... logFields) {
        BasicDBObject query = prepareOplogListenQuery(processHistory);
        BasicDBObject fields = prepareOplogListenFields(logFields);
        
        DBCursor cur = collection.find(query, fields).sort((BasicDBObjectBuilder.start("$natural", 1)).get())
                        .addOption(Bytes.QUERYOPTION_TAILABLE | Bytes.QUERYOPTION_AWAITDATA | Bytes.QUERYOPTION_NOTIMEOUT);
        
        performListenTask(cur);
    }
    
    private BasicDBObject prepareOplogListenQuery(boolean processHistory) {
        BasicDBObject query = new BasicDBObject();
        query.put("ns",Params.getProperty("shardedDB")+"."+Params.getProperty("shardedCollection"));
        if (!processHistory) {
            BSONTimestamp bs = new BSONTimestamp((int)(System.currentTimeMillis()/1000), 1);
            query.put("ts", new BasicDBObject("$gt", bs));
        }
        return query;
    }
    
    private BasicDBObject prepareOplogListenFields(String... logFields) {
        BasicDBObject fields = new BasicDBObject();
        for (String field: logFields)
            fields.put(field, 1);
        return fields;
    }
    
    private void performListenTask(DBCursor cur) {
        EssexDataHandler dh = new EssexDataHandler(Params.getProperty("essexHostPath"));
        Mongo mongo2 = new Mongo(Params.getProperty("host"), Integer.parseInt(Params.getProperty("port")));
        DBCollection realCollection = mongo2.getDB(Params.getProperty("shardedDB")).getCollection(Params.getProperty("shardedCollection"));
        Runnable task = () -> {
            System.out.println("\tWaiting for events");
            while (cur.hasNext()) { 
                DBObject obj = cur.next();
                System.out.println(obj.toString());
                JSONObject output = new JSONObject(JSON.serialize(obj));
                String id = output.getJSONObject("o").getJSONObject("_id").getString("$oid");
                String opType = output.getString("op");
                String content = retrieveContent(realCollection, id);
                dh.handle(content, opType);

        }
    }; 
        new Thread(task).start();
    }
    
    private String retrieveContent(DBCollection realCollection, String id) {
        BasicDBObject query = new BasicDBObject();
        query.put("_id", new ObjectId(id));
        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("content", 1);
        return realCollection.findOne(query, fields).get("content").toString();
        
    }
}
