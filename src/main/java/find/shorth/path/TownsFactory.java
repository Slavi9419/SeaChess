package find.shorth.path;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.jgrapht.graph.SimpleWeightedGraph;

public class TownsFactory implements GraphFactory {
	SimpleWeightedGraph<String, Double> myGrapht;
	private Scanner townEdgeStream;
	private Scanner townVertexStreem;
	private ArrayList<Integer> XCoordinates;
	private ArrayList<Integer> YCoordinates;
	private InputStream countryEdgesStream;
	private InputStream countryVertexStream;
	
	public TownsFactory () {
		countryVertexStream = this.getClass().getResourceAsStream("/Data/vertexBG.txt");
		countryEdgesStream = this.getClass().getResourceAsStream("/Data/edgesBG.txt");
	}

	@Override
	public SimpleWeightedGraph<String, Double> create() throws FileNotFoundException {
		myGrapht = new SimpleWeightedGraph<String, Double>(Double.class);
		YCoordinates = new ArrayList<Integer>();
		XCoordinates = new ArrayList<Integer>();
		try {
			townVertexStreem = new Scanner(countryVertexStream);
			while (townVertexStreem.hasNext()) {
				String line = townVertexStreem.next();
				String[] words = line.split(",");
				for (int i = 0; i < words.length; i++) {
					if (i == 0) {
						myGrapht.addVertex(words[i]);
					} else if (i == 1) {
						XCoordinates.add(Integer.parseInt(words[i]));
						
					} else if (i == 2) {
						YCoordinates.add(Integer.parseInt(words[i]));
						
					}
				}
			}
		} finally {
			townVertexStreem.close();

		}
		return myGrapht;
	}

	private SimpleWeightedGraph<String, Double> addEdges(SimpleWeightedGraph<String, Double> myGrapht) {
		try {
			townEdgeStream = new Scanner(countryEdgesStream);

			String a = null;
			String b = null;
			Double width = null;
			while (townEdgeStream.hasNext()) {
				String line = townEdgeStream.next();
				String[] words = line.split(",");
				for (int i = 0; i < words.length; i++) {
					if (i == 0) {
						a = words[i];
					} else if (i == 1) {
						b = words[i];
					} else if (i == 2) {
						width = Double.parseDouble(words[i]);
					}
				}
				myGrapht.addEdge(a, b, width);

			}

		}  finally {
			townEdgeStream.close();
		}
		this.myGrapht = myGrapht;
		return this.myGrapht;
	}

	public int getXCoordinate(int index) {
		return (int) XCoordinates.get(index);
	}

	public int getYCoordinate(int index) {
		return (int) YCoordinates.get(index);
	}

	public void setXCoordinate(int index, int value) {
		this.XCoordinates.set(index, value);
	}

	public void setYCoordinate(int index, int value) {
		this.YCoordinates.set(index, value);
	}


	public void setCountryEdgesFile(InputStream countryEdges) {
		this.countryEdgesStream = countryEdges;
		addEdges(myGrapht);
	}

	public void setCountryVertexFile(InputStream countryVertex) {
		this.countryVertexStream = countryVertex;
		try {
			create();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public SimpleWeightedGraph<String, Double> getMyNewGraphtt() {
		return myGrapht;
	}

}
