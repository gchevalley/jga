package jga;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList; // pour conserver l historique des populations sans devoir perdre du temps a reconstuire des tableaux lors redimensionnement car le nombre de generation n est pas connu lors de l initialisation de l objet Population
import java.util.Comparator; // interface necessaire a l utilisation de la fonction .sort() des objets de type Arrays

public class Population {

	int size_pop; // nombre d individu completement initialise
	double p_crossover; // probabilite de reproduction
	double p_mutation; // probabilite de mutation
	double p_elite;
	int generation_state = 0; /*
							 * nombre de fois que la function .generation() a
							 * ete appelee
							 */
	Individual oIndividus[]; // Array contenant les objets Individu (les
								// solutions)
	String target; /*
					 * solution optimale, pratique pour connaitre la taille des
					 * objets Individus a generer et pour calculer leur fitness
					 */

	/*
	 * l un des parametres du constructeur principal est la methode de selection
	 * qui peut etre pickee directement dans cette enumeration
	 */
	public enum Selection_Methods {
		tournoi, roulette_proportionnelle, rang
	}

	// la methode de selection choisie par l utilisateur
	Selection_Methods selection_method;

	// historique des populations a travers les generations
	ArrayList<Individual[]> histo_pop = new ArrayList<Individual[]>();

	/**
	 * Constructeur de la class Population avec creation des Individus aleatoire
	 * 
	 * @param size_pop
	 *            nombre d'objet Individu dans l'Array oIndividus
	 * @param target
	 *            solution optimale, pratique car permet de connaitre la taille
	 *            de l'Array de la representation binaire individus, sera
	 *            egalement transmise a la fonction d'evaluation du fitness des
	 *            individus
	 * @param selection_method
	 * @param p_crossover
	 *            probabilite de crossover
	 * @param p_mutation
	 *            probabilite de mutation
	 */
	public Population(int size_pop, String target,
			Selection_Methods selection_method, double p_crossover,
			double p_mutation, double p_elite) {
		this(size_pop); /*
						 * demarre avec l initialisation des variables de class
						 * en appelant un autre constructeur
						 */
		this.target = target;
		this.selection_method = selection_method;
		this.p_crossover = p_crossover;
		this.p_mutation = p_mutation;
		this.p_elite = p_elite;
		// creation des objet Individus aleatoire
		for (int i = 0; i < oIndividus.length; i++) {
			oIndividus[i] = new Individual(this.target.length()); /*
																 * se sert
																 * dirctement de
																 * la taille du
																 * benchmark
																 * pour etablir
																 * la size des
																 * individus
																 */
			this.size_pop++;
		}
	}

	/**
	 * Constructeur de la class Population, initialisant uniquement tableau des
	 * Individus, ne genere pas les individus
	 * 
	 * @param size_pop
	 *            permet de la construction avec une taille approprie de l'Array
	 *            qui contiendra les Individus
	 */
	public Population(int size_pop) {
		// pop sans individu, pratique pour selection
		this.size_pop = 0;
		this.oIndividus = new Individual[size_pop];
	}

	/**
	 * 
	 * Alias qui imposera la selection par tournoi couplee avec une elite de 10%
	 * des meilleurs individus, si l utilisateur n a pas fait son choix parmi l
	 * enumeration Selection_Methods
	 */
	public Population(int size_pop, String target,
			Selection_Methods selection_method, double p_crossover,
			double p_mutation) {
		this(size_pop, target, selection_method, p_crossover, p_mutation, 0.10);
	}

	/**
	 * 
	 * Alias qui imposera la selection par tournoi, si l utilisateur n a pas
	 * fait son choix parmi l enumeration Selection_Methods
	 */
	public Population(int size_pop, String target, double p_crossover,
			double p_mutation, double p_elite) {
		this(size_pop, target, Selection_Methods.tournoi, p_crossover,
				p_mutation, p_elite);
	}

