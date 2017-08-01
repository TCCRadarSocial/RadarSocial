package tcc.radarsocial.principal;

import java.text.ParseException;

import tcc.radarsocial.dao.TwitterDao;
import tcc.radarsocial.servico.IntegracaoFacebook;
import tcc.radarsocial.servico.IntegracaoTwitter;
import twitter4j.TwitterException;

public class Principal {

	public static void main(String[] args) throws TwitterException, ParseException {
		
		IntegracaoTwitter intTwitter = new IntegracaoTwitter();
		intTwitter.buscarDadosTwitter(intTwitter.autenticar(), "google");
		
		IntegracaoFacebook login = new IntegracaoFacebook("1818125321786434", "874b37094b726ca18b0bc7684cf4c757");
		login.retornaJson("bbcbrasil");
		
		
		
	}

}
