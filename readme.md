# Manuel d'utilisation
3 méthodes de sélection ont été implémentées, chacune supporte également la sélection de l'élite à travers le paramètre p_elite

## Les parmètres
Les différents paramètres sur lesquels peuvent influencer l'utilisateur se trouve dans la class main et sont les suivants :
* size_pop: la taille de la population
* target: la solution optimale sous la forme d'une chaîne de caractères composée de "0" et de "1", par exemple "111111", nous avons opté pour ce design plutôt que de confiner cette partie dans la fonction d'évaluation du fitness afin d'éviter de devoir fournir continuellement la taille des individus. La taille des invidus sera automatiquement calquée sur celle de cette chaîne
* p_mutation: la probabilité de mutation sous sa forme décmiale par ex 0.005
* p_elite: la pourcentage des meilleurs individus automatiquement retenus, ce paramètre peut très bien être fixé à 0
* nbre_generation
* selection_method: la méthode de sélection, à choisir parmi Selection_Methods.tournoi, Selection_Methods.rang, Selection_Methods.roulette_proportionnelle

Les autres paramètres relatifs aux méthodes de sélection se trouve dans la fonction \.selection de la class Population
* la méthode du tournoi dispose d'un second paramètre permettant de choisir le nombre d'individus qui combattent par tournoi

 


