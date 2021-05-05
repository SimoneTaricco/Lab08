package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;


public class Model {
	
	private Graph<Airport,DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer,Airport> idMap; // per adiacenze
	
	private ArrayList<Adiacenza> res;
	
	public Model() {
		dao = new ExtFlightDelaysDAO();
		idMap = new HashMap<Integer,Airport>();
	}
	
	public void creaGrafo(int distMedia) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		res = new ArrayList<Adiacenza>(); // override di toString in adiacenza così da stampare gli archi in maniera un po' più decente
		
		for (Airport a:dao.loadAllAirports()) {
			idMap.put(a.getId(), a);
		}
		
		Graphs.addAllVertices(grafo, idMap.values());
		
		for(Adiacenza a:dao.getAdiacenze()) {
			if (a.getPeso()>distMedia) {
				Graphs.addEdge(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
				res.add(a);
			}
		}
		
	}
	
	public String result() {
		String res = "";

		res += "Numero di vertici: " + grafo.vertexSet().size() + "\n";
		res += "Numero di archi: " + grafo.edgeSet().size() + "\n";
		
		for (Adiacenza a:this.res) {
			res += a.toString() + "\n";
		}

		return res;
	}

}