	/**
	 * 
	 * Alias qui imposera une selection de 10% de elite des meilleurs individus,
	 * si l utilisateur n a pas fait son choix pour le parametre p_elite
	 */
	public Population(int size_pop, String target, double p_crossover,
			double p_mutation) {
		this(size_pop, target, Selection_Methods.tournoi, p_crossover,
				p_mutation, 0.10);
	}

	/**
	 * effectue le cycle complet d une generation 1) selection d une population
	 * temporaire avant la reproduction 2)a reproduction, 2 parents produisent 2
	 * enfants qui les remplacent dans la population temporaire 2)b si les
	 * parents n'ont pas ete retenus pour la reproduction, on les passe a la
	 * fonction de mututation des individus 3) la population temporaire des
	 * enfants écrase la précédente
	 */
	public void generation() {
		// selection
		Population popselect = this.selection(); /*
												 * population temporaire retenue
												 * pour reproduction / mutation
												 */
		Random oRandom = new Random();
		for (int i = 0; i < popselect.size_pop; i++) { /*
														 * autant de tour que
														 * d'objet d'Individu
														 * constituant la
														 * population
														 */

			/*
			 * tire au sort une MAMAN grace a l index de l Array des individus
			 * de la population temporaire selectionnee pour reproduction /
			 * mutation
			 */
			int idx_parent1 = oRandom.nextInt(popselect.size_pop);
			/*
			 * tire au sort un PAPA grace a l index de l Array des individus de
			 * la population temporaire selectionnee pour reproduction /
			 * mutation
			 */
			int idx_parent2 = oRandom.nextInt(popselect.size_pop);
			Individual parent1 = popselect.oIndividus[idx_parent1];
			Individual parent2 = popselect.oIndividus[idx_parent2];
			Individual kid1;
			Individual kid2;
			Individual[] two_kids; /*
									 * la fonction crossver retourne un Array de
									 * 2 objets Individu
									 */

			if (oRandom.nextDouble() <= this.p_crossover) {
				// crossover
				two_kids = crossover(parent1, parent2, 2); /*
															 * la fonction
															 * crossver retourne
															 * un Array de 2
															 * objets Individu
															 * (les enfants)
															 */
				kid1 = two_kids[0];
				kid2 = two_kids[1];
			} else {
				// mutation
				parent1.mutation(this.p_mutation);
				parent2.mutation(this.p_mutation);
				kid1 = parent1;
				kid2 = parent2;
			}
			// les 2 enfants remplacent les parents
			popselect.oIndividus[idx_parent1] = kid1;
			popselect.oIndividus[idx_parent2] = kid2;
		}
		this.generation_state++;
		this.histo_pop.add(this.oIndividus);
		this.oIndividus = popselect.oIndividus;
		// stats
		System.out.println("Fitness of the best individu after "
				+ this.generation_state + " generation(s): "
				+ this.getBestIndividu().eval(this.target)
				+ " avg fitness of the whole population: "
				+ this.get_avg_fitness());
	}

	/**
	 * switch/case a travers les differentes methodes de selections possibles
	 * 
	 * @return Population retenue avant la reproduction / mutation
	 */
	public Population selection() {
		switch (this.selection_method) {
		case tournoi:
			return this.selection_tournoi_with_elite(5, this.p_elite);
		case roulette_proportionnelle:
			return this
					.selection_roulette_proportionnelle_with_elite(this.p_elite);
		case rang:
			return this.selection_rang_with_elite(this.p_elite);
		default:
			return this.selection_tournoi_with_elite(5, 0.10);
		}
	}

	/**
	 * 
	 * ordonne dans l'ordre descroissant le vecteur oIndividu d'apres le fitness
	 * 
	 */
	public class IndividualsComparator implements Comparator<Individual> {
		@Override
		public int compare(Individual i1, Individual i2) {
			if (i1.eval(target) == i2.eval(target))
				return 0;
			else if (i1.eval(target) < i2.eval(target))
				return 1;
			else
				return -1;
		}
	}

