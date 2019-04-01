/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ListUtils;

/**
 * Classe IOLTS extende da classe LTS
 * @author Camila
 */
public class IOLTS extends LTS{
    private List<String> inputs;
    private List<String> outputs;
    
    /***
     * Construtor vazio
     */
    public IOLTS() {}
    
    /***
     * Construtor recebe um LTS e altera os atributos do IOLTS
     * @param lts
     */
    public IOLTS(LTS lts) {
    	//chama os métodos da super classe para inicializar os parametros
    	this.setInitialState(lts.getInitialState());
    	this.setStates(lts.getStates());
    	this.setTransitions(lts.getTransitions());
    	this.setAlphabet(lts.getAlphabet());
    }
    
    /**
     * Retorna os rótulos de entradas
     * @return os rotulos de Entrada
     */
    public List<String> getInputs() {
        return inputs;
    }

    /** Altera os rótulos de entrada
     * @param rotulosEntrada o conjunto de rótulos de entrada
     */
    public void setInputs(List<String> rotulosEntrada) {
        this.inputs = rotulosEntrada;
    }

    /**
     * Retorna o conjunto de rótulos de saída
     * @return rotulosSaida o conjunto de rótulos de saída
     */
    public List<String> getOutputs() {
        return outputs;
    }

    /** Altera o conjunto de rótulos de saída
     * @param rotulosSaida  o conjunto de rótulos de saída
     */
    public void setOutputs(List<String> rotulosSaida) {
        this.outputs = rotulosSaida;
    }
    

    /***
     * Constroi um automato apartir do IOLTS
     * @return o automato subjacente ao IOLTS
     */
    public LTS toLTS() {
    	//no automato não há distinção entre rótulos de entrada e de saída, portanto une-se os rótulos de entrada e de saída para formar o alfabeto 
		List<String> alfabeto = ListUtils.union(this.inputs, this.outputs);
		// instancia um LTS com os dados do IOLTS
		LTS lts = new LTS(this.getStates(), this.getInitialState(),alfabeto,this.getTransitions());	
		//constroi o automato apartir do LTS
		return lts;
	}
    
    
    /***
     * Constroi um automato apartir do IOLTS
     * @return o automato subjacente ao IOLTS
     */
    public Automaton_ ioltsToAutomaton() {    	
		//constroi o automato apartir do LTS
		return toLTS().ltsToAutomaton();
	}
    
    /***
     * Sobreescrita do método toString, com a separação entre o rótulo de entrada e de saída
     * @return a string que descreve o IOLTS
     */
    @Override
    public String toString() {
    	//chama o toString do LTS
    	String s = super.toString();
    	//adiciona a descrição dos rótulo de entrada
    	s+=("##############################\n");
		s+=("           Entradas \n");
		s+=("##############################\n");
		s+=("Quantidade: " + this.getInputs().size() + "\n");
		for (String e : this.getInputs()) {
			s+=("[" + e + "] - ");
		}
		//adiciona a descrição dos rótulo de saída
		s+=("##############################\n");
		s+=("           Saídas \n");
		s+=("##############################\n");
		s+=("Quantidade: " + this.getOutputs().size() + "\n");
		for (String e : this.getOutputs()) {
			s+=("[" + e + "] - ");
		}
    	return  s;
    }
    
    /***
     * 
     * @param e estado
     * @return lista de rotulos de saída do estado recebido por parâmetro
     */
	public List<String> statesOutputs(State_ e) {
		List<String> rotulos = new ArrayList<String>();		
		
		if(e != null) {
			// percorre todas as transições
			for (Transition_ t : transitions) {
				// verifica se a transição contem o mesmo o estado inicial da transição e o
				// rótulo passados de parametro
				if (t.getEstadoIni().getNome().toString().equals(e.getNome().toString())
						&& outputs.contains(t.getRotulo())) {
					rotulos.add(t.getRotulo());				
				}
			}
		}
		
		
		return rotulos;
	}
    
}
