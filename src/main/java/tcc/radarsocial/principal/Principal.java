package tcc.radarsocial.principal;

import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import tcc.radarsocial.dao.FacebookDao;
import tcc.radarsocial.dao.TwitterDao;
import tcc.radarsocial.servico.IntegracaoFacebook;
import tcc.radarsocial.servico.IntegracaoTwitter;
import twitter4j.TwitterException;

public class Principal {

	public static void main(String[] args) throws TwitterException, ParseException {
		
		Thread t = new Thread(new Runnable() {
		    public void run() {
		    			    	
		    	FacebookDao daoFace = new FacebookDao();
		    	AggregationOutput portaisFacebook = daoFace.buscarTodosPortaisSemFiltroNomeUrl();
		    	
		    	JSONArray arrayFace = new JSONArray();
		    	for (DBObject res : portaisFacebook.results()) {
		            System.out.println(res);
		            JSONObject jsonobj = new JSONObject();
		            jsonobj.put("portal", res.get("_id"));
		            arrayFace.put(jsonobj);
		        }
		    	
		    	
		    	TwitterDao daoTwitter = new TwitterDao();
		    	AggregationOutput portaisTwitter = daoTwitter.buscarTodosPortaisSemFiltro();

		    	JSONArray arrayTwitter = new JSONArray();
		    	for (DBObject res : portaisTwitter.results()) {
		            System.out.println(res);
		            JSONObject jsonobj = new JSONObject();
		            jsonobj.put("portal", res.get("_id"));
		            arrayTwitter.put(jsonobj);
		        }
		    	for(int i=0;i<arrayTwitter.length();i++)
	            {
		    		JSONObject json = arrayTwitter.getJSONObject(i);
		    		System.out.println(json.get("portal"));
		    		executeIntegracaoTwitter(json.get("portal").toString());
	            }
		    	for(int i=0;i<arrayFace.length();i++)
	            {
		    		JSONObject json = arrayFace.getJSONObject(i);
		    		System.out.println(json.get("portal"));
		    		executeIntegracaoFacebook(json.get("portal").toString());
	            }
		    	
		    	
		    }
		});
//		try {
////			t.sleep(86400000); //1 dia
//			//t.sleep(600000); //10min
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		t.start();
		
		
		
		
	}
	
	public static void executeIntegracaoFacebook(String facebook){
				
		IntegracaoFacebook login = new IntegracaoFacebook("1818125321786434", "874b37094b726ca18b0bc7684cf4c757");
		try {
			login.retornaJson(facebook);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void executeIntegracaoTwitter(String twitter){
		IntegracaoTwitter intTwitter = new IntegracaoTwitter();
		try {
			intTwitter.buscarDadosTwitter(intTwitter.autenticar(), twitter);
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