	/**
	 * trie le vecteur d'individu de la population dans l'ordre descending car
	 * au comparateur si-dessus
	 */
	public void eval_current_state() {
		Arrays.sort(this.oIndividus, new IndividualsComparator());
	}

	/**
	 * 
	 * @return fitness moyen de la population
	 */
	public double get_avg_fitness() {
		this.eval_current_state();
		double tmp_sum_fitness;
		tmp_sum_fitness = 0.0;
		for (int i = 0; i < this.size_pop; i++) {
			tmp_sum_fitness += this.oIndividus[i].fitness; /*
															 * peut utiliser
															 * .fitness car
															 * .eval_current_state
															 * () vient de
															 * tourner
															 */
		}
		return tmp_sum_fitness / this.size_pop;
	}

	/**
	 * 
	 * isole un pourcentage des meilleures individus dans une nouvelle
	 * population
	 * 
	 * @param pct_to_keep
	 * @return objet Population, dont la taille si le taux est inferieur a 1
	 *         sera plus petite que l'originale
	 */
	public Population selection_with_elite(double pct_to_keep) {
		// nouvelle population temporaire
		Population popselect = new Population(this.size_pop);
		popselect.size_pop = 0;
		// rempli un tableau avec les fitness actuels pour chaque individu
		this.eval_current_state();
		/*
		 * maintenant que les fitness sont tries, on ajoute automatiquement a la
		 * population temporaire les meilleurs Individus
		 */
		for (int i = 0; i < this.size_pop; i++) {
			if (((i + 1) / (double) this.size_pop) > pct_to_keep) {
				break;
			} else {
				popselect.oIndividus[popselect.size_pop] = this.oIndividus[i];
				popselect.size_pop++;
			}
		}
		return popselect;
	}

	/**
	 * Selection avec roulette biaisee -> proportionnelle au fitness
	 * 
	 * @deprecated utilise la fonction qui supporte l elite
	 * 
	 * @return Population retenue avant la reproduction / mutation
	 */
	public Population selection_roulette_proportionnelle() {
		Population popselect = new Population(this.size_pop);
		popselect.size_pop = 0;
		// rempli un tableau avec les fitness actuels pour chaque individu
		this.eval_current_state();
		/*
		 * calcul la sum de tous les fitness pour connaitre la taille de la
		 * roulette biaisee
		 */
		int sum_fitness;
		sum_fitness = 0;
		for (int i = 0; i < this.size_pop; i++) {
			// sum_fitness += this.oIndividus[i].eval(this.target); /* deperecie
			// car trop lent */
			sum_fitness += this.oIndividus[i].fitness; /*
														 * comme
														 * .eval_current_state()
														 * a ete appelee, le
														 * champs fitness de l
														 * individu est a jour
														 */
		}
		int[] roulette = new int[sum_fitness];
		int k = 0;
		for (int i = 0; i < this.size_pop; i++) { // parcours tous les individus
			// for(int j=0; j<this.oIndividus[i].eval(this.target); j++) {
			// /*autant de fois que le fitness de l individu */
			for (int j = 0; j < this.oIndividus[i].fitness; j++) {
				roulette[k] = i;
				k++;
			}
		}
		Random oRandom = new Random();
		/*
		 * complete la population temporaire avec tirage aleatoire effectue sur
		 * la roulette biaisee
		 */
		while (popselect.size_pop < this.size_pop) {
			popselect.oIndividus[popselect.size_pop] = this.oIndividus[roulette[oRandom
					.nextInt(roulette.length)]];
			popselect.size_pop++;
		}
		return popselect;
	}

