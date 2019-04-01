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
	//estados de aceitação
	private List<State_> finalStates;

	/***
	 * Construtor vazio inicializa lista de estados finais
	 */
	public Automaton_() {
		//inicia lista de estados de aceitação
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
	 * Adiciona o estado a lista de estados de aceitação, verifica se o estado já foi adicionado anteriormente
	 * @param state o estado a ser adicionado na lista de estados finais
	 * 
	 */
	public void addFinalStates(State_ state) {
		//verifica se o estado já foi adicionado, com base no nome do estado
		if (!this.finalStates.contains(state)) {
			this.finalStates.add(state);
		}
	}

	
	/***
	 * Retorna os estados alcançados a partir do estado recebido de parametro com o rótulo epsilon
	 * @param estado
	 * @return os estados alcançados com epsilon
	 */
	public List<State_> reachableStatesWithEpsilon(State_ estado) {
		//estados alcançados com epsilon
		List<State_> estadosAlcancados = new ArrayList<State_>();
		//lista auxiliar para verificar condição de parada
		List<State_> aux = new ArrayList<State_>();
		//adiciona o estado recebido de parametro a lista para ser o primeiro a ser visitado
		aux.add(estado);
		//resultado de retorno da função "transição existe"
		Result r;
		//estado atual que se esta percorrendo
		State_ atual;
		
		//enquanto houver estados a serem explorados na lista
		while (aux.size() > 0) {
			//o atual recebe o primeiro elemento da lista
			atual = aux.remove(0);
			//se a transição partindo do estado atual com rótulo epsilon existe
			r = transitionExists(atual.getNome(), Constants.EPSILON);

			//encontrou transição com epsilon
			if (r.getFound()) {
				//estadosFim são os estados alcançados com a transição epsilon partindo do estado atual
				//é adicionado o estado atual na lista de estados alcançados por epsilon, pois com epsilon pode se permanecer no mesmo estado
				r.getReachedStates().add(atual);
			} else {
				//se nenhum estado é alcançado por epsilon
				//adiciona o estado atual como sendo alcançado por epsilon
				r.setReachedStates(new ArrayList<State_>(Arrays.asList(atual)));
			}
			//na lista contem apenas os estados que não consta em "estadosAlcancados"
			ArrayList<State_> diferenca = new ArrayList<State_>(r.getReachedStates());
			diferenca.removeAll(estadosAlcancados);
			
			//adicionar na lista auxiliar os estados alcançados pelo estado atual com epsilon
			aux = Stream.concat(diferenca.stream(), aux.stream()).distinct().collect(Collectors.toList());

			//adicionar na lista estadosAlcancados os estados alcançados pelo estado atual com epsilon 
			estadosAlcancados = Stream.concat(diferenca.stream(), estadosAlcancados.stream()).distinct()
					.collect(Collectors.toList());
		}

		return estadosAlcancados;
	}

	/***
	 * Retorna os estados que tem transição epsilon partindo dele
	 * @return lista de estados com transição epsilon
	 */
	public List<State_> getStatesWithEpsilonTransition() {
		//inicializa lista que irá retornar
		List<State_> estadosComTransicaoEpsilon = new ArrayList<State_>();
		//percorre as transições do automato
		for (Transition_ transicao : getTransitions()) {
			//se a transição tiver rótulo epsilon
			if(transicao.getRotulo().equals(Constants.EPSILON)) {
				//adiciona a transição na lista de transição
				estadosComTransicaoEpsilon.add(transicao.getEstadoIni());
			}
		}
		
		return estadosComTransicaoEpsilon;
	}

	/***
	 * Verifica se o automato é deterministico com base em suas transições
	 * 
	 * @return se o automato é deterministico ou não
	 */
	public boolean isDeterministic() {
		// contador de transições repetidas
		int cont = 0;

		// verifica se no alfabeto contem epsilon
		if (alphabet.contains(Constants.EPSILON)) {
			// se o alfabeto contem epsilon então o automato não é deterministico
			return false;
		} else {
			// percorre todas as transições
			for (Transition_ transicaoAtual : transitions) {
				// para cada transição o contador de transição começa com 0
				cont = 0;
				// percorre todas as transições
				for (Transition_ t : transitions) {
					// verifica se a transição atual é igual a alguma transição do LTS, com base no
					// estado inicial e o rótulo de ambas as transições
					if (t.getEstadoIni().getNome().equals(transicaoAtual.getEstadoIni().getNome())
							&& t.getRotulo().equals(transicaoAtual.getRotulo())) {
						cont++;
					}

					// se há mais de uma(ela mesma e mais alguma) transição igual a transição atual
					if (cont > 1) {
						// não é deterministico
						return false;
					}
				}

			}
			// se nenhuma transição repetida foi encontrada então é determinista
			return true;
		}

	}
	
	/***
	 * Sobreescrita do método toString do automato para listar os atributos que consta no LTS e os estados finais do automato
	 * @return retorna a string que descreve o automato
	 */
	@Override
	public String toString() {
		//toString da classe LTS
		String s = super.toString();
		//descrição referente aos estados finais do automato
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
