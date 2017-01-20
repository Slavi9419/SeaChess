package find.shorth.path;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jgraph.JGraph;
import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgrapht.ext.JGraphModelAdapter;

public class FindAndVisualizeMostShortestPath {

	private Graph graph = new Graph();
	private List<String> towns = new ArrayList<>(graph.getMyGraph().vertexSet());
	private List<String> pathTowns = new ArrayList<String>();
	private JGraph graphAdapter;
	private final JTextField inputStartVertex = startField();
	private final JTextField inputEndVertex = endField();
	private final JList<String> countryList = contryList();
	private final JScrollPane countryScroll = countryScrollPane();
	private JTextArea textAreaFirst = addTextArea();
	private Image map;
	private final JFrame mapFrame = new JFrame();
	private JLabel distLabel = new JLabel();
	private StringBuilder disMessage = new StringBuilder();
	private StringBuilder stepMessage = new StringBuilder();
	private StringBuilder shortEdgesBuild = new StringBuilder();
	private String startVertex;
	private String endVertex;
	private String originalText;
	private String newCountry = "Bulgaria";
	private int endIndex;
	private int length = towns.size();
	private int tmpX, tmpiX, tmpY, tmpiY;
	private int[] path = new int[length];
	private double[] shortDist = new double[length];
	private double[] pathLines = new double[length];
	private boolean inputVertex;
	private Scanner countryStreem;
	private JButton setBtn;

	public FindAndVisualizeMostShortestPath() throws IOException {
		InputStream is = new BufferedInputStream(this.getClass().getResourceAsStream("/Data/Map/bgMap.png"));
		map = ImageIO.read(is);
	}

	private void FaindShorthPath() {

		if (!graph.getMyGraph().containsVertex(startVertex) || !graph.getMyGraph().containsVertex(endVertex)) {
			setMessage("Location not found!");
			return;
		}
		towns = setStartVertexInPositionZero(towns);

		boolean[] visited = new boolean[length];

		for (int i = 0; i < length; i++) {

			if (towns.get(i).equals(endVertex)) {
				endIndex = i;
			}
			if (towns.get(i).equals(startVertex)) {
				shortDist[i] = 0;
			} else {
				shortDist[i] = Integer.MAX_VALUE;
			}
		}

		int min = 0;
		double minWidth = Integer.MAX_VALUE;
		for (int i = 0; i < towns.size(); i++) {
			if (i == 0) {
				for (int j = 0; j < length; j++) {
					if (!visited[j] && graph.getMyGraph().containsEdge(towns.get(i), towns.get(j))
							&& shortDist[j] > shortDist[i] + graph.getMyGraph().getEdge(towns.get(i), towns.get(j))) {

						shortDist[j] = shortDist[i] + graph.getMyGraph().getEdge(towns.get(i), towns.get(j));
						path[j] = i;
						pathLines[j] = (graph.getMyGraph().getEdge(towns.get(i), towns.get(j)));

					}
				}

				visited[i] = true;
			} else if (i != 0) {
				minWidth = Integer.MAX_VALUE;
				for (int j = 0; j < length; j++) {
					if (!visited[j] && shortDist[j] < minWidth) {
						minWidth = shortDist[j];
						min = j;
					}
				}

				for (int l = 0; l < length; l++) {
					if (!visited[l] && graph.getMyGraph().containsEdge(towns.get(min), towns.get(l))) {
						if (l == length - 1) {
							visited[min] = true;
						}
						if (shortDist[l] > shortDist[min]
								+ (graph.getMyGraph().getEdge(towns.get(min), towns.get(l)))) {

							shortDist[l] = shortDist[min] + (graph.getMyGraph().getEdge(towns.get(min), towns.get(l)));
							path[l] = min;
							pathLines[l] = (graph.getMyGraph().getEdge(towns.get(min), towns.get(l)));

						}
					}
				}
				visited[min] = true;
			}
		}
		addShortPath();
	}

	private List<String> setStartVertexInPositionZero(List<String> towns) {
		if (!towns.get(0).equals(startVertex)) {
			for (int i = 1; i < towns.size(); i++) {
				if (!towns.get(i).equals(startVertex)) {
					continue;
				} else {
					Collections.swap(towns, 0, i);

					tmpX = graph.graphFactory.getXCoordinate(0);
					tmpiX = graph.graphFactory.getXCoordinate(i);
					tmpY = graph.graphFactory.getYCoordinate(0);
					tmpiY = graph.graphFactory.getYCoordinate(i);

					graph.graphFactory.setXCoordinate(0, tmpiX);
					graph.graphFactory.setYCoordinate(0, tmpiY);
					graph.graphFactory.setXCoordinate(i, tmpX);
					graph.graphFactory.setYCoordinate(i, tmpY);

				}
			}
		}
		return towns;
	}