	/**
	 * Selection avec roulette biaisee -> proportionnelle au fitness
	 * 
	 * @param pct_to_keep
	 *            pourcentage d individu automatiquement retenu pour la
	 *            population de reproduction / mutation
	 * @return un nouvel objet Population contenant la population selectionnee
	 *         pour la reproduction et la mutation
	 */
	public Population selection_roulette_proportionnelle_with_elite(
			double pct_to_keep) {
		Population popselect = this.selection_with_elite(pct_to_keep);
		// rempli un tableau avec les fitness actuels pour chaque individu
		this.eval_current_state();
		/*
		 * maintenant que les fitness sont tries, on ajoute automatiquement a la
		 * population temporaire les meilleurs Individus
		 */
		for (int i = 0; i < this.size_pop; i++) {
			if (((i + 1) / (double) this.size_pop) > pct_to_keep) {
				break;
			} else {
				popselect.oIndividus[popselect.size_pop] = this.oIndividus[i];
				popselect.size_pop++;
			}
		}
		/*
		 * calcul la sum de tous les fitness pour connaitre la taille de la
		 * roulette biaisee
		 */
		int sum_fitness;
		sum_fitness = 0;
		for (int i = 0; i < this.size_pop; i++) {
			// sum_fitness += this.oIndividus[i].eval(this.target); /* deperecie
			// car trop lent */
			sum_fitness += this.oIndividus[i].fitness; /*
														 * comme
														 * .eval_current_state()
														 * a ete appelee, le
														 * champs fitness de l
														 * individu est a jour
														 */
		}
		int[] roulette = new int[sum_fitness];
		int k = 0;
		for (int i = 0; i < this.size_pop; i++) { // parcours tous les individus
			// for(int j=0; j<this.oIndividus[i].eval(this.target); j++) {
			/* autant de fois que le fitness de l individu */
			for (int j = 0; j < this.oIndividus[i].fitness; j++) {
				roulette[k] = i;
				k++;
			}
		}
		Random oRandom = new Random();
		/*
		 * complete la population temporaire avec tirage aleatoire effectue sur
		 * la roulette biaisee
		 */
		while (popselect.size_pop < this.size_pop) {
			popselect.oIndividus[popselect.size_pop] = this.oIndividus[roulette[oRandom
					.nextInt(roulette.length)]];
			popselect.size_pop++;
		}
		return popselect;
	}

	/**
	 * @deprecated utiliser la fonction supportant l elite Plutot que de biaiser
	 *             la roulette avec la fitness, ici seul compte l'ordre des
	 *             fitness et pas l'ecart entre eux
	 * 
	 *             Limitation : leger biais, les fitness identiques sont quand
	 *             meme ordonnes et conduisent donc a des probabilite de
	 *             selection legerement biaise ex : les fitness : | 2 | 3 | 3 |
	 *             10 -> | 0 | 1 | 1 | 2 | 2 | 2 | 3 | alors que l indice 1 et 2
	 *             devraient avoir la meme probabilite
	 * 
	 * @return Population retenue avant la reproduction / mutation
	 */
	public Population selection_rang() {
		Population popselect = new Population(this.size_pop);
		popselect.size_pop = 0;
		// sort des fitness, higher first
		this.eval_current_state();
		int count_rang = 0;
		for (int i = 0; i < this.size_pop; i++) {
			count_rang += i + 1;
		}
		int[] roulette_rang = new int[count_rang];
		int k = 0;
		for (int i = 0; i < this.size_pop; i++) {
			for (int j = 1; j <= this.size_pop - i; j++) {
				roulette_rang[k] = i;
				k++;
			}
		}
		Random oRandom = new Random();
		while (popselect.size_pop < this.size_pop) {
			popselect.oIndividus[popselect.size_pop] = this.oIndividus[roulette_rang[oRandom
					.nextInt(roulette_rang.length)]];
			popselect.size_pop++;
		}
		return popselect;
	}

