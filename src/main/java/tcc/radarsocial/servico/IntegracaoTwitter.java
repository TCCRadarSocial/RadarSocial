package tcc.radarsocial.servico;

import java.util.List;

import tcc.radarsocial.dao.TwitterDao;
import tcc.radarsocial.model.Tweet;
import tcc.radarsocial.model.Twitter;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class IntegracaoTwitter {

	public ConfigurationBuilder autenticar(){
		
		ConfigurationBuilder cb = new ConfigurationBuilder();
		
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey("Uan1HJyxZVIApBl5znaB8rdU9")
		.setOAuthConsumerSecret("C7zbMEqNl57hTtZ6MgO8mxHwQJqj6YnzrTHgvLsN9ouD4qxzSh")
		.setOAuthAccessToken("844697526565568512-J3Y7jU7WiAQ9BVuMKT8NqaAKGAcpgdB")
		.setOAuthAccessTokenSecret("po5UpLHtopq89WHN8Nb3pzEWqHXK5615yzpbF4NirZw3e");
		
		return cb;
	}
	
	public void buscarDadosTwitter(ConfigurationBuilder cb,String twitterProfile) throws TwitterException{
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		
		twitter4j.Twitter twitter = tf.getInstance();
		
		Paging paging = new Paging(1, 100);

		List<Status> status = twitter.getUserTimeline(twitterProfile,paging);
		
		Twitter twitterObj = new Twitter();
		Tweet tweet = new Tweet();
		
		for(Status st : status){
			
			twitterObj.setIdTwitter(st.getUser().getId());
			twitterObj.setNome(st.getUser().getName());
			
			tweet.setIdTweet(st.getId());
			tweet.setTexto(st.getText());
			tweet.setRetweets(st.getRetweetCount());
			tweet.setFavorites(st.getFavoriteCount());
			
			if(st.getMediaEntities().length!=0)
				tweet.setImagem(st.getMediaEntities()[0].getMediaURL());
			
			tweet.setLink("https://twitter.com/"+twitterProfile+"/status/"+st.getId());	
			
			TwitterDao dao = new TwitterDao();
			dao.gravarDadosTwitter(twitterObj,tweet);
		}
		
	}
}