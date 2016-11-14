package DataImpl;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by Seven on 2016/11/13.
 */
public class MongoDBConnector {

    public MongoDatabase getMongoDBConnection(){
        // 连接到 mongodb 服务
        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
        // 连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("ticket_system");
        return mongoDatabase;
    }
}
