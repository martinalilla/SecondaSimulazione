package it.polito.tdp.food.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.Condiment;
import it.polito.tdp.food.db.FoodDao;

public class Model {
	private FoodDao dao;
	private SimpleWeightedGraph<Condiment, DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao = new FoodDao();
	}
	
	public List<Condiment> getIngredienti(double calorie){
		return dao.condimentiCalorie(calorie);
	}
	
	public void creaGrafo(double calorie) {
		grafo = new SimpleWeightedGraph<Condiment, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		List<Condiment> condimenti = getIngredienti(calorie);
		for(Condiment c: condimenti) {
			grafo.addVertex(c);
		}
		for(Condiment c1: grafo.vertexSet()) {
			for(Condiment c2: grafo.vertexSet()) {
				if(dao.condimentsFood(c1, c2)>0 && !grafo.containsEdge(c1, c2) && !c1.equals(c2)) {
					Graphs.addEdgeWithVertices(grafo, c1, c2, dao.condimentsFood(c1, c2));
				}
			}
		}
		System.out.println(grafo);
	}

	public SimpleWeightedGraph<Condiment, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(SimpleWeightedGraph<Condiment, DefaultWeightedEdge> grafo) {
		this.grafo = grafo;
	}
	public String ordinaIngredienti() {
		String s="";
		
		List <Condiment> condimenti = new LinkedList<Condiment>();
		for(Condiment c: grafo.vertexSet()) {
			condimenti.add(c);
		}
		Collections.sort(condimenti);
		
		for(Condiment c:condimenti) {
			double tot=0;
			Set <DefaultWeightedEdge>archi=grafo.outgoingEdgesOf(c);
			for(DefaultWeightedEdge e:archi ) {
				tot+=grafo.getEdgeWeight(e);
			}	
s+="Ingrediente: "+c.toString()+", calorie corrispondenti:  "+c.getCondiment_calories()+" Numero totale di cibi che lo contengono:  "+(int)tot+"\n";
			
	}
		return s;
	}
	
	

}
