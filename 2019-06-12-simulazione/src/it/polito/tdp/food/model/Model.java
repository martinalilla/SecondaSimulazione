package it.polito.tdp.food.model;

import java.util.ArrayList;
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
	
	private List<Condiment> dietaBest;
	private double maxCalorie;
	
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
s+="Ingrediente: "+c.toString()+"\n Calorie corrispondenti:  "+c.getCondiment_calories()+"\n Numero totale di cibi che lo contengono:  "+(int)tot+"\n";
			
	}
		return s;
	}
	public List<Condiment> dietaEquilibrata(Condiment c){
		//preparo le variabili utili alla ricorsione 
		List<Condiment> parziale = new ArrayList<Condiment>();
		this.dietaBest=new ArrayList<Condiment>();
		maxCalorie=0;
		
		
		
		
		//itero a livello zero
		//for(Condiment co: grafo.vertexSet()) {
			//if(c.equals(co)) {
				parziale.add(c);
				cerca(1, parziale, c.getCondiment_calories());
				parziale.remove(0);
			//}
	//	}
		
		return dietaBest;
	}
	
	private void cerca(int livello, List<Condiment> parziale, double contaCalorie) {
		boolean trovato=false;
		List<Condiment> vertici=new ArrayList<Condiment>();
		for(Condiment vertice: grafo.vertexSet()){
			vertici.add(vertice);
		}
		
		
		Condiment ultimo=parziale.get(livello-1);
		//per ogni prossimo che considero, rimuovo tutti i suoi vicini 
		//e aggiungo a parziali tutti i condimenti del vertex set.
		
		vertici.removeAll(Graphs.neighborListOf(grafo, ultimo));
		
		
		for(Condiment indipendente: vertici) {
				if(!parziale.contains(indipendente) && !grafo.containsEdge(ultimo, indipendente) && !ultimo.equals(indipendente)
						) {
					//candidato accettabile, fai ricorsione
					trovato=true;
					parziale.add(indipendente);
					System.out.println(indipendente);
					contaCalorie+=indipendente.getCondiment_calories();
					cerca(livello+1, parziale, contaCalorie);
					contaCalorie-=indipendente.getCondiment_calories();
					parziale.remove(livello);
				}
		}
		
		
		//valuta caso terminale
		if(!trovato) {
			if(contaCalorie>maxCalorie) {
				maxCalorie=contaCalorie;
				dietaBest=new ArrayList<Condiment>(parziale); //clona il best
				
			}
		}
		
	}


	

	public List<Condiment> getDietaBest() {
		return dietaBest;
	}

	public void setDietaBest(List<Condiment> dietaBest) {
		this.dietaBest = dietaBest;
	}

	public double getMaxCalorie() {
		return maxCalorie;
	}

	public void setMaxCalorie(double maxCalorie) {
		this.maxCalorie = maxCalorie;
	}
	
	public boolean vicinoParziale(List<Condiment> parziale, Condiment indipendente) {
		for(Condiment co: parziale) {
		for(Condiment c: Graphs.neighborListOf(grafo, co)) {
			if(c.equals(indipendente)) {
				return false; //controllo che l'elemento che ho scelto di aggiungere a parziale non sia già vicino di 
				              //qualcuno presente in parziale.
			}
		}
		}
		return true;
	}
	
	public String verticiGrafo() {
		String s="Numero di vertici= "+grafo.vertexSet().size()+"\nElenco dei vertici: \n";
		for(Condiment c: grafo.vertexSet()) {
			s+=c.toString()+"\n";
		}
		return s;
	}
	
	public String archiGrafo() {
		String s="Gli archi, ovvero gli ingredienti con almeno un cibo in comune sono i seguenti: \n";
		for(DefaultWeightedEdge arco : grafo.edgeSet()) {
			s+=arco.toString()+"\n";
		}
		return s;
	}
	
	
	
	

}
