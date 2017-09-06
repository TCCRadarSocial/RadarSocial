package tcc.radarsocial.servico;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.Post;

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
		
		String nomePagina = pagina.getJsonObject(paginaFacebook).getString("name");
		int id = pagina.getJsonObject(paginaFacebook).getInt("id");
		
		Connection<Post> pageFeed = this.fetchConnection(paginaFacebook + "/feed", Post.class);
		
		Pagina pag = new Pagina();
		PostFacebook postFacebook = new PostFacebook();

		for (List<Post> feed : pageFeed){
            for (Post post : feed){     
                 JsonObject fetchObjectsResults =  this.fetchObjects(Arrays.asList(post.getId().toString()),JsonObject.class, Parameter.with("fields","likes.summary(true),comments.summary(true),shares,reactions.summary(true),full_picture,permalink_url,message,created_time"));
                 
                 int shares = fetchObjectsResults.getJsonObject(post.getId()).has("shares") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("shares").getInt("count") : 0;
                 int comentarios = fetchObjectsResults.getJsonObject(post.getId()).has("comments") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("comments").getJsonObject("summary").getInt("total_count") : 0;
                 String imagem = fetchObjectsResults.getJsonObject(post.getId()).has("full_picture") ? fetchObjectsResults.getJsonObject(post.getId()).getString("full_picture"): "";
                 String link =  fetchObjectsResults.getJsonObject(post.getId()).has("permalink_url") ? fetchObjectsResults.getJsonObject(post.getId()).getString("permalink_url") : "";
                 int reactions = fetchObjectsResults.getJsonObject(post.getId()).has("reactions") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("reactions").getJsonObject("summary").getInt("total_count") : 0;
                 int likes = fetchObjectsResults.getJsonObject(post.getId()).has("likes") ? fetchObjectsResults.getJsonObject(post.getId()).getJsonObject("likes").getJsonObject("summary").getInt("total_count") : 0;
                 String message = fetchObjectsResults.getJsonObject(post.getId()).has("message") ? fetchObjectsResults.getJsonObject(post.getId()).getString("message") : "";
                 String createdTime = fetchObjectsResults.getJsonObject(post.getId()).has("created_time") ? fetchObjectsResults.getJsonObject(post.getId()).getString("created_time"): "";
                 
                 pag.setIdPagina(id);
                 pag.setNome(nomePagina);
                 
                 postFacebook.setIdPost(post.getId());
                 postFacebook.setLikes(likes);
                 postFacebook.setComments(comentarios);
                 postFacebook.setShares(shares);
                 postFacebook.setReactions(reactions);
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