	/**
	 * Plutot que de biaiser la roulette avec la fitness, ici seul compte
	 * l'ordre des fitness et pas l'ecart entre eux
	 * 
	 * Limitation : leger biais, les fitness identiques sont quand meme ordonnes
	 * et conduisent donc a des probabilite de selection legerement biaise ex :
	 * les fitness : | 2 | 3 | 3 | 10 -> | 0 | 1 | 1 | 2 | 2 | 2 | 3 | alors que
	 * l indice 1 et 2 devraient avoir la meme probabilite
	 * 
	 * @return Population retenue avant la reproduction / mutation
	 */
	public Population selection_rang_with_elite(double pct_to_keep) {
		Population popselect = this.selection_with_elite(pct_to_keep);
		// sort des fitness, higher first
		this.eval_current_state();
		int count_rang = 0;
		for (int i = 0; i < this.size_pop; i++) {
			count_rang += i + 1;
		}
		int[] roulette_rang = new int[count_rang];
		int k = 0;
		for (int i = 0; i < this.size_pop; i++) {
			for (int j = 1; j <= this.size_pop - i; j++) {
				roulette_rang[k] = i;
				k++;
			}
		}
		Random oRandom = new Random();
		while (popselect.size_pop < this.size_pop) {
			popselect.oIndividus[popselect.size_pop] = this.oIndividus[roulette_rang[oRandom
					.nextInt(roulette_rang.length)]];
			popselect.size_pop++;
		}
		return popselect;
	}

	/**
	 * 
	 * @deprecated remplacee par selection_tournoi_with_elite
	 * 
	 * @param tournament_size
	 *            le nombre d'individus que l on fait combattre, seul le
	 *            meilleur sera retenu base sur sa fitness
	 * @return un nouvel objet Population contenant la population selectionnee
	 *         pour la reproduction et la mutation
	 */
	public Population selection_tournoi(int tournament_size) {
		Population popselect = new Population(this.size_pop);
		popselect.size_pop = 0;
		while (popselect.size_pop < this.size_pop) {
			popselect.oIndividus[popselect.size_pop] = tournoi(tournament_size);
			popselect.size_pop++;
		}
		return popselect;
	}

	/**
	 * Creation d une population temporaire avant la phase de reproduction /
	 * mutation Un certain pourcentage des meilleurs individus est
	 * automatiquement retenu Pour avoir la meme taille de population qu'au
	 * depart, on la complete ensuite avec des vainqueurs de tournoi
	 * 
	 * @param tournament_size
	 *            le nombre d'individus que l on fait combattre, seul le
	 *            meilleur sera retenu base sur sa fitness
	 * @param pct_to_keep
	 *            pourcentage d individu automatiquement retenu pour la
	 *            population de reproduction / mutation
	 * @return un nouvel objet Population contenant la population selectionnee
	 *         pour la reproduction et la mutation
	 */
	public Population selection_tournoi_with_elite(int tournament_size,
			double pct_to_keep) {
		Population popselect = this.selection_with_elite(pct_to_keep);
		// rempli un tableau avec les fitness actuels pour chaque individu
		this.eval_current_state();
		/*
		 * maintenant que les fitness sont tries, on ajoute automatiquement a la
		 * population temporaire les meilleurs Individus
		 */
		for (int i = 0; i < this.size_pop; i++) {
			if (((i + 1) / (double) this.size_pop) > pct_to_keep) {
				break;
			} else {
				popselect.oIndividus[popselect.size_pop] = this.oIndividus[i];
				popselect.size_pop++;
			}
		}
		/*
		 * complete la population pour qu'elle est la meme taille avec des
		 * tournois
		 */
		while (popselect.size_pop < this.size_pop) {
			popselect.oIndividus[popselect.size_pop] = tournoi(tournament_size);
			popselect.size_pop++;
		}
		return popselect;
	}

	/**
	 * 
	 * Retourne un seul vainqeur
	 * 
	 * @param nombre
	 *            de d Individus combattant en meme temps
	 * @return objet Individu vainqueur du tournoi
	 */
	public Individual tournoi(int tsize) {
		Random oRandom = new Random();
		int tmp_idx;
		int idx_max = -1;
		int value_max = -1;
		for (int i = 0; i < tsize; i++) {
			tmp_idx = oRandom.nextInt(this.size_pop);
			// if (this.oIndividus[tmp_idx].eval(this.target) > value_max) { /*
			// si dipose d une meilleure fitness devient le meilleur */
			if (this.oIndividus[tmp_idx].fitness > value_max) { /*
																 * besoin que .
																 * eval_current_state
																 * () ait tourne
																 * pour utiliser
																 * cette
																 * variable de
																 * class
																 */
				idx_max = tmp_idx;
				// value_max = this.oIndividus[tmp_idx].eval(this.target);
				value_max = this.oIndividus[tmp_idx].fitness;
			}
		}
		return this.oIndividus[idx_max];
	}

