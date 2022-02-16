package com.kindminds.drs.persist.data.access.nosql.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.connection.ConnectionPoolSettings;
import org.bson.Document;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DrsMongoClient {

    /* prod
    ConnectionString connString = new ConnectionString(
            "mongodb://drsMongo:vgy7ujm@35.230.119.69:27017/drs"
    );
     */

    private static Properties mongoProperties = null;

    static {
        mongoProperties = new Properties();
        String resourceName = "mongo.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
            mongoProperties.load(resourceStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static  MongoClient mongoClient = null;
    private static  MongoDatabase database = null;

    public MongoClient getClient(){
        return mongoClient;
    }

    public MongoDatabase getDatabase(){
        return database;
    }

    public MongoCollection<Document> getCollection(String name){
        return database.getCollection(name);
    }


    private static DrsMongoClient instance;

    private DrsMongoClient() {
    }

    public static DrsMongoClient getInstance() {
        if (instance == null) {

            ConnectionString connString = new ConnectionString(
                    "mongodb://drsMongo:vgy7ujm@"+ mongoProperties.getProperty("host") +":27017/drs"
            );


            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connString)
                    .applyToConnectionPoolSettings(builder ->
                            builder.maxSize(20))
                    .retryWrites(true)
                    .build();

            mongoClient = MongoClients.create(settings);

            database = mongoClient.getDatabase("drs");

            instance = new DrsMongoClient();

//            System.out.println("Pool size: " +
//                    settings.getConnectionPoolSettings().getMaxSize());
        }
        return instance;
    }



}
