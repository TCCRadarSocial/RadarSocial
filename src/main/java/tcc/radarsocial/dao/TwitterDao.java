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
	
	public void gravarDadosTwitter(Twitter twitter, Tweet tweet){
		
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
		document.put("texto", tweet.getTexto());

		collection.insert(document);
		collectionFeeds.insert(document);
	}
	
	
	public AggregationOutput buscarTodosPortaisSemFiltro(){
        
        // $group operation
        DBObject groupFields = new BasicDBObject( "_id", "$nomeTwitter");
        groupFields.put("sum", new BasicDBObject( "$sum", "$reactions"));

        DBObject group = new BasicDBObject("$group", groupFields);

        // run aggregation
        AggregationOutput output = collection.aggregate( group );
        
            return output;
    }
	
	public AggregationOutput buscarTodosPortais(String portal,String dataInicial, String dataFinal,String link){
		
		BasicDBObject match = null;
		
		if(link.isEmpty() && portal.isEmpty()){
			match = (BasicDBObject) JSON.parse("{ \"$match\":"
				+ "{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} }}}");
		}
		else if(!link.isEmpty() && portal.isEmpty()){
			match = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
				+ "{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"link\": \""+link+"\"}]}}");
		}
		else if(!portal.isEmpty()){
			match = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
				+ "{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"nomeTwitter\": \""+portal+"\"}]}}");
		}
		
		// $group operation
		DBObject groupFields = new BasicDBObject( "_id", "$nomeTwitter");
		groupFields.put("sum", new BasicDBObject( "$sum", "$retweets"));

		DBObject group = new BasicDBObject("$group", groupFields);

		
		// run aggregation
		AggregationOutput output = collection.aggregate(match, group );
		
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
			clauseData = (DBObject) JSON.parse("{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
			and.add(clauseData);
		}
		
		if(!link.isEmpty()){
			clauseLink = new BasicDBObject("link", link); 
			and.add(clauseLink);
		}
				

//		clauseAggregatePortais = (DBObject) JSON.parse(buscarTodosPortais().toString());
//		and.add(clauseAggregatePortais);
		
		
		DBObject query = new BasicDBObject("$and", and);
		
		DBCursor cursor = collection.find(query);
		
		return cursor;
	}

}
