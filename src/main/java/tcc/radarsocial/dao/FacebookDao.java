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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;	
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;


public class FacebookDao {
		
	ConnectionFactory con = new ConnectionFactory();
	DBCollection collection = ConnectionFactory.connectDB().getCollection("FacebookMetricas");
	
	

	public void gravarDadosFacebook(Pagina pag, PostFacebook post){
		
		System.out.println("BasicDBObject example...");
		BasicDBObject document = new BasicDBObject();
		document.put("idPagina", pag.getIdPagina());
		document.put("nomePagina", pag.getNome());
		document.put("idPost", post.getIdPost());
		document.put("likes", post.getLikes());
		document.put("comments", post.getComments());
		document.put("shares", post.getShares());
		document.put("reactions", post.getReactions());
		document.put("imagem", post.getImagem());
		document.put("link", post.getLink());
		document.put("dataGravacao", new Date());
		
		collection.insert(document);
	}
	public DBCursor buscarTodosDadosFacebook(){
		
		DBCursor cursor = collection.find();
		  
		return cursor;
	}
	
	public AggregationOutput buscarTodosPortais(){
		
		// $group operation
		DBObject groupFields = new BasicDBObject( "_id", "$nomePagina");
		groupFields.put("sum", new BasicDBObject( "$sum", "$reactions"));

		DBObject group = new BasicDBObject("$group", groupFields);

		// run aggregation
		AggregationOutput output = collection.aggregate( group );
		
			return output;
		
	}
	
	public DBCursor buscaPorFiltro(String portal, String dataInicial, String dataFinal) throws ParseException{
						
		DBObject clausePortal = new BasicDBObject("nomePagina", portal); 
		
		DBObject clauseData = null;
		if(!dataInicial.isEmpty() && !dataFinal.isEmpty())
			clauseData = (DBObject) JSON.parse("{ \"dataGravacao\" : { \"$gte\" : { \"$date\" : \""+dataInicial+"\"} , \"$lte\" : { \"$date\" : \""+dataFinal+"\"}}}");
	
		
		BasicDBList and = new BasicDBList();
		and.add(clausePortal);
		
		if(clauseData != null)
			and.add(clauseData);

		DBObject query = new BasicDBObject("$and", and);
		
		DBCursor cursor = collection.find(query);
		
		return cursor;
	}
	
}
