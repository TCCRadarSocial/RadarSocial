package tcc.radarsocial.db;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class ConnectionFactory {

	public static DB connectDB(){
		DB db = null;
		
		try{
			
		Mongo mongo = new Mongo("localhost", 27017);
		db = mongo.getDB("RadarSocial");
	
		//DBCollection collection = db.getCollection("dummyColl");
		
		}catch (UnknownHostException e) {
			e.printStackTrace();
	    } catch (MongoException erro) {
	    	erro.printStackTrace();
	    }
		
		return db;
	}
}
