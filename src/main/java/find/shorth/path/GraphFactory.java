package find.shorth.path;


import java.io.FileNotFoundException;
import java.io.InputStream;

import org.jgrapht.graph.SimpleWeightedGraph;

public interface GraphFactory {
	public SimpleWeightedGraph<String, Double> create() throws FileNotFoundException;

	public int getXCoordinate(int i);

	public int getYCoordinate(int i);

	public void setXCoordinate(int i, int j);

	public void setYCoordinate(int i, int tmpY);

	public void setCountryVertexFile(InputStream file);

	public void setCountryEdgesFile(InputStream file);

	public SimpleWeightedGraph<String, Double> getMyNewGraphtt();

	
}
