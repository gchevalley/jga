package jga;

import jga.Population.Selection_Methods;

public class Main {
	public static void main(String[] args) {

		int size_pop = 1000;
		String target = "00000000000000000000011111100000000110000001100000100000000001000010011001100100010001100110001001000000000000100100000000000010010000000000001000101000000101000010011111100100000110000001100000000111111000000000000000000000";
		double p_crossover = 0.7;
		double p_mutation = 0.005;
		double p_elite = 0.10;
		Selection_Methods selection_method = Selection_Methods.tournoi;
		
		int nbre_generation = 100;

		Population oPop = new Population(size_pop, target,
				selection_method, p_crossover, p_mutation, p_elite);

		for (int i = 0; i < nbre_generation; i++) {
			oPop.generation();
		}
		
		System.out.println("Best individu after " + nbre_generation
				+ " rounds: ");
		
		oPop.getBestIndividu().draw_with_transformation(16, " ", "*");
	}
}