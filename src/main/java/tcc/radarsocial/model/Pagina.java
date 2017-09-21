package tcc.radarsocial.model;

public class Pagina {
	private int idPagina;
	private String nome;
	private int totalLikes;
	private String nomePagina;
	
	
	public int total;
	
	public String getNomePagina() {
		return nomePagina;
	}
	public void setNomePagina(String nomePagina) {
		this.nomePagina = nomePagina;
	}
	public int getIdPagina() {
		return idPagina;
	}
	public void setIdPagina(int idPagina) {
		this.idPagina = idPagina;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public int getTotalLikes() {
		return totalLikes;
	}
	public void setTotalLikes(int totalLikes) {
		this.totalLikes = totalLikes;
	}

}
