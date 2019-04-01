/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Classe transição
 * @author Camila
 */
public class Transition_ {
	//rótulo da transição
	private String rotulo;
	//tipo de transição se é entrada/saída IOLTS
	private TipoTransicao tipo;
	//estado que inicia a transição
	private State_ estadoIni;
	//estado final da transição
	private State_ estadoFim;
	//id da transição, utilizado para as bibliotecas
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
	 * Contrutor com estado inicial, rótulo, estadoo final
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
	 * Retorna o rótulo da transição
	 * @return rotulo
	 */
	public String getRotulo() {
		return rotulo;
	}

	/**
	 * Altera o rótulo da transição
	 * @param rotulo
	 *           
	 */
	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}

	/**
	 * Retorna o tipo da transição
	 * @return tipo
	 */
	public TipoTransicao getTipo() {
		return tipo;
	}

	/**
	 * Altera o tipo de transição
	 * @param tipo
	 *            
	 */
	public void setTipo(TipoTransicao tipo) {
		this.tipo = tipo;
	}

	/**Retorna o estado inicial da transição
	 * @return estadoIni
	 */
	public State_ getEstadoIni() {
		return estadoIni;
	}

	/**
	 * Altera o estado inicial da transição
	 * @param estadoIni
	 *            
	 */
	public void setEstadoIni(State_ estadoIni) {
		this.estadoIni = estadoIni;
	}

	/**
	 * Retorna o estado final da transição
	 * @return estadoFim
	 */
	public State_ getEstadoFim() {
		return estadoFim;
	}

	/**
	 * Altera o estado final da transição
	 * @param estadoFim
	 * 
	 */
	public void setEstadoFim(State_ estadoFim) {
		this.estadoFim = estadoFim;
	}

	/***
	 * Retorna o id da transição
	 * @return id
	 */
	private String getId() {
		return id;
	}

	/***
	 * Altera o id da transição
	 * @param id
	 */
	private void setId(String id) {
		this.id = id;
	}

	/***
	 * Sobreescrita do método equals
	 * @return se as transições são iguais
	 */
	@Override
	public boolean equals(Object obj) {

		boolean result;
		//se a transição é nula, ou de classe diferente
		if (obj == null || obj.getClass() != getClass()) {
			//transição não pe igual
			result = false;
		} else {
			Transition_ t = (Transition_) obj;
			//retorna se o estado inicial, o rótulo e o estado final são iguais
			result = t.estadoIni.equals(estadoIni) && t.estadoFim.equals(estadoFim) && rotulo.equals(t.rotulo);
		}
		return result;
	}

	/***
	 * Enum tipo de transição se é ENTRADA ou SAIDA
	 * @author camil
	 *
	 */
	public enum TipoTransicao {
		ENTRADA, SAIDA
	};
	
	/***
	 * Sobreescrita do método toString
	 */
	@Override
	public String toString() {
		return "[" + estadoIni + " - " + rotulo + " - " + estadoFim + "] \n";
	}
}
