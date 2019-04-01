/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Classe transi��o
 * @author Camila
 */
public class Transition_ {
	//r�tulo da transi��o
	private String rotulo;
	//tipo de transi��o se � entrada/sa�da IOLTS
	private TipoTransicao tipo;
	//estado que inicia a transi��o
	private State_ estadoIni;
	//estado final da transi��o
	private State_ estadoFim;
	//id da transi��o, utilizado para as bibliotecas
	private String id;

	/***
	 * Contrutor vazio
	 */
	public Transition_() {
	}

	/***
	 * Contrutor com todos os parametros
	 * @param id
	 * @param estadoIni
	 * @param rotulo
	 * @param estadoFim
	 */
	public Transition_(String id, State_ estadoIni, String rotulo, State_ estadoFim) {
		this.id = id;
		this.estadoIni = estadoIni;
		this.rotulo = rotulo;
		this.estadoFim = estadoFim;
	}

	/***
	 * Contrutor com estado inicial, r�tulo, estadoo final
	 * @param estadoIni
	 * @param rotulo
	 * @param estadoFim
	 */
	public Transition_(State_ estadoIni, String rotulo, State_ estadoFim) {
		this.estadoIni = estadoIni;
		this.rotulo = rotulo;
		this.estadoFim = estadoFim;
	}

	/**
	 * Retorna o r�tulo da transi��o
	 * @return rotulo
	 */
	public String getRotulo() {
		return rotulo;
	}

	/**
	 * Altera o r�tulo da transi��o
	 * @param rotulo
	 *           
	 */
	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	/**
	 * Retorna o tipo da transi��o
	 * @return tipo
	 */
	public TipoTransicao getTipo() {
		return tipo;
	}

	/**
	 * Altera o tipo de transi��o
	 * @param tipo
	 *            
	 */
	public void setTipo(TipoTransicao tipo) {
		this.tipo = tipo;
	}

	/**Retorna o estado inicial da transi��o
	 * @return estadoIni
	 */
	public State_ getEstadoIni() {
		return estadoIni;
	}

	/**
	 * Altera o estado inicial da transi��o
	 * @param estadoIni
	 *            
	 */
	public void setEstadoIni(State_ estadoIni) {
		this.estadoIni = estadoIni;
	}

	/**
	 * Retorna o estado final da transi��o
	 * @return estadoFim
	 */
	public State_ getEstadoFim() {
		return estadoFim;
	}

	/**
	 * Altera o estado final da transi��o
	 * @param estadoFim
	 * 
	 */
	public void setEstadoFim(State_ estadoFim) {
		this.estadoFim = estadoFim;
	}

	/***
	 * Retorna o id da transi��o
	 * @return id
	 */
	private String getId() {
		return id;
	}

	/***
	 * Altera o id da transi��o
	 * @param id
	 */
	private void setId(String id) {
		this.id = id;
	}

	/***
	 * Sobreescrita do m�todo equals
	 * @return se as transi��es s�o iguais
	 */
	@Override
	public boolean equals(Object obj) {

		boolean result;
		//se a transi��o � nula, ou de classe diferente
		if (obj == null || obj.getClass() != getClass()) {
			//transi��o n�o pe igual
			result = false;
		} else {
			Transition_ t = (Transition_) obj;
			//retorna se o estado inicial, o r�tulo e o estado final s�o iguais
			result = t.estadoIni.equals(estadoIni) && t.estadoFim.equals(estadoFim) && rotulo.equals(t.rotulo);
		}
		return result;
	}

	/***
	 * Enum tipo de transi��o se � ENTRADA ou SAIDA
	 * @author camil
	 *
	 */
	public enum TipoTransicao {
		ENTRADA, SAIDA
	};
	
	/***
	 * Sobreescrita do m�todo toString
	 */
	@Override
	public String toString() {
		return "[" + estadoIni + " - " + rotulo + " - " + estadoFim + "] \n";
	}
}
