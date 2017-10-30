package tcc.radarsocial.dao;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import fr.turri.jiso8601.*;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;

import tcc.radarsocial.db.ConnectionFactory;
import tcc.radarsocial.model.Pagina;
import tcc.radarsocial.model.PostFacebook;

public class FacebookDao {
		
	ConnectionFactory con = new ConnectionFactory();
	DBCollection collection = ConnectionFactory.connectDB().getCollection("FacebookMetricas");
	DBCollection collectionFeeds = ConnectionFactory.connectDB().getCollection("FeedsMetricas");
	

	public void gravarDadosFacebook(Pagina pag, PostFacebook post) throws ParseException{
		
		System.out.println("Gravando Facebook...");
		BasicDBObject document = new BasicDBObject();
		document.put("idPagina", pag.getIdPagina());
		document.put("nomePagina", pag.getNome());
		document.put("nomePaginaUrl", pag.getNomePagina());
		document.put("idPost", post.getIdPost());
		document.put("likes", post.getLikes());
		document.put("comments", post.getComments());
		document.put("shares", post.getShares());
		document.put("reactions", post.getReactions());
		document.put("reactionLove", post.getReactionsLove());
		document.put("reactionLike", post.getReactionsLike());
		document.put("reactionHaha", post.getReactionsHaha());
		document.put("reactionWow", post.getReactionsWow());
		document.put("reactionSad", post.getReactionsSad());
		document.put("reactionAngry", post.getReactionsAngry());
		document.put("reactionThankful", post.getReactionsThankful());
		document.put("imagem", post.getImagem());
		document.put("link", post.getLink());
		document.put("mensagem", post.getMensagem());
		document.put("dataGravacao", new Date());
		document.put("tipoRede", "facebook");
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ssZ");
		Date date = formatter.parse(post.getCreatedDate());
        date = new Date(date.getTime() - 180 * 60 * 1000);
         
		document.put("dataCriacao", date);
		
		collection.insert(document);
		
		FeedsDao feeds = new FeedsDao();
		
		DBCursor cursor = feeds.buscaPorFiltroPorLink(post.getLink().toString());
System.out.println(cursor.length());
		if(cursor.length() > 0){
			BasicDBObject searchQuery = new BasicDBObject().append("link", post.getLink().toString());
			BasicDBObject updateFields = new BasicDBObject();
			updateFields.append("likes", post.getLikes());
			updateFields.append("comments", post.getComments());
			updateFields.append("shares", post.getShares());
			updateFields.append("reactions", post.getReactions());
			updateFields.append("reactionLove", post.getReactionsLove());
			updateFields.append("reactionLike", post.getReactionsLike());
			updateFields.append("reactionHaha", post.getReactionsHaha());
			updateFields.append("reactionWow", post.getReactionsWow());
			updateFields.append("reactionSad", post.getReactionsSad());
			updateFields.append("reactionAngry", post.getReactionsAngry());
			updateFields.append("reactionThankful", post.getReactionsThankful());
			
			BasicDBObject setQuery = new BasicDBObject();
			setQuery.append("$set", updateFields);
			collectionFeeds.update(searchQuery, setQuery);
		}
		else
			collectionFeeds.insert(document);
	}
	public DBCursor buscarTodosDadosFacebook(){
		
		DBCursor cursor = collection.find();
		  
		return cursor;
	}
	
    public AggregationOutput buscarTodosPortaisSemFiltro(){
        
        // $group operation
        BasicDBObject groupFields = new BasicDBObject( "_id", "$nomePagina");
        groupFields.put("sum", new BasicDBObject( "$sum", "$reactions"));
        groupFields.put("tipo", new BasicDBObject("$first", "$tipoRede"));

        BasicDBObject group = new BasicDBObject("$group", groupFields);

        // run aggregation
        AggregationOutput output = collection.aggregate( group );
        
            return output;
    }
    
    public AggregationOutput buscarTodosPortaisSemFiltroNomeUrl(){
        
        // $group operation
        BasicDBObject groupFields = new BasicDBObject( "_id", "$nomePaginaUrl");
        groupFields.put("sum", new BasicDBObject( "$sum", "$reactions"));
        groupFields.put("tipo", new BasicDBObject("$first", "$tipoRede"));

        BasicDBObject group = new BasicDBObject("$group", groupFields);

        // run aggregation
        AggregationOutput output = collection.aggregate( group );
        
            return output;
    }

	
	
	public AggregationOutput buscarTodosPortais(String portal,String dataInicial, String dataFinal,String link){
		
		BasicDBObject match = null;
		
		if(link.isEmpty() && portal.isEmpty()){
			match = (BasicDBObject) JSON.parse("{ \"$match\":"
				+ "{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} }}}");
		}
		else if(!link.isEmpty() && portal.isEmpty()){
			match = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
				+ "{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"link\": \""+link+"\"}]}}");
		}
		else if(!portal.isEmpty()){
			match = (BasicDBObject) JSON.parse("{ \"$match\": {\"$and\": ["
				+ "{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"nomePagina\": \""+portal+"\"}]}}");
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
//				+ "{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}} },{\"nomePagina\": \""+portal+"\"}]}}");
//		}
		
		
		// $group operation
		BasicDBObject groupFields = new BasicDBObject( "_id", "$nomePagina");
		groupFields.put("sum", new BasicDBObject( "$sum", "$reactions"));

		BasicDBObject group = new BasicDBObject("$group", groupFields);

		
		// run aggregation
		AggregationOutput output = collection.aggregate(match, group );
		
			return output;
		
	}
	
	public DBCursor buscaPorFiltro(String portal, String dataInicial, String dataFinal, String link) throws ParseException{
		
		BasicDBObject clausePortal = null;
		BasicDBObject clauseData = null;
		BasicDBObject clauseLink = null;
		
		BasicDBList and = new BasicDBList();
		
		if(!portal.isEmpty()){
			clausePortal = new BasicDBObject("nomePagina", portal); 
			and.add(clausePortal);
		}
		
		if(!dataInicial.isEmpty() && !dataFinal.isEmpty()){
			clauseData = (BasicDBObject) JSON.parse("{ \"dataCriacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
//			clauseData = (BasicDBObject) JSON.parse("{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
			
			and.add(clauseData);
		}
		
		if(!link.isEmpty()){
			clauseLink = new BasicDBObject("link", link); 
			and.add(clauseLink);
		}
				

//		clauseAggregatePortais = (DBObject) JSON.parse(buscarTodosPortais().toString());
//		and.add(clauseAggregatePortais);
		
		
		BasicDBObject query = new BasicDBObject("$and", and);
		
		DBCursor cursor = collection.find(query).sort(new BasicDBObject("dataCriacao",1));
		
		return cursor;
	}
	
	public void excluirRegistros(String nome){
		
		BasicDBObject query = new BasicDBObject();
		query.put("nomePagina", nome);
		collection.remove(query);
		
	}
	
}
