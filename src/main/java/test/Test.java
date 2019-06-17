package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import algorithm.Operations;
import model.Automaton_;
import model.IOLTS;
import model.State_;
import model.Transition_;
import parser.ImportAutFile;
import util.Constants;
import util.ModelImageGenerator;

public class Test {

	public static void main(String[] args) throws Exception {
//		Automaton_ s = new Automaton_();
//		State_ s0 = new State_("s0");
//		State_ s1 = new State_("s1");
//		State_ s2 = new State_("s2");		
//		s.setInitialState(s0);
//		s.addFinalStates(s2);		
//		s.addTransition(new Transition_(s0,"a",s2));
//		s.addTransition(new Transition_(s0,"b",s1));
//		s.addTransition(new Transition_(s1,"a",s2));
//		s.addTransition(new Transition_(s1,"b",s2));
//		s.addTransition(new Transition_(s2,"a",s2));
//		s.addTransition(new Transition_(s2,"b",s1));
//		
//		Automaton_ q = new Automaton_();
//		State_ q0 = new State_("q0");
//		State_ q1 = new State_("q1");
//		q.setInitialState(q0);
//		q.addFinalStates(q1);
//		q.addTransition(new Transition_(q0,"a",q1));
//		q.addTransition(new Transition_(q1,"a",q0));
//		q.addTransition(new Transition_(q1,"b",q1));
//		
//		System.out.println(Operations.intersection(q,s));
		
		
		Automaton_ s = new Automaton_();
		State_ s0 = new State_("s0");
		State_ s1 = new State_("s1");
		State_ s2 = new State_("s2");		
		s.setInitialState(s0);
		s.addFinalStates(s2);
		s.addTransition(new Transition_(s0,Constants.EPSILON,s2));
		//s.addTransition(new Transition_(s0,"b",s2));//
		s.addTransition(new Transition_(s0,"b",s1));
		s.addTransition(new Transition_(s1,"b",s1));
		s.addTransition(new Transition_(s1,"b",s2));
		s.addTransition(new Transition_(s1,"a",s2));
		s.addTransition(new Transition_(s2,"a",s2));
		//s.addTransition(new Transition_(s2,"a",s0));//
		
		//System.out.println(Operations.convertToDeterministicAutomaton(s));
		
		
		String labelIniState = s1.getName();
		String labelTransition = "b";
		
		Transition_ result = s.getTransitions()
			      .stream().parallel()
			      .filter(x -> x.getIniState().getName().equals(labelIniState) && x.getLabel().equals(labelTransition)).findFirst().orElse(null);
		
		System.out.println(result);
		
//		IOLTS iolts = new IOLTS();	
//		State_ s0 = new State_("s0");
//		State_ s1 = new State_("s1");
//		State_ s2 = new State_("s2");
//		State_ s3 = new State_("s3");
//		
//		iolts.addState(s0);
//		iolts.addState(s1);
//		iolts.addState(s2);
//		iolts.addState(s3);
//		iolts.setInitialState(s0);
//		iolts.addTransition(new Transition_(s0,"a",s1));
//		iolts.addTransition(new Transition_(s0,"b",s1));
//		iolts.addTransition(new Transition_(s1,"a",s1));
//		iolts.addTransition(new Transition_(s1,"x",s1));
//		iolts.addTransition(new Transition_(s0,"b",s2));
//		iolts.addTransition(new Transition_(s2,"a",s2));
//		iolts.addTransition(new Transition_(s2,"a",s3));
//		iolts.addTransition(new Transition_(s3,"b",s3));
//		iolts.addTransition(new Transition_(s3,"y",s1));
//		iolts.setOutputs(Arrays.asList("x", "y"));
//		iolts.setInputs(Arrays.asList("a", "b"));
//		IOLTS iolts = ImportAutFile.autToIOLTS("C:\\Users\\camil\\Documents\\aut-separados\\determinismo\\naodet-spec.aut", true, new ArrayList<String>(Arrays.asList("a","b")), new ArrayList<String>(Arrays.asList("x")));
//		iolts.addQuiescentTransitions();
//		Object[] a = Operations.path(iolts, true, "a -> x -> b -> b -> b", "Spec");
//		System.out.println(a[1]);
//		
//		iolts = ImportAutFile.autToIOLTS("C:\\Users\\camil\\Documents\\aut-separados\\determinismo\\iut2-1.aut", true, new ArrayList<String>(Arrays.asList("a","b")), new ArrayList<String>(Arrays.asList("x")));
//		iolts.addQuiescentTransitions();
//		 a = Operations.path(iolts, true, "a -> x -> b -> b -> b", "Spec");
//		System.out.println(a[1]);
		
		
//		BufferedImage  img = ModelImageGenerator.generateImage(iolts);
//		File outputfile = new File("C:\\Users\\camil\\Desktop\\saved.png");
//	    ImageIO.write(img, "png", outputfile);
		

//		try {
//			IOLTS i = ImportAutFile.autToIOLTS("C:\\Users\\camil\\Documents\\aut-separados\\determinismo\\naodet-spec.aut", true, new ArrayList<String>(Arrays.asList("a","b")), new ArrayList<String>(Arrays.asList("x")));
//			i.addQuiescentTransitions();			
//			BufferedImage  img = ModelImageGenerator.generateImage(i);
//			File outputfile = new File("C:\\Users\\camil\\Desktop\\saved.png");
//			 ImageIO.write(img, "png", outputfile);
//			
//			
//			// create automaton
//			Automaton_ as = new Automaton_();
//			// changes attributes based on LTS
//			as.setStates(i.getStates());
//			as.setInitialState(i.getInitialState());
//			as.setAlphabet(i.getAlphabet());			
//			as.setTransitions(Operations.processTauTransition(i.getTransitions()));
//			as.setFinalStates(Arrays.asList(new State_("s3")));
//			/* Automaton_ as = i.ioltsToAutomaton();
//			 as.setFinalStates(Arrays.asList(new State_("s3")));
//			System.out.println(as);*/
//			
//			List<String> words = Operations.getWordsFromAutomaton(as, true);
//			
//			for (String w : words) {
//				System.out.println(w);
//			}
//			//System.out.println(Operations.path(i, true, "a -> b -> b -> b", "Spec"));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		Automaton_ a = new Automaton_();
//		State_ s0 = new State_("s0");
//		State_ s1 = new State_("s1");
//		State_ s2 = new State_("s2");
//		State_ s3 = new State_("s3");
//		State_ s4 = new State_("s4");
//		
//		a.setFinalStates(Arrays.asList(s3));
//		a.setInitialState(s0);
//		a.addState(s0);
//		a.addState(s1);
//		a.addState(s2);
//		a.addState(s3);
//		a.addState(s4);
//		a.addTransition(new Transition_(s0, Constants.EPSILON,s1));
//		a.addTransition(new Transition_(s0, Constants.EPSILON,s2));
//		a.addTransition(new Transition_(s1,"a",s3));
//		a.addTransition(new Transition_(s1,"a",s2));
//		a.addTransition(new Transition_(s2, Constants.EPSILON,s1));
//	
//		
//		System.out.println(Operations.convertToDeterministicAutomaton(a));
			
		
	}

}
