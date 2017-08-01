package tcc.radarsocial.dao;

import java.text.ParseException;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import tcc.radarsocial.db.ConnectionFactory;

public class FeedsDao {


	DBCollection collection = ConnectionFactory.connectDB().getCollection("FeedsMetricas");
	
	public DBCursor buscaPorFiltro(String portal, String dataInicial, String dataFinal,String redeSocial) throws ParseException{
		
		DBObject clausePortal = null;
		DBObject clauseData = null;
		
		BasicDBList and = new BasicDBList();
		
		if(redeSocial.equals("Facebook")){
		
			if(!portal.isEmpty()){
				clausePortal = new BasicDBObject("nomePagina", portal); 
				and.add(clausePortal);
			}
		}
		else if(redeSocial.equals("Twitter")){
			if(!portal.isEmpty()){
				clausePortal = new BasicDBObject("nomeTwitter", portal); 
				and.add(clausePortal);
			}
		}
		if(!dataInicial.isEmpty() && !dataFinal.isEmpty()){
			clauseData = (DBObject) JSON.parse("{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
			and.add(clauseData);
		}
				
		
		DBObject query = new BasicDBObject("$and", and);
		
		DBCursor cursor = collection.find(query);
		
		return cursor;
	}
}
