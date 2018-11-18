package example.database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import example.constants.Constant;
import org.bson.Document;

public class MongoDB {
    private static final MongoClient mongoClient = new MongoClient();
    private static final MongoDatabase database = mongoClient.getDatabase(Constant.DATABASE_NAME);
    public static final MongoCollection<Document> courses = database.getCollection(Constant.COLLECTION_NAME);
}
