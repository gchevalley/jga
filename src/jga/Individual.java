package jga;

import java.util.Random;

public class Individual {
	String binchain = ""; // Array representant la chaine binaire de l'objet
							// Individual
	int fitness = 0; // sera ajustee lorsque la fonction de class .eval() est
						// appelee

	/**
	 * Constructor d un objet Individual avec une chaine binaire aleatoire
	 * 
	 * @param size
	 *            : nombre d elements dans la chaine binaire constituant
	 *            l'individu, generalement connue grace a la taille du tableau
	 *            de solution optimale
	 */
	public Individual(int size) {
		Random oRandom = new Random();
		while (this.binchain.length() < size) {
			if (oRandom.nextDouble() > 0.5)
				this.binchain += '1';
			if (oRandom.nextDouble() < 0.5)
				this.binchain += '0';
		}
		this.binchain = this.binchain.substring(0, size);
	}

	/**
	 * constructor d un objet Individual avec un chaine binaire connue utilise
	 * pour cree les enfants issus des parents
	 * 
	 * @param force_pattern
	 *            Array de binaires represenant l'individu
	 */
	public Individual(String force_pattern) {
		this.binchain = force_pattern;
	}

	/**
	 * fonction de fitness, permettant l'evaluation d'un individu somme des
	 * constituants identiques entre l'objet et la solution optimale fournie en
	 * parametre
	 * 
	 * @param benchmark
	 *            la solution optimale sous forme d un array de binaires,
	 * @return sommes des elements concordant entre l'objet et la solution
	 *         optimale
	 */
	public int eval(String benchmark) {
		int value = 0;
		for (int i = 0; i < this.binchain.length(); i++) {
			if (this.binchain.charAt(i) == benchmark.charAt(i)) {
				value++;
			}
		}
		this.fitness = value;
		return value;
	}

	/**
	 * appliquer une fonction de mutation sur la representation de chaine
	 * binaire de l objet
	 * 
	 * @param p_mut
	 *            probabilite de mutation
	 */
	public void mutation(double p_mut) {
		Random oRandom = new Random();
		for (int i = 0; i < this.binchain.length(); i++) {
			if (oRandom.nextDouble() <= p_mut) {
				String oldstring = this.binchain;
				if (this.binchain.charAt(i) == '1') {
					if (i == 0) {
						this.binchain = '0' + oldstring.substring(1,
								this.binchain.length());
					} else {
						this.binchain = oldstring.substring(0, i)
								+ '0'
								+ oldstring.substring(i + 1,
										this.binchain.length());
					}
				} else {
					if (i == 0) {
						this.binchain = '1' + oldstring.substring(1,
								this.binchain.length());
					} else {
						this.binchain = oldstring.substring(0, i)
								+ '1'
								+ oldstring.substring(i + 1,
										this.binchain.length());
					}
				}
			}
		}
	}

	/**
	 * affichage de la solution binaire, chaine de 0 et de 1 de l objet
	 */
	public void draw() {
		System.out.print(this.binchain);
	}

	/**
	 * 
	 * @param modulo
	 *            tous les combien de caracteres faut-il faire un retour a la
	 *            ligne
	 * @param char_for_0
	 *            caractere de substitution pour les 0 de la chaine binaire
	 * @param char_for_1
	 *            caractere de substitution pour les 1 de la chaine binaire
	 */
	public void draw_with_transformation(int modulo, String char_for_0,
			String char_for_1) {
		String tmpstr = new String();
		for (int i = 0; i < this.binchain.length(); i++) {
			if (((i) % modulo) == 0 && i != 0) {
				tmpstr = tmpstr + System.getProperty("line.separator");
			}
			if (this.binchain.charAt(i) == '0')
				tmpstr += char_for_0;
			else
				tmpstr += char_for_1;
		}
		System.out.println(tmpstr);
	}
}
