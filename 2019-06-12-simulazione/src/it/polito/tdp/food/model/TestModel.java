package it.polito.tdp.food.model;

public class TestModel {
	public static void main(String[] args) {
		Model model=new Model();
		model.creaGrafo(60);
		System.out.println(model.getGrafo());
		System.out.println(model.ordinaIngredienti());
		

	}
}
