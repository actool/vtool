/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import util.Constants;

/**
 *Classe Automaton_
 * @author Camila
 */
public class Automaton_ extends LTS {
	//estados de aceita��o
	private List<State_> finalStates;

	/***
	 * Construtor vazio inicializa lista de estados finais
	 */
	public Automaton_() {
		//inicia lista de estados de aceita��o
		finalStates = new ArrayList<State_>();
	}

	/**
	 * Retorna os estados finais do automato
	 * @return estadosFinais
	 */
	public List<State_> getFinalStates() {
		return finalStates;
	}

	/**Altera os estados finais do automato
	 * @param finalStates o conjunto de estados finais
	 *            
	 */
	public void setFinalStates(List<State_> finalStates) {
		this.finalStates = finalStates;
	}

	/***
	 * Adiciona o estado a lista de estados de aceita��o, verifica se o estado j� foi adicionado anteriormente
	 * @param state o estado a ser adicionado na lista de estados finais
	 * 
	 */
	public void addFinalStates(State_ state) {
		//verifica se o estado j� foi adicionado, com base no nome do estado
		if (!this.finalStates.contains(state)) {
			this.finalStates.add(state);
		}
	}

	
	/***
	 * Retorna os estados alcan�ados a partir do estado recebido de parametro com o r�tulo epsilon
	 * @param estado
	 * @return os estados alcan�ados com epsilon
	 */
	public List<State_> reachableStatesWithEpsilon(State_ estado) {
		//estados alcan�ados com epsilon
		List<State_> estadosAlcancados = new ArrayList<State_>();
		//lista auxiliar para verificar condi��o de parada
		List<State_> aux = new ArrayList<State_>();
		//adiciona o estado recebido de parametro a lista para ser o primeiro a ser visitado
		aux.add(estado);
		//resultado de retorno da fun��o "transi��o existe"
		Result r;
		//estado atual que se esta percorrendo
		State_ atual;
		
		//enquanto houver estados a serem explorados na lista
		while (aux.size() > 0) {
			//o atual recebe o primeiro elemento da lista
			atual = aux.remove(0);
			//se a transi��o partindo do estado atual com r�tulo epsilon existe
			r = transitionExists(atual.getNome(), Constants.EPSILON);

			//encontrou transi��o com epsilon
			if (r.getFound()) {
				//estadosFim s�o os estados alcan�ados com a transi��o epsilon partindo do estado atual
				//� adicionado o estado atual na lista de estados alcan�ados por epsilon, pois com epsilon pode se permanecer no mesmo estado
				r.getReachedStates().add(atual);
			} else {
				//se nenhum estado � alcan�ado por epsilon
				//adiciona o estado atual como sendo alcan�ado por epsilon
				r.setReachedStates(new ArrayList<State_>(Arrays.asList(atual)));
			}
			//na lista contem apenas os estados que n�o consta em "estadosAlcancados"
			ArrayList<State_> diferenca = new ArrayList<State_>(r.getReachedStates());
			diferenca.removeAll(estadosAlcancados);
			
			//adicionar na lista auxiliar os estados alcan�ados pelo estado atual com epsilon
			aux = Stream.concat(diferenca.stream(), aux.stream()).distinct().collect(Collectors.toList());

			//adicionar na lista estadosAlcancados os estados alcan�ados pelo estado atual com epsilon 
			estadosAlcancados = Stream.concat(diferenca.stream(), estadosAlcancados.stream()).distinct()
					.collect(Collectors.toList());
		}

		return estadosAlcancados;
	}

	/***
	 * Retorna os estados que tem transi��o epsilon partindo dele
	 * @return lista de estados com transi��o epsilon
	 */
	public List<State_> getStatesWithEpsilonTransition() {
		//inicializa lista que ir� retornar
		List<State_> estadosComTransicaoEpsilon = new ArrayList<State_>();
		//percorre as transi��es do automato
		for (Transition_ transicao : getTransitions()) {
			//se a transi��o tiver r�tulo epsilon
			if(transicao.getRotulo().equals(Constants.EPSILON)) {
				//adiciona a transi��o na lista de transi��o
				estadosComTransicaoEpsilon.add(transicao.getEstadoIni());
			}
		}
		
		return estadosComTransicaoEpsilon;
	}

	/***
	 * Verifica se o automato � deterministico com base em suas transi��es
	 * 
	 * @return se o automato � deterministico ou n�o
	 */
	public boolean isDeterministic() {
		// contador de transi��es repetidas
		int cont = 0;

		// verifica se no alfabeto contem epsilon
		if (alphabet.contains(Constants.EPSILON)) {
			// se o alfabeto contem epsilon ent�o o automato n�o � deterministico
			return false;
		} else {
			// percorre todas as transi��es
			for (Transition_ transicaoAtual : transitions) {
				// para cada transi��o o contador de transi��o come�a com 0
				cont = 0;
				// percorre todas as transi��es
				for (Transition_ t : transitions) {
					// verifica se a transi��o atual � igual a alguma transi��o do LTS, com base no
					// estado inicial e o r�tulo de ambas as transi��es
					if (t.getEstadoIni().getNome().equals(transicaoAtual.getEstadoIni().getNome())
							&& t.getRotulo().equals(transicaoAtual.getRotulo())) {
						cont++;
					}

					// se h� mais de uma(ela mesma e mais alguma) transi��o igual a transi��o atual
					if (cont > 1) {
						// n�o � deterministico
						return false;
					}
				}

			}
			// se nenhuma transi��o repetida foi encontrada ent�o � determinista
			return true;
		}

	}
	
	/***
	 * Sobreescrita do m�todo toString do automato para listar os atributos que consta no LTS e os estados finais do automato
	 * @return retorna a string que descreve o automato
	 */
	@Override
	public String toString() {
		//toString da classe LTS
		String s = super.toString();
		//descri��o referente aos estados finais do automato
		s += ("##############################\n");
		s += ("           Estados Finais \n");
		s += ("##############################\n");
		s += ("Quantidade: " + this.finalStates.size() + "\n");
		for (State_ e : this.finalStates) {
			s += ("[" + e.getNome() + "] - ");
		}
		return s;
	}

}
