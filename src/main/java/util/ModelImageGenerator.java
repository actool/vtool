package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jgraph.graph.ConnectionSet.Connection;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.ListenableDirectedGraph;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;

import model.IOLTS;
import model.State_;
import model.Transition_;

public class ModelImageGenerator {

	/*public static void main(String[] args) throws IOException {
		IOLTS a = new IOLTS();
		State_ s0 = new State_("s0");
		State_ s1 = new State_("s1");
		a.addState(s0);
		a.addState(s1);
		
		a.addTransition(new Transition_(s0,"a", s1));
		a.addTransition(new Transition_(s1,"a", s0));
		a.addTransition(new Transition_(s1,"b", s0));
		
		generateImage(a);
	}*/

	// DefaultDirectedGraph
	public static String generateImage(IOLTS model) throws IOException {
		
		File imgFile = File.createTempFile("model", ".png");
		imgFile.createNewFile();
		
		DirectedPseudograph<String, DefaultEdge> g = ioltsToGraph(model);
	
		JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
		mxIGraphLayout  layout = new mxHierarchicalLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());
	
		BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);

		ImageIO.write(image, "PNG", imgFile);

		System.out.println(imgFile.getAbsolutePath());
		
		return imgFile.getAbsolutePath();
	}

	private static DirectedPseudograph<String, DefaultEdge> ioltsToGraph(IOLTS model) {
		
		DirectedPseudograph<String, DefaultEdge> g = new DirectedPseudograph<String, DefaultEdge>(DefaultEdge.class);
		
		for (State_ state : model.getStates()) {
			g.addVertex(state.toString());
		}
		
		for (Transition_ t : model.getTransitions()) {// getTransions(model)
			g.addEdge(t.getIniState().toString(), t.getEndState().toString(), new RelationshipEdge(t.getLabel()));
		}

		
		return g;
	}

	/*
	 * private static List<Transition_> getTransions(IOLTS model) { String label =
	 * ""; List<Transition_> transitions = new ArrayList<Transition_>(); Transition_
	 * newTransition;
	 * 
	 * for (Transition_ actualTransition : model.getTransitions()) { label =
	 * actualTransition.getLabel() + ","; for (Transition_ t :
	 * model.getTransitions()) { if
	 * (!actualTransition.getLabel().equals(t.getLabel()) &&
	 * actualTransition.getIniState().equals(t.getIniState()) &&
	 * actualTransition.getEndState().equals(t.getEndState())) { label +=
	 * t.getLabel() + ","; } } newTransition = new
	 * Transition_(actualTransition.getIniState(), label.substring(0, label.length()
	 * - 1), actualTransition.getEndState()); System.out.println(newTransition);
	 * transitions.add(newTransition);
	 * 
	 * }
	 * 
	 * return transitions; }
	 */
}

class RelationshipEdge extends DefaultEdge {
	private String label;

	public RelationshipEdge(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}
}
