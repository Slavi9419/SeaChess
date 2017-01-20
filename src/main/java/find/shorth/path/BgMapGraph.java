package find.shorth.path;

import org.jgrapht.graph.SimpleWeightedGraph;

public class BgMapGraph {
	SimpleWeightedGraph<String, Double> myGrapht = new SimpleWeightedGraph<String, Double>(Double.class);
	public String[] towns = { "Sofia", "Plovdiv", "Varna", "Burgas","Veliko Tarnovo"};

	public BgMapGraph() {
		for (int i = 0; i < towns.length; i++) {
			myGrapht.addVertex(towns[i]);
		}
		addEdges();
	}

	private void addEdges() {
		myGrapht.addEdge("Sofia", "Plovdiv", 145.44);
		myGrapht.addEdge("Sofia", "Veliko Tarnovo",221.69);
		myGrapht.addEdge("Plovdiv", "Burgas", 252.91);
		myGrapht.addEdge("Plovdiv", "Veliko Tarnovo",212.63);
		myGrapht.addEdge("Veliko Tarnovo", "Varna", 221.59);
		myGrapht.addEdge("Varna", "Burgas", 115.04);
		myGrapht.addEdge("Sofia", "Burgas", 382.76);
		
	}
}
