package jga;

import jga.Population.Selection_Methods;

public class Main {
	public static void main(String[] args) {

		int size_pop = 1000;
		double p_crossover = 0.7;
		double p_mutation = 0.005;
		int nbre_generation = 100;

		String target = "00000000000000000000011111100000000110000001100000100000000001000010011001100100010001100110001001000000000000100100000000000010010000000000001000101000000101000010011111100100000110000001100000000111111000000000000000000000";

		System.out.println("longueur target" + target.length());

		/*
		 * ici la methode de selection par defaut est tournoi, pour changer de
		 * methode ecrivez a la place de "Selection_Methods.tournoi",
		 * "Selection_Methods.[methode souhaitee:"
		 * rang" ou "roulette_proportionnelle"]"
		 */
		Population oPop = new Population(size_pop, target,
				Selection_Methods.tournoi, p_crossover, p_mutation);

		for (int i = 0; i < nbre_generation; i++) {
			oPop.generation();
		}
		System.out.println("Best individu after " + nbre_generation
				+ " rounds: ");
		oPop.getBestIndividu().draw_with_transformation(16, " ", "*");
	}
}