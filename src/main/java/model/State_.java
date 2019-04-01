/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Classe estado
 * @author Camila
 */
public class State_ {
	//nome do estado
	private String name;
	//id utilizado para integra��o com as bibliotecas
	private int id;
	//descri��o utilizada na interse��o e pra pegar as palavras
	private String info;
	
	private boolean visited;

	/***
	 * Construtor vazio inicializa o nome do estado sem nome
	 */
	public State_() {
		this.name = "";
	}

	/***
	 * Construtor recebe um estado e gera uma copia dele
	 * @param estado
	 */
	public State_(State_ estado) {
		this.id = estado.id;
		this.name = estado.name;
		this.info = estado.info;
	}
	
	/***
	 * Construtor recebe o nome do estado
	 * @param nome
	 */
	public State_(String nome) {
		this.name = nome;
	}
	
	/***
	 * Construtor recebe o nome e o id do estado, utilizado para integra��o com as bibliotecas
	 * @param nome do estado
	 * @param id do estado
	 */
	public State_(String nome, int id) {
		this.name = nome;
		this.setId(id);	
	}

	/**
	 * Retorna o nome do estao
	 * @return o nome
	 */
	public String getNome() {
		return name;
	}

	/**
	 * Altera o nome do estado
	 * @param nome o nome do estado
	 *            
	 */
	public void setNome(String nome) {
		this.name = nome;
	}

	/***
	 * Retorna o id do estado
	 * @return id 
	 */
	public int getId() {
		return id;
	}

	/***
	 * Altera o id do estado
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/***
	 * Sobreescreve o equals do estado, a igualdade entre estados � considerando apenas o nome
	 */
	@Override
	public boolean equals(Object obj) {
		boolean result;
		if (obj == null || obj.getClass() != getClass()) {
			result = false;
		} else {
			State_ _estado = (State_) obj;
			result = this.name.equals(_estado.name);
		}
		return result;
	}

	/***
	 * Retorna a descri��o da interse��o, utilizado na fun��o de interse��o
	 * @return descIntersecao descri��o da interse��o
	 */
	public String getInfo() {
		return info;
	}

	/***
	 * Altera a descri��o da interse��o, utilizado na fun��o de interse��o
	 * @param descProdSinc descri��o da interse��o
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/***
	 * Sobreescrita do m�todo to string
	 * @return nome O nome do estado
	 */
	@Override
	public String toString() {		
		return name;
	}
	
	/***
	 * Sobreescreve o hash do estado  com base no nome e no id
	 */
	@Override
	public int hashCode() {
	    final int primo = 7;
	    int result = 56;
	    result = primo * result + ((name == null) ? 0 : name.hashCode());
	    result = primo * result + id;
	    return result;
	}

	public boolean isVisitado() {
		return visited;
	}

	public void setVisitado(boolean visitado) {
		this.visited = visitado;
	}

}
