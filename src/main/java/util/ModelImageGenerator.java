package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DirectedPseudograph;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.util.mxCellRenderer;
import model.IOLTS;
import model.State_;
import model.Transition_;

public class ModelImageGenerator {
	static mxIGraphLayout layout;

	public static BufferedImage generateImage(IOLTS model) throws IOException {

		try {
			int maxTransition = 30;

			// generate image from models with up to maxTransition transitions
			if (model.getTransitions().size() <= maxTransition) {
				File imgFile = File.createTempFile("model", ".png");
				imgFile.createNewFile();

				DirectedPseudograph<String, DefaultEdge> g = ioltsToGraph(model);

				JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<String, DefaultEdge>(g);

				Future<String> control = Executors.newSingleThreadExecutor().submit(new TimeOut(graphAdapter));

				boolean imageGenerated = true;
				int limitSecondToGenerateImage = 5;

				try {
					control.get(limitSecondToGenerateImage, TimeUnit.SECONDS);
				} catch (Exception ex) {// TimeoutException
					control.cancel(true);
					imageGenerated = false;
				}

				if (imageGenerated) {
					layout.execute(graphAdapter.getDefaultParent());

					BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true,
							null);

					// System.out.println(imgFile.getAbsolutePath());
					// ImageIO.write(image, "PNG", imgFile);
					// return imgFile.getAbsolutePath();
					return image;
				} else {
					return null;
					// return "";
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}

		return null;

	}

	private static DirectedPseudograph<String, DefaultEdge> ioltsToGraph(IOLTS model) {

		DirectedPseudograph<String, DefaultEdge> g = new DirectedPseudograph<String, DefaultEdge>(DefaultEdge.class);

		for (State_ state : model.getStates()) {
			g.addVertex(state.toString());

		}

		for (Transition_ t : model.getTransitions()) {
			if (t.getLabel().equals(Constants.DELTA_UNICODE)) {
				g.addEdge(t.getIniState().toString(), t.getEndState().toString(),
						new RelationshipEdge(Constants.DELTA));
			} else {
				g.addEdge(t.getIniState().toString(), t.getEndState().toString(), new RelationshipEdge(t.getLabel()));
			}

		}

		return g;
	}

	public static class TimeOut implements Callable<String> {
		static JGraphXAdapter<String, DefaultEdge> graphAdapter;

		public TimeOut(JGraphXAdapter<String, DefaultEdge> graphAdapter) {
			this.graphAdapter = graphAdapter;
		}

		@Override
		public String call() throws Exception {
			layout = new mxHierarchicalLayout(graphAdapter);
			layout.execute(graphAdapter.getDefaultParent());
			return "";
		}
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
