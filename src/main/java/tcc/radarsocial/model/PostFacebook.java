package tcc.radarsocial.model;

import java.util.Date;

public class PostFacebook {

	private String idPost;
	private int idPagina;
	private int shares;
	private int comments;
	private int likes;
	private int reactions;
	private int reactionsLove;
	private int reactionsLike;
	private int reactionsHaha;
	private int reactionsWow;
	private int reactionsSad;
	private int reactionsAngry;
	private int reactionsThankful;
	private String imagem;
	private String link;
	private String mensagem;
	private String createdDate;
	
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getIdPost() {
		return idPost;
	}
	public void setIdPost(String idPost) {
		this.idPost = idPost;
	}
	public int getIdPagina() {
		return idPagina;
	}
	public void setIdPagina(int idPagina) {
		this.idPagina = idPagina;
	}
	public int getShares() {
		return shares;
	}
	public void setShares(int shares) {
		this.shares = shares;
	}
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getReactions() {
		return reactions;
	}
	public void setReactions(int reactions) {
		this.reactions = reactions;
	}
	
	
	public int getReactionsLove() {
		return reactionsLove;
	}
	public void setReactionsLove(int reactionsLove) {
		this.reactionsLove = reactionsLove;
	}
	public int getReactionsLike() {
		return reactionsLike;
	}
	public void setReactionsLike(int reactionsLike) {
		this.reactionsLike = reactionsLike;
	}
	public int getReactionsHaha() {
		return reactionsHaha;
	}
	public void setReactionsHaha(int reactionsHaha) {
		this.reactionsHaha = reactionsHaha;
	}
	public int getReactionsWow() {
		return reactionsWow;
	}
	public void setReactionsWow(int reactionsWow) {
		this.reactionsWow = reactionsWow;
	}
	public int getReactionsSad() {
		return reactionsSad;
	}
	public void setReactionsSad(int reactionsSad) {
		this.reactionsSad = reactionsSad;
	}
	public int getReactionsAngry() {
		return reactionsAngry;
	}
	public void setReactionsAngry(int reactionsAngry) {
		this.reactionsAngry = reactionsAngry;
	}
	public int getReactionsThankful() {
		return reactionsThankful;
	}
	public void setReactionsThankful(int reactionsThankful) {
		this.reactionsThankful = reactionsThankful;
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
	public String getMensagem() {
		return mensagem;
	}
	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
	
	
}
