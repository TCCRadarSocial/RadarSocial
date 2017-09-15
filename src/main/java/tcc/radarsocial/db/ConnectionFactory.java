package tcc.radarsocial.db;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
//import com.mongodb.client.MongoDatabase;

public class ConnectionFactory {
	
//	public static DB db = null;
//	public static MongoDatabase database = null;

	public static DB connectDB(){
		DB db = null;
		
		try{
			
//			MongoClient client = new MongoClient("localhost", 27017);
//			database = client.getDatabase("RadarSocial");
//			client.close();
//		if(db == null){
			
			
			
			Mongo mongo = new Mongo("localhost", 27017);
			db = mongo.getDB("RadarSocial");
//		}
	
		//DBCollection collection = db.getCollection("dummyColl");
		
		}catch (MongoException erro) {
	    	erro.printStackTrace();
	    	
	    } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return db;
	}
}
