package find.shorth.path;

import java.io.FileNotFoundException;

import org.jgrapht.graph.SimpleWeightedGraph;

public class Graph {
	public SimpleWeightedGraph<String, Double> myGraph;
	public GraphFactory graphFactory;

	public Graph() {

		 graphFactory = new TownsFactory();
		try {
			myGraph = graphFactory.create();
		} catch (FileNotFoundException e) {

			e.printStackTrace();

		}
	}

	public SimpleWeightedGraph<String, Double> getMyGraph() {
		return myGraph;
	}

	public void setMyGrapht(SimpleWeightedGraph<String, Double> myGrapht) {
		this.myGraph = myGrapht;
	}
}
