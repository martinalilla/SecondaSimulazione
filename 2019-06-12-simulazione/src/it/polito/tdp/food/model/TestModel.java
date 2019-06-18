package it.polito.tdp.food.model;

import org.jgrapht.Graph;

public class TestModel {
	public static void main(String[] args) {
		Model model=new Model();
		model.creaGrafo(60);
		Graph grafo=model.getGrafo();
		//System.out.println(model.getGrafo());
		//System.out.println(model.ordinaIngredienti());
		System.out.println(model.verticiGrafo());
		System.out.println(model.archiGrafo());
		

	}
}