	/**
	 * 
	 * @param oI1
	 *            parent numero 1
	 * @param oI2
	 *            parent numero 2
	 * @param nbre_separator
	 *            nombre de points choisis de maniere aleatoire ou les genes
	 *            seront coupes
	 * @return Array de 2 objets Individu (enfants)
	 */
	public Individual[] crossover(Individual oI1, Individual oI2,
			int nbre_separator) {
		// creation des separateurs
		int place_separators[] = new int[nbre_separator];
		/*
		 * positionnement des separateurs par rapport a la chaine binaire des
		 * individus
		 */
		for (int i = 0; i < nbre_separator; i++) {
			Random oRandom = new Random();
			place_separators[i] = oRandom.nextInt(target.length()) + 1;
		}
		// ordonner en ordre croissant les place separators
		java.util.Arrays.sort(place_separators);
		/*
		 * creation de chaines temporaires pour les parents oI1 et oI2 et
		 * stockage de leurs chaines binaires
		 */
		String temp1 = oI1.binchain;
		String temp2 = oI2.binchain;
		// creation de l output final qui sera retourne par la function
		Individual[] two_kids = new Individual[2];
		// crossover
		String newbinstring1 = "";
		String newbinstring2 = "";
		int[] range = new int[nbre_separator];
		for (int i = 0; i < nbre_separator; i++) {
			range[i] = place_separators[i];
		}
		newbinstring1 = newbinstring1.concat(temp1.substring(0, range[0]));
		newbinstring2 = newbinstring2.concat(temp2.substring(0, range[0]));

		/*
		 * pour savoir si le troncon provient du papa ou de la maman, utilise le
		 * modulo
		 */
		for (int i = 0; i < nbre_separator - 1; i++) {
			if (i % 2 == 0) {
				newbinstring1 = newbinstring1.concat(temp2.substring(range[i],
						range[i + 1]));
				newbinstring2 = newbinstring2.concat(temp1.substring(range[i],
						range[i + 1]));
			}
			if (i % 2 != 0) {
				newbinstring1 = newbinstring1.concat(temp1.substring(range[i],
						range[i + 1]));
				newbinstring2 = newbinstring2.concat(temp2.substring(range[i],
						range[i + 1]));
			}
		}
		if ((range.length - 1) % 2 != 0) {
			newbinstring1 = newbinstring1.concat(temp2.substring(
					range[nbre_separator - 1], target.length()));
			newbinstring2 = newbinstring2.concat(temp1.substring(
					range[nbre_separator - 1], target.length()));
		} else {
			newbinstring1 = newbinstring1.concat(temp1.substring(
					range[nbre_separator - 1], temp1.length()));
			newbinstring2 = newbinstring2.concat(temp2.substring(
					range[nbre_separator - 1], temp2.length()));
		}
		/*
		 * impose la chaine binaire des nouveaux individus pour eviter d'avoir
		 * un objet aleatoire, un second constructeur est prevu pour ce cas la
		 */
		two_kids[0] = new Individual(newbinstring1);
		two_kids[1] = new Individual(newbinstring2);
		return two_kids;
	}

	/**
	 * 
	 * @return l'objet Individu issu de la varaible de class Array oIndividus
	 *         avec la meilleure fitness
	 */
	public Individual getBestIndividu() {
		this.eval_current_state(); // class l'Array .oIndividu du meilleur au pire individu
		return this.oIndividus[0];
	}
}
