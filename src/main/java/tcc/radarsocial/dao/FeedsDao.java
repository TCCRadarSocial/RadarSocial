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
	
	public DBCursor buscaPorFiltro(String portal, String dataInicial, String dataFinal,String redeSocial, String orderBy) throws ParseException{
		
		DBObject clausePortal = null;
		DBObject clauseData = null;
		DBObject clauseRede = null;
		
		BasicDBList and = new BasicDBList();
				
		if(redeSocial.equals("Facebook")){
		
			if(!portal.isEmpty()){
				clausePortal = new BasicDBObject("nomePagina", portal); 
				and.add(clausePortal);
			}else{
				clauseRede = new BasicDBObject("tipoRede", "facebook"); 
				and.add(clauseRede);
			}
		}
		else if(redeSocial.equals("Twitter")){
			if(!portal.isEmpty()){
				clausePortal = new BasicDBObject("nomeTwitter", portal); 
				and.add(clausePortal);
			}else{
				clauseRede = new BasicDBObject("tipoRede", "twitter"); 
				and.add(clauseRede);
			}
		}
		if(!dataInicial.isEmpty() && !dataFinal.isEmpty()){
			clauseData = (DBObject) JSON.parse("{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
			and.add(clauseData);
		}
			
		DBObject query = new BasicDBObject("$and", and);
		
		DBCursor cursor = collection.find(query).sort(new BasicDBObject(orderBy, -1));
		
		return cursor;
	}
	
	
	public DBCursor buscaPorFiltroPorLink(String link) throws ParseException{
				
		BasicDBList and = new BasicDBList();
		
		DBObject clauseLink = new BasicDBObject("link", link); 
		and.add(clauseLink);
			
		DBObject query = new BasicDBObject("$and", and);
		
		DBCursor cursor = collection.find(query);
		
		return cursor;
	}
	
	public void excluirRegistros(String nome,String tipo){
		
		BasicDBObject query = new BasicDBObject();
		
		if(tipo.equals("facebook")){
			query.put("nomePagina", nome);
		}else if(tipo.equals("twitter")){
			query.put("nomeTwitter", nome);
		}
				
		collection.remove(query);
		
	}
	
}
