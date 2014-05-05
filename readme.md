# Manuel d'utilisation
Nous avons opte. Ce choix a pour avantage d\'eviter de devoir, le code se base directement sur la taille de l\'array

3 méthodes de sélection ont été implémentées, chacun supporte également la sélection de l'élite à travers le paramètre p_elite

Les différents paramètres sur lesquels peuvent influencer l'utilisateur se trouve dans la class main est sont les suivants :
* size_pop: la taille de la population
* target: la solution optimale sous la forme d\'une chaîne de caractères composée de "0" et "1", par exemple "111111", nous avons opté pour ce design plutôt que de confiner cette partie dans la fonction d\'évaluation du fitness afin d\'éviter de devoir fournir continuellement la taille des individus
* p_crossover: la probabilité de croissement sous la forme décimale par ex 0.7 pour 70%
* p_mutation: la probabilité de mutation sous sa forme décmiale par ex 0.005
* p_elite: la pourcentage des meilleurs individus automatiquement retenus, ce paramètre peut très bien être fixé à 0
* nbre_generation: le nombre de génération
* selection_method: la méthode de sélection, à choisir parmi Selection_Methods.tournoi, Selection_Methods.rang, Selection_Methods.roulette_proportionnelle



