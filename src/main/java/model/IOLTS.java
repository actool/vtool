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
    	//chama os m�todos da super classe para inicializar os parametros
    	this.setInitialState(lts.getInitialState());
    	this.setStates(lts.getStates());
    	this.setTransitions(lts.getTransitions());
    	this.setAlphabet(lts.getAlphabet());
    }
    
    /**
     * Retorna os r�tulos de entradas
     * @return os rotulos de Entrada
     */
    public List<String> getInputs() {
        return inputs;
    }

    /** Altera os r�tulos de entrada
     * @param rotulosEntrada o conjunto de r�tulos de entrada
     */
    public void setInputs(List<String> rotulosEntrada) {
        this.inputs = rotulosEntrada;
    }

    /**
     * Retorna o conjunto de r�tulos de sa�da
     * @return rotulosSaida o conjunto de r�tulos de sa�da
     */
    public List<String> getOutputs() {
        return outputs;
    }

    /** Altera o conjunto de r�tulos de sa�da
     * @param rotulosSaida  o conjunto de r�tulos de sa�da
     */
    public void setOutputs(List<String> rotulosSaida) {
        this.outputs = rotulosSaida;
    }
    

    /***
     * Constroi um automato apartir do IOLTS
     * @return o automato subjacente ao IOLTS
     */
    public LTS toLTS() {
    	//no automato n�o h� distin��o entre r�tulos de entrada e de sa�da, portanto une-se os r�tulos de entrada e de sa�da para formar o alfabeto 
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
     * Sobreescrita do m�todo toString, com a separa��o entre o r�tulo de entrada e de sa�da
     * @return a string que descreve o IOLTS
     */
    @Override
    public String toString() {
    	//chama o toString do LTS
    	String s = super.toString();
    	//adiciona a descri��o dos r�tulo de entrada
    	s+=("##############################\n");
		s+=("           Entradas \n");
		s+=("##############################\n");
		s+=("Quantidade: " + this.getInputs().size() + "\n");
		for (String e : this.getInputs()) {
			s+=("[" + e + "] - ");
		}
		//adiciona a descri��o dos r�tulo de sa�da
		s+=("##############################\n");
		s+=("           Sa�das \n");
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
     * @return lista de rotulos de sa�da do estado recebido por par�metro
     */
	public List<String> statesOutputs(State_ e) {
		List<String> rotulos = new ArrayList<String>();		
		
		if(e != null) {
			// percorre todas as transi��es
			for (Transition_ t : transitions) {
				// verifica se a transi��o contem o mesmo o estado inicial da transi��o e o
				// r�tulo passados de parametro
				if (t.getEstadoIni().getNome().toString().equals(e.getNome().toString())
						&& outputs.contains(t.getRotulo())) {
					rotulos.add(t.getRotulo());				
				}
			}
		}
		
		
		return rotulos;
	}
    
}