	private void addShortPath() {

		for (int i = 0; i < length; i++) {
			if (!towns.get(i).equals(endVertex)) {
				continue;
			} else {
				double value = shortDist[i];
				value = value * 100;
				value = (double) ((int) value);
				value = value / 100;
				setMessage("The distance is " + value + " km.");
			}
		}

		while (endIndex != 0) {
			pathTowns.add(towns.get(endIndex));
			shortEdgesBuild.append(Double.toString(pathLines[endIndex]));
			endIndex = path[endIndex];
			shortEdgesBuild.append("\n");
			if (endIndex == 0) {
				pathTowns.add(towns.get(endIndex));
				break;
			}
		}
		for (int i = pathTowns.size() - 1; i >= 0; i--) {
			if (i == 0) {
				setStepMessage(pathTowns.get(i));
			} else {
				setStepMessage(pathTowns.get(i) + " -> ");
			}
		}
	}

	public void show() {
		graph.graphFactory.setCountryEdgesFile(this.getClass().getResourceAsStream("/Data/edgesBG.txt"));
		graphAdapter = buildUrbanNetwork();
		mapFrame.setSize(810, 490);
		//mapFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mapFrame.getContentPane().add(graphAdapter);
		ImageIcon img = new ImageIcon("Data/Logo/logo.png");
		mapFrame.setIconImage(img.getImage());

		final JPanel surchPanel = new JPanel();
		final JButton searchBtn = new JButton("Search");
		searchBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textAreaFirst.setVisible(false);
				setStartVertex(inputStartVertex.getText().trim());
				setEndVertex(inputEndVertex.getText().trim());
				Arrays.fill(pathLines, 0);
				shortEdgesBuild.setLength(0);
				disMessage.setLength(0);
				FaindShorthPath();
				mapFrame.getContentPane().remove(graphAdapter);
				surchPanel.remove(distLabel);
				distLabel = new JLabel(getMessage());
				distLabel.setForeground(Color.RED);
				graphAdapter = buildUrbanNetwork();
				surchPanel.add(distLabel);
				JScrollPane scrollPane = new JScrollPane(graphAdapter);
				mapFrame.getContentPane().add(scrollPane);
				mapFrame.revalidate();
				mapFrame.repaint();
			}

		});
		setBtn = new JButton("Set");
		setBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startVertex = "";
				endVertex = "";
				newCountry = countryList.getSelectedValue();
				mapFrame.getContentPane().remove(graphAdapter);
				shortEdgesBuild.setLength(0);
				try {
					if (newCountry.equals("Germany")) {
						graph.graphFactory
								.setCountryVertexFile(this.getClass().getResourceAsStream("/Data/vertexGB.txt"));
						graph.graphFactory
								.setCountryEdgesFile(this.getClass().getResourceAsStream("/Data/edgesGB.txt"));
						InputStream is = new BufferedInputStream(
								this.getClass().getResourceAsStream("/Data/Map/gbMap.png"));
						map = ImageIO.read(is);

					} else if (newCountry.equals("Bulgaria")) {
						graph.graphFactory
								.setCountryVertexFile(this.getClass().getResourceAsStream("/Data/vertexBG.txt"));
						graph.graphFactory
								.setCountryEdgesFile(this.getClass().getResourceAsStream("/Data/edgesBG.txt"));
						InputStream is = new BufferedInputStream(
								this.getClass().getResourceAsStream("/Data/Map/bgMap.png"));
						map = ImageIO.read(is);

					} else if (newCountry.equals("Macedonia")) {
						graph.graphFactory
								.setCountryVertexFile(this.getClass().getResourceAsStream("/Data/vertexMK.txt"));
						graph.graphFactory
								.setCountryEdgesFile(this.getClass().getResourceAsStream("/Data/edgesMK.txt"));
						InputStream is = new BufferedInputStream(
								this.getClass().getResourceAsStream("/Data/Map/mkMap.png"));
						map = ImageIO.read(is);

					} else if (newCountry.equals("Russia")) {
						graph.graphFactory
								.setCountryVertexFile(this.getClass().getResourceAsStream("/Data/vertexRU.txt"));
						graph.graphFactory
								.setCountryEdgesFile(this.getClass().getResourceAsStream("/Data/edgesRU.txt"));
						InputStream is = new BufferedInputStream(
								this.getClass().getResourceAsStream("/Data/Map/ruMap.png"));
						map = ImageIO.read(is);

					} else if (newCountry.equals("USA")) {
						graph.graphFactory
								.setCountryVertexFile(this.getClass().getResourceAsStream("/Data/vertexUS.txt"));
						graph.graphFactory
								.setCountryEdgesFile(this.getClass().getResourceAsStream("/Data/edgesUS.txt"));
						InputStream is = new BufferedInputStream(
								this.getClass().getResourceAsStream("/Data/Map/usMap.png"));
						map = ImageIO.read(is);

					} else if (newCountry.equals("Canada")) {
						graph.graphFactory
								.setCountryVertexFile(this.getClass().getResourceAsStream("/Data/vertexCA.txt"));
						graph.graphFactory
								.setCountryEdgesFile(this.getClass().getResourceAsStream("/Data/edgesCA.txt"));
						InputStream is = new BufferedInputStream(
								this.getClass().getResourceAsStream("/Data/Map/caMap.png"));
						map = ImageIO.read(is);

					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}  finally {
					graph.setMyGrapht(graph.graphFactory.getMyNewGraphtt());
					towns = new ArrayList<>(graph.getMyGraph().vertexSet());
					inputVertex = false;
					graphAdapter = buildUrbanNetwork();
					length = towns.size();
					pathLines = new double[length];
					shortDist = new double[length];
					path = new int[length];
					textAreaFirst = addTextArea();
					textAreaFirst.setVisible(false);
					inputStartVertex.setText("");
					inputEndVertex.setText("");
					surchPanel.removeAll();
					surchPanel.add(inputStartVertex);
					surchPanel.add(textAreaFirst);
					surchPanel.add(inputEndVertex);
					surchPanel.add(searchBtn);
					surchPanel.add(new JLabel("Country"));
					surchPanel.add(countryScroll);
					surchPanel.add(setBtn);
					mapFrame.getContentPane().add(graphAdapter);
					mapFrame.revalidate();
					mapFrame.repaint();
				}
			}
		});

		textAreaFirst.setVisible(false);
		surchPanel.setPreferredSize(new Dimension(1800, 55));
		surchPanel.add(inputStartVertex);
		surchPanel.add(textAreaFirst);
		surchPanel.add(inputEndVertex);
		surchPanel.add(searchBtn);
		surchPanel.add(distLabel);
		surchPanel.add(new JLabel("Country"));
		surchPanel.add(Box.createGlue());
		surchPanel.add(countryScroll);
		surchPanel.add(setBtn);
		mapFrame.add(surchPanel, BorderLayout.NORTH);
		mapFrame.setLocationRelativeTo(null);
		mapFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mapFrame.setVisible(true);
	}

	private JGraph buildUrbanNetwork() {
		final JGraph jGraph = new JGraph(new JGraphModelAdapter<String, Double>(graph.getMyGraph()));
		// b.setBackgroundImage(map);
		final ImagePanel mapPanel = new ImagePanel(map, 20.0);
		jGraph.setBackgroundComponent(mapPanel);
//		 addMouseListener(new MouseAdapter() {
//		 @Override
//		 public void mousePressed(MouseEvent e) {
//		 if(SwingUtilities.isLeftMouseButton(e)) {
//		 zoomIn();
//		 repaint();
//		 }
//		 }
//		 });
		GraphLayoutCache cache = jGraph.getGraphLayoutCache();
		
		int width = 90;
		int height = 30;

		for (Object item : jGraph.getRoots()) {

			String strItem = item.toString();
			GraphCell cell = (GraphCell) item;
			CellView view = cache.getMapping(cell, true);
			AttributeMap map = view.getAttributes();
			map.applyValue(GraphConstants.BACKGROUND, Color.BLACK);
			AttributeMap attr = cell.getAttributes();

			for (int i = 0; i < towns.size(); i++) {
				if (strItem.equals(towns.get(i))) {
					Rectangle2D newBounds = new Rectangle2D.Double(graph.graphFactory.getXCoordinate(i),
							graph.graphFactory.getYCoordinate(i), width, height);
					GraphConstants.setBounds(attr, newBounds);
					GraphConstants.setGradientColor(attr, Color.GREEN);
					if (strItem.equals(startVertex)) {
						GraphConstants.setGradientColor(attr, Color.BLUE);
					} else if (strItem.equals(endVertex)) {
						GraphConstants.setGradientColor(attr, Color.RED);
					}
					break;
				}
			}

			if (shortEdgesBuild.toString().trim().contains(strItem)) {
				GraphConstants.setLineWidth(attr, 5);
				GraphConstants.setLineColor(attr, Color.BLUE);
			} else {
				GraphConstants.setLineWidth(attr, 0);
			}
		}
		jGraph.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (jGraph.getSelectionCell() == null) {
					mapPanel.zoomIn();
					return;
				}

				if (!graph.getMyGraph().containsVertex(jGraph.getSelectionCell().toString())) {
					
					return;
				}

				if (!inputVertex) {
					inputStartVertex.setText(jGraph.getSelectionCell().toString());
					if (!inputEndVertex.getText().equals("")) {
						inputEndVertex.setText("");
					}
					inputVertex = true;
				} else {
					inputEndVertex.setText(jGraph.getSelectionCell().toString());
					inputVertex = false;

				}

			}
		});
		cache.reload();
		jGraph.repaint();
		return jGraph;
	}

	private String getMessage() {
		return disMessage.toString();
	}

	private void setMessage(String message) {
		this.disMessage.append(message);
	}

	private void setStepMessage(String stepMessage) {
		this.stepMessage.append(stepMessage);
	}

	private JTextField startField() {
		final JTextField text = new JTextField(8);
		text.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				inputVertex = false;
				textAreaFirst.setVisible(true);
				firstLocationFilter();
			}

			public void keyReleased(KeyEvent e) {
				firstLocationFilter();
			}

		});

		return text;
	}

	private JTextField endField() {
		JTextField text = new JTextField(8);
		text.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {

				textAreaFirst.setVisible(true);
				inputVertex = true;
				seconLocationFilter();
			}

			public void keyReleased(KeyEvent e) {
				seconLocationFilter();
			}
		});
		return text;
	}

	private void setStartVertex(String startVertex) {
		this.startVertex = startVertex;
	}

	private void setEndVertex(String endVertex) {
		this.endVertex = endVertex;
	}

	private JTextArea addTextArea() {
		JTextArea textArea = new JTextArea();
		textArea.setPreferredSize(new Dimension(80, 50));

		for (String v : graph.getMyGraph().vertexSet()) {
			textArea.append(v);
			textArea.append("\n");
		}

		originalText = textArea.getText();
		textArea.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (!inputVertex) {
					inputStartVertex.setText(textAreaFirst.getSelectedText());
				} else {
					inputEndVertex.setText(textAreaFirst.getSelectedText());
				}
			}
		});
		return textArea;
	}

	private void firstLocationFilter() {

		textAreaFirst.setText(originalText);
		StringBuilder builder = new StringBuilder();
		String[] lines = originalText.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];

			if (line.toLowerCase().contains(inputStartVertex.getText().toLowerCase())) {
				builder.append(line);
				builder.append("\n");
			}
		}
		textAreaFirst.setText(builder.toString());
	}

	private void seconLocationFilter() {

		textAreaFirst.setText(originalText);

		StringBuilder builder = new StringBuilder();
		String[] lines = originalText.split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];

			if (line.toLowerCase().contains(inputEndVertex.getText().toLowerCase())) {
				builder.append(line);
				builder.append("\n");
			}
		}
		textAreaFirst.setText(builder.toString());
	}

	private JList<String> contryList() {
		JList<String> list = new JList<String>();
		DefaultListModel<String> listModel = new DefaultListModel<String>();

		try {
			countryStreem = new Scanner(this.getClass().getResourceAsStream("/Data/countryes.txt"));
			while (countryStreem.hasNext()) {
				String line = countryStreem.next();
				String[] words = line.split(",");
				for (int i = 0; i < words.length; i++) {
					listModel.addElement(words[i].toString().trim());
				}
			}
		} finally {
			countryStreem.close();
		}

		list.setModel(listModel);
		list.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					setBtn.doClick();
				}
			}
		});

		return list;
	}

	private JScrollPane countryScrollPane() {
		JScrollPane scroll = new JScrollPane(countryList);
		scroll.setPreferredSize(new Dimension(100, 55));
		return scroll;
	}
}
