package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jgraph.graph.ConnectionSet.Connection;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;

import model.IOLTS;
import model.State_;
import model.Transition_;

public class ModelImageGenerator {
	/*public static void main(String[] args) throws IOException {
		generateImage(new IOLTS());
	}*/
	
	public static String  generateImage(IOLTS model) throws IOException {
		File imgFile = File.createTempFile("model", ".png");
		imgFile.createNewFile();

		DefaultDirectedGraph<String, DefaultEdge> g =ioltsToGraph(model);

		JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);
		mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
		layout.execute(graphAdapter.getDefaultParent());

		BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);

		ImageIO.write(image, "PNG", imgFile);

		return imgFile.getAbsolutePath();
	}

	 private static DefaultDirectedGraph ioltsToGraph(IOLTS model) {
		 DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

			for (State_ state: model.getStates()) {
				g.addVertex(state.toString());
			}
			
			for (Transition_ t : model.getTransitions()) {
				g.addEdge(t.getIniState().toString(), t.getEndState().toString(), new RelationshipEdge(t.getLabel()));
			}

		 return g;
	 }
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
