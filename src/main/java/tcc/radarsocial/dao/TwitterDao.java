package tcc.radarsocial.dao;

import java.text.ParseException;
import java.util.Date;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import tcc.radarsocial.db.ConnectionFactory;
import tcc.radarsocial.model.Tweet;
import tcc.radarsocial.model.Twitter;

public class TwitterDao {

	ConnectionFactory con = new ConnectionFactory();
	DBCollection collection = ConnectionFactory.connectDB().getCollection("TwitterMetricas");
	DBCollection collectionFeeds = ConnectionFactory.connectDB().getCollection("FeedsMetricas");
	
	public void gravarDadosTwitter(Twitter twitter, Tweet tweet) throws ParseException{
		
		BasicDBObject document = new BasicDBObject();
		document.put("idTwitter", twitter.getIdTwitter());
		document.put("nomeTwitter", twitter.getNome());
		document.put("idTweet", tweet.getIdTweet());
				
		document.put("dataGravacao", new Date());
		document.put("dataCriacao", tweet.getDataCriacao());
		document.put("retweets", tweet.getRetweets());
		document.put("favorites", tweet.getFavorites());
		document.put("imagem", tweet.getImagem());
		document.put("link", tweet.getLink());
		document.put("mensagem", tweet.getTexto());
		document.put("tipoRede", "twitter");
		

		collection.insert(document);
		FeedsDao feeds = new FeedsDao();
		
		DBCursor cursor = feeds.buscaPorFiltroPorLink(tweet.getLink().toString());
		if(cursor.length() > 0){
			BasicDBObject searchQuery = new BasicDBObject().append("link", tweet.getLink().toString());
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("retweets", tweet.getRetweets());
			updateFields.append("favorites", tweet.getFavorites());
			
			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$set", updateFields);
			collectionFeeds.update(searchQuery, setQuery);
		}
		else
			collectionFeeds.insert(document);
	}
	
	
	public AggregationOutput buscarTodosPortaisSemFiltro(){
        
        // $group operation
        DBObject groupFields = new BasicDBObject( "_id", "$nomeTwitter");
        groupFields.put("sum", new BasicDBObject( "$sum", "$reactions"));
        groupFields.put("tipo", new BasicDBObject("$first", "$tipoRede"));

        DBObject group = new BasicDBObject("$group", groupFields);

        // run aggregation
        AggregationOutput output = collection.aggregate( group );
        
            return output;
    }
	
	public AggregationOutput buscarTodosPortais(String portal,String dataInicial, String dataFinal,String link){
		
		BasicDBObject match = null;
		
		if(link.isEmpty() && portal.isEmpty()){
			match = (BasicDBObject) JSON.parse("{ \"$match\":{\"$and\": ["
				+ "{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}},{\"tipoRede\": \"twitter\"}]}}");
		}
		else if(!link.isEmpty() && portal.isEmpty()){
			match = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
				+ "{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"link\": \""+link+"\"},{\"tipoRede\": \"twitter\"}]}}");
		}
		else if(!portal.isEmpty()){
			match = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
				+ "{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"nomeTwitter\": \""+portal+"\"},{\"tipoRede\": \"twitter\"}]}}");
		}
		
//		if(link.isEmpty() && portal.isEmpty()){
//			match = (BasicDBObject) JSON.parse("{ \"$match\":"
//				+ "{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} }}}");
//		}
//		else if(!link.isEmpty() && portal.isEmpty()){
//			match = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
//				+ "{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"link\": \""+link+"\"}]}}");
//		}
//		else if(!portal.isEmpty()){
//			match = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
//				+ "{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"nomeTwitter\": \""+portal+"\"}]}}");
//		}
		
		// $group operation
		DBObject groupFields = new BasicDBObject( "_id", "$nomeTwitter");
		groupFields.put("sum", new BasicDBObject( "$sum", "$retweets"));

		DBObject group = new BasicDBObject("$group", groupFields);

		
		// run aggregation
		AggregationOutput output = collectionFeeds.aggregate(match, group );
		
			return output;
		
	}
	
	public DBCursor buscaPorFiltro(String portal, String dataInicial, String dataFinal, String link) throws ParseException{
		
		DBObject clausePortal = null;
		DBObject clauseData = null;
		DBObject clauseLink = null;
		
		BasicDBList and = new BasicDBList();
		
		if(!portal.isEmpty()){
			clausePortal = new BasicDBObject("nomeTwitter", portal); 
			and.add(clausePortal);
		}
		
		if(!dataInicial.isEmpty() && !dataFinal.isEmpty()){
			clauseData = (DBObject) JSON.parse("{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
//			clauseData = (DBObject) JSON.parse("{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
			
			and.add(clauseData);
		}
		
		if(!link.isEmpty()){
			clauseLink = new BasicDBObject("link", link); 
			and.add(clauseLink);
		}
				

//		clauseAggregatePortais = (DBObject) JSON.parse(buscarTodosPortais().toString());
//		and.add(clauseAggregatePortais);
		
		
		DBObject query = new BasicDBObject("$and", and);
		
		DBCursor cursor = collection.find(query).sort(new BasicDBObject("dataCriacao",1));
		
		return cursor;
	}
	
public DBCursor buscaPorFiltroFeeds(String portal, String dataInicial, String dataFinal, String link) throws ParseException{
		
		DBObject clausePortal = null;
		DBObject clauseData = null;
		DBObject clauseLink = null;
		BasicDBObject clauseTipoRede = null;
		
		BasicDBList and = new BasicDBList();
		
		if(!portal.isEmpty()){
			clausePortal = new BasicDBObject("nomeTwitter", portal); 
			and.add(clausePortal);
		}
		
		if(!dataInicial.isEmpty() && !dataFinal.isEmpty()){
			clauseData = (DBObject) JSON.parse("{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
//			clauseData = (DBObject) JSON.parse("{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
			
			and.add(clauseData);
		}
		
		if(!link.isEmpty()){
			clauseLink = new BasicDBObject("link", link); 
			and.add(clauseLink);
		}
				
		clauseTipoRede = new BasicDBObject("tipoRede", "twitter"); 
		and.add(clauseTipoRede);

//		clauseAggregatePortais = (DBObject) JSON.parse(buscarTodosPortais().toString());
//		and.add(clauseAggregatePortais);
		
		
		DBObject query = new BasicDBObject("$and", and);
		
		DBCursor cursor = collectionFeeds.find(query).sort(new BasicDBObject("dataCriacao",1));
		
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
	public void excluirRegistros(String nome){
		
		BasicDBObject query = new BasicDBObject();
		query.put("nomeTwitter", nome);
		collection.remove(query);
		
	}

}
