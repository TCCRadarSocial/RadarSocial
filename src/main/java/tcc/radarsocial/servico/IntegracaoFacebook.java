package tcc.radarsocial.servico;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.Page;
import com.restfb.types.Post;
import com.restfb.types.User;

import tcc.radarsocial.dao.FacebookDao;
import tcc.radarsocial.model.Pagina;
import tcc.radarsocial.model.PostFacebook;

public class IntegracaoFacebook extends DefaultFacebookClient{
	
	public IntegracaoFacebook(String appId, String appSecret) {
        AccessToken accessToken = this.obtainAppAccessToken(appId, appSecret);
        this.accessToken = accessToken.getAccessToken();
        System.out.println("token:" +this.accessToken);
    }
	
	
	public void retornaJson(String paginaFacebook) throws ParseException {
//		JsonObject fetchObjectsResults =  this.fetchObjects(Arrays.asList("bbcbrasil/feeds"),JsonObject.class, Parameter.with("fields","likes.summary(true)"));
		JsonObject pagina =  this.fetchObjects(Arrays.asList(paginaFacebook),JsonObject.class, Parameter.with("fields","id,name"));
		
		JsonObject pagina_fan =  this.fetchObjects(Arrays.asList(paginaFacebook),JsonObject.class, Parameter.with("fields","fan_count"));
		
//		int count_likes_page = pagina_fan.getJsonObject(paginaFacebook).getInt("fan_count");
		
		String nomePagina = pagina.getJsonObject(paginaFacebook).getString("name");
		int id = pagina.getJsonObject(paginaFacebook).getInt("id");
		
		Connection<Post> pageFeed = this.fetchConnection(paginaFacebook + "/feed", Post.class);
		
		Pagina pag = new Pagina();
		PostFacebook postFacebook = new PostFacebook();

		for (List<Post> feed : pageFeed){
            for (Post post : feed){     
                 JsonObject fetchObjectsResults =  this.fetchObjects(Arrays.asList(post.getId().toString()),JsonObject.class, Parameter.with("fields","likes.summary(true),comments.summary(true),shares,reactions.summary(true),full_picture,permalink_url,message,created_time,"
                 		+ "reactions.type(LOVE).limit(0).summary(true).as(love),reactions.type(LIKE).limit(0).summary(true).as(like),reactions.type(WOW).limit(0).summary(true).as(wow),reactions.type(HAHA).limit(0).summary(true).as(haha),"
                 		+ "reactions.type(SAD).limit(0).summary(true).as(sad),reactions.type(ANGRY).limit(0).summary(true).as(angry),reactions.type(THANKFUL).limit(0).summary(true).as(thankful)"));
                 
                 
                 int shares = fetchObjectsResults.getJsonObject(post.getId()).has("shares") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("shares").getInt("count") : 0;
                 int comentarios = fetchObjectsResults.getJsonObject(post.getId()).has("comments") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("comments").getJsonObject("summary").getInt("total_count") : 0;
                 String imagem = fetchObjectsResults.getJsonObject(post.getId()).has("full_picture") ? fetchObjectsResults.getJsonObject(post.getId()).getString("full_picture"): "";
                 String link =  fetchObjectsResults.getJsonObject(post.getId()).has("permalink_url") ? fetchObjectsResults.getJsonObject(post.getId()).getString("permalink_url") : "";
                 int reactions = fetchObjectsResults.getJsonObject(post.getId()).has("reactions") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("reactions").getJsonObject("summary").getInt("total_count") : 0;
                 int likes = fetchObjectsResults.getJsonObject(post.getId()).has("likes") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("likes").getJsonObject("summary").getInt("total_count") : 0;
                 String message = fetchObjectsResults.getJsonObject(post.getId()).has("message") ? fetchObjectsResults.getJsonObject(post.getId()).getString("message") : "";
                 String createdTime = fetchObjectsResults.getJsonObject(post.getId()).has("created_time") ? fetchObjectsResults.getJsonObject(post.getId()).getString("created_time"): "";
                 int reactionsLove = fetchObjectsResults.getJsonObject(post.getId()).has("love") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("love").getJsonObject("summary").getInt("total_count") : 0;
                 int reactionsLike = fetchObjectsResults.getJsonObject(post.getId()).has("like") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("like").getJsonObject("summary").getInt("total_count") : 0;
                 int reactionsWow = fetchObjectsResults.getJsonObject(post.getId()).has("wow") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("wow").getJsonObject("summary").getInt("total_count") : 0;
                 int reactionsHaha = fetchObjectsResults.getJsonObject(post.getId()).has("haha") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("haha").getJsonObject("summary").getInt("total_count") : 0;
                 int reactionsSad = fetchObjectsResults.getJsonObject(post.getId()).has("sad") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("sad").getJsonObject("summary").getInt("total_count") : 0;
                 int reactionsAngry = fetchObjectsResults.getJsonObject(post.getId()).has("angry") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("angry").getJsonObject("summary").getInt("total_count") : 0;
                 int reactionsThankful = fetchObjectsResults.getJsonObject(post.getId()).has("thankful") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("thankful").getJsonObject("summary").getInt("total_count") : 0;
                 
                 
                 
                 pag.setIdPagina(id);
                 pag.setNome(nomePagina);
                 pag.setNomePagina(paginaFacebook);
                 postFacebook.setIdPost(post.getId());
                 postFacebook.setLikes(likes);
                 postFacebook.setComments(comentarios);
                 postFacebook.setShares(shares);
                 postFacebook.setReactions(reactions);
                 postFacebook.setReactionsLove(reactionsLove);
                 postFacebook.setReactionsLike(reactionsLike);
                 postFacebook.setReactionsWow(reactionsWow);
                 postFacebook.setReactionsHaha(reactionsHaha);
                 postFacebook.setReactionsSad(reactionsSad);
                 postFacebook.setReactionsAngry(reactionsAngry);
                 postFacebook.setReactionsThankful(reactionsThankful);
                 postFacebook.setMensagem(message);
                 postFacebook.setLink(link);
                 postFacebook.setImagem(imagem);
                 postFacebook.setCreatedDate(createdTime);
                 
                 FacebookDao dao = new FacebookDao();
     			 dao.gravarDadosFacebook(pag,postFacebook);
            }
            
       }
	}
	
	public Boolean hasPage(String paginaFacebook){
		
		Boolean response = false;
		
		try{
			JsonObject pagina =  this.fetchObjects(Arrays.asList(paginaFacebook),JsonObject.class, Parameter.with("fields","id,name"));
			response = true;
		}
		catch(Exception e){
			response = false;
		}
			
		return response;
	}
}
