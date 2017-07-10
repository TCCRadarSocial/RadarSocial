package tcc.radarsocial.dao;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

import tcc.radarsocial.db.ConnectionFactory;
import tcc.radarsocial.model.Tweet;
import tcc.radarsocial.model.Twitter;

public class TwitterDao {

public void gravarDadosTwitter(Twitter twitter, Tweet tweet){
		
		DBCollection collection = ConnectionFactory.connectDB().getCollection("TwitterMetricas");

		BasicDBObject document = new BasicDBObject();
		document.put("idTwitter", twitter.getIdTwitter());
		document.put("nomeTwitter", twitter.getNome());
		document.put("idTweet", tweet.getIdTweet());
		document.put("dataGravacao", new Date());
		document.put("retweets", tweet.getRetweets());
		document.put("favorites", tweet.getFavorites());
		document.put("imagem", tweet.getImagem());
		document.put("link", tweet.getLink());
		document.put("texto", tweet.getTexto());

		collection.insert(document);
	}
}
