package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private Graph<User,DefaultWeightedEdge>grafo;
	private YelpDao dao;
	private Map<String,User>idMap;
	public Model() {
		dao=new YelpDao();
		idMap=new HashMap<>();
		this.dao.getAllUsers(idMap);
	}public void creaGrafo(int n,int anno) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
	Graphs.addAllVertices(this.grafo, this.dao.getVertici(n, idMap));
	for(Adiacenza a:dao.getArchi( anno,idMap)) {
		if(this.grafo.containsVertex(a.getU1())&&this.grafo.containsVertex(a.getU2())) {
			DefaultWeightedEdge e=this.grafo.getEdge(a.getU1(),a.getU2());
		
		if(e==null) { Graphs.addEdgeWithVertices(grafo, a.getU1(),a.getU2(),a.getPeso());
			
		}}}
	}public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<User>getSimile(User u){
		List<User>result=new ArrayList<User>();
		int max=0;
		for(DefaultWeightedEdge e:this.grafo.edgesOf(u)) {
			if(this.grafo.getEdgeWeight(e)>max) {
				max=(int)this.grafo.getEdgeWeight(e);
			}
		}
		for(DefaultWeightedEdge e :this.grafo.edgesOf(u)) {
			if((int)this.grafo.getEdgeWeight(e)==max) {
				User u2=Graphs.getOppositeVertex(this.grafo, e, u);
				result.add(u2);
			}
			
			
		}
		return result;
		
	}
	public List<User> getVertici() {
		// TODO Auto-generated method stub
		return new ArrayList<>(this.grafo.vertexSet());
	}
	
	

}
