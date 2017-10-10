package tcc.radarsocial.dao;

import java.text.ParseException;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import tcc.radarsocial.db.ConnectionFactory;

public class FeedsDao {


	DBCollection collection = ConnectionFactory.connectDB().getCollection("FeedsMetricas");
//	DBCollection collectionFace = ConnectionFactory.connectDB().getCollection("FacebookMetricas");
//	DBCollection collectionTwitter = ConnectionFactory.connectDB().getCollection("TwitterMetricas");
	
	public DBCursor buscaPorFiltro(String portal, String dataInicial, String dataFinal,String redeSocial, String orderBy, String palavraChave) throws ParseException{
		
		DBObject clausePortal = null;
		DBObject clauseData = null;
		DBObject clauseRede = null;
		DBObject clausePalavraChave = null;
		
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
		
		if(!palavraChave.equals("")){
			clausePalavraChave = (DBObject) JSON.parse("{ \"mensagem\" :  {\"$regex\": \""+palavraChave+"\",\"$options\": \"i\"} }"); 
			and.add(clausePalavraChave);
		}
			
		DBObject query = new BasicDBObject("$and", and);
		DBCursor cursor = null;
		if(orderBy.equals("mensagem"))
			cursor = collection.find(query).sort(new BasicDBObject(orderBy, 1));	
		else
			cursor = collection.find(query).sort(new BasicDBObject(orderBy, -1));
				
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
	
	
	public String buscarComparativoAgregacaoRedes(String portalFacebook,String portalTwitter,String dataInicial,String dataFinal){
		
		BasicDBObject matchFacebook = null;
		BasicDBObject matchTwitter = null;
		
		if(!portalFacebook.isEmpty()){
			
			
			matchFacebook = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
					+ "{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"nomePagina\": \""+portalFacebook+"\"}]}}");
		
//			matchFacebook = (BasicDBObject) JSON.parse("{ \"$match\": {\"nomePagina\": \""+portalFacebook+"\"}}");
			
			
			}
		
		if(!portalTwitter.isEmpty()){
//			matchTwitter = (BasicDBObject) JSON.parse("{ \"$match\": {\"nomeTwitter\": \""+portalTwitter+"\"}}");
			matchTwitter = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
					+ "{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"nomeTwitter\": \""+portalTwitter+"\"}]}}");
		
		}		
		
		// $group operation
		BasicDBObject groupFieldsFacebook = new BasicDBObject( "_id", "$dataCriacao");
		groupFieldsFacebook.put("sum_reactions", new BasicDBObject( "$sum", "$reactions"));

		BasicDBObject groupFieldsTwitter = new BasicDBObject( "_id", "$dataCriacao");
		groupFieldsTwitter.put("sum_retweets", new BasicDBObject( "$sum", "$retweets"));
		
		
		BasicDBObject groupFace = new BasicDBObject("$group", groupFieldsFacebook);
		BasicDBObject groupTwitter = new BasicDBObject("$group", groupFieldsTwitter);
		
		//sort
		DBObject sortFields = new BasicDBObject("dataCriacao", 1);
		DBObject sort = new BasicDBObject("$sort", sortFields );
		
		// run aggregation
		AggregationOutput outputFace = collection.aggregate(matchFacebook, groupFace,sort);
		AggregationOutput outputTwitter = collection.aggregate(matchTwitter, groupTwitter,sort);
		
		
		String jsonTwitter = null;
		String jsonFace = null;
		
		if(!outputTwitter.results().toString().equals(""))
			jsonTwitter =  outputTwitter.results().toString().replaceAll("\\[+\\]+","");
		
		if(!outputFace.results().toString().equals(""))
			jsonFace = outputFace.results().toString().replaceAll("\\[+\\]+","");
		
		String serialize = null;
		if(!jsonFace.equals("") && !jsonTwitter.equals(""))
			serialize = "[" + jsonTwitter + ", " + jsonFace + "]";
		else
			serialize = "";
			
		return serialize;
		
	}
	
	
}
