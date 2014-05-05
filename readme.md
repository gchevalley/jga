#Notice d'utilisation           

Lien pour le code source: [https://github.com/gchevalley/jga](https://github.com/gchevalley/jga)

3 méthodes de sélection ont été implémentées: la sélection par tournoi, la sélection par rang et la sélection par roulette proportionnelle. Chacune supporte également en amont la sélection d’une élite à travers le paramètre p_elite qu’il est possible de modifier depuis le main. 

##Les paramètres   

La majorité des paramètres sur lesquels peuvent influencer l'utilisateur se trouve dans la classe Main et sont les suivants :

* size_pop: la taille de la population
* target: la solution optimale sous la forme d'une chaîne de caractères composée de "0" et de "1", par exemple "111111", nous avons opté pour ce design plutôt que de confiner cette partie dans la fonction d'évaluation du fitness afin d'éviter de devoir fournir continuellement la taille des individus. La taille des individus sera automatiquement calquée sur celle de cette chaîne
* p_crossover: la probabilité de croisement sous sa forme décimale, par ex 0.7
* p_mutation: la probabilité de mutation sous sa forme décimale par ex 0.015
* p_elite: la pourcentage des meilleurs individus automatiquement retenus, ce paramètre peut très bien être fixé à 0. A fournir sous la forme décimale par exemple 0.10. Ce paramètre est facultatif, s’il n’est pas fourni par l’utilisateur, il sera fixé à 0.10 grâce à l’un des constructeurs de la classe Population
* nbre_generation: définit le nombre d’itérations de l’algorithme génétique.
* selection_method: la méthode de sélection, à choisir parmi Selection_Methods.tournoi, Selection_Methods.rang, Selection_Methods.roulette_proportionnelle dans le main. Tout comme le pourcentage d’élite, ce paramètre est facultatif est sera fixé à .tournoi s’il n’a pas été précisé par l’utilisateur. 

Les autres paramètres relatifs aux méthodes de sélection se trouve directement dans la fonction .selection() de la classe Population.
La méthode du tournoi dispose d'un second paramètre permettant de choisir le nombre d'individus qui combattent dans un même tournoi. Il est fixé à 5.

Notre méthode de crossover propose en paramètre le nombre de séparateurs. Par défaut ce paramètre est fixé à 2 à travers la ligne crossover(parent1, parent2, 2) située dans la fonction .generation() de la classe Population.
 
Les paramètres de la méthode .draw_with_transformation(), qui s’applique à un objet Individu, permettent de formatter l’affichage d’une solution. Le premier paramètre permet de définir le nombre de caractères après lequel il y aura un retour à la ligne. Le second paramètre permet de définir en quel caractère le 0 est transformé et le troisième paramètre permet de définir en quel caractère le 1 est transformé. 


Des informations additionnelles sur le fonctionnement de notre implémentation sont fournies en commentaires du code source.
