package com.napier.sem;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class App
{
    public static void main(String[] args)
    {
        // Get MongoDB connection details from environment variables
        // Defaults allow the application to run directly on the Mac
        String mongoHost = System.getenv().getOrDefault("MONGO_HOST", "localhost");
        int mongoPort = Integer.parseInt(
                System.getenv().getOrDefault("MONGO_PORT", "27000")
        );

        // Connect to MongoDB
        MongoClient mongoClient = new MongoClient(mongoHost, mongoPort);

        // Get a database - will create when we use it
        MongoDatabase database = mongoClient.getDatabase("mydb");

        // Get a collection from the database
        MongoCollection<Document> collection = database.getCollection("test");

        // Create a document to store
        Document doc = new Document("name", "Kevin Sim")
                .append("class", "DevOps")
                .append("year", "2024")
                .append("result", new Document("CW", 95).append("EX", 85));

        // Add document to collection
        collection.insertOne(doc);

        // Check document in collection
        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());
    }
}