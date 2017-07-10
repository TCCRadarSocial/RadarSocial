package tcc.radarsocial.model;

public class Tweet {

	private long idTweet;
	private String texto;
	private int retweets;
	private int favorites;
	private String imagem;
	private String link;
	public long getIdTweet() {
		return idTweet;
	}
	public void setIdTweet(long idTweet) {
		this.idTweet = idTweet;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getRetweets() {
		return retweets;
	}
	public void setRetweets(int retweets) {
		this.retweets = retweets;
	}
	public int getFavorites() {
		return favorites;
	}
	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
	
}
