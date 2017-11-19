package io.hedwig.igniteusage.store;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * 1. author: patrick
 */
public class MongoDataStore {
  MongoClient mongoClient;
  MongoDatabase database;
  DBCollection collection;

  public Boolean init() {
    mongoClient = new MongoClient("localhost" , 27017);
    database = mongoClient.getDatabase("URL");
    collection = (DBCollection) database.getCollection("zippedURL");

    return true;
  }
  public Boolean ifURLUnique(final String hashedURL) {
    BasicDBObject query = new BasicDBObject();
    query.put("hashedUrl", hashedURL);

    DBCursor res = collection.find(query);

    if(res.count() > 0)
      return false;

    return true;
  }
}
