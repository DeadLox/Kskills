<?php
/*
 * Vous avez fait une belle rencontre sur Meetic et vous partagez avec votre âme soeur une passion pour le jeu des Chiffres en folie.

	Ce jeu est très simple.
	Le 1er joueur énonce un nombre entier positif N.

	Le 2ème joueur doit arriver en moins de 5 secondes à déterminer quel est le nombre M supérieur à N,  tel que M est le plus proche possible de N,  et que M doit utiliser exactement les mêmes chiffres que N.

	Afin de pouvoir déterminer le 2ème joueur a trouvé la meilleure solution, vous décidez de créer un programme permettant de calculer automatiquement le meilleur score possible.

	 

	 

	Entrée de la fonction
	1 ligne contenant un nombre entier positif N contenant entre 2 et 20 chiffres inclus.

	Sortie de la fonction
	Votre fonction doit retourner un nombre entier M tel que :

	M supérieur à N
	M est composé des mêmes chiffres que N
	M est le plus proche possible de N
 * 
 */

function main()
{
	$stdin = fopen('meetic.txt', 'r');
	$stdout = fopen('php://stdout', 'w');
	
	$num1 = trim(fgets($stdin));
	
	// Découpe le nombre
	$numTab = str_split($num1);
	// Génère la liste des combinaisons d'index possibles
	$listCombiIndex = generateAllCombinaisons(initListCombi(strlen($num1)), $num1);
	// Génère la liste des combinaisons grâce à la liste des combinaisons d'index
	$listCombi = generateListCombi($listCombiIndex, $numTab);
	// On recherche le nombre supérieur le plus proche et on l'affiche
	echo findBestNum($listCombi, intval($num1));
	
	fclose($stdout);
	fclose($stdin);
}

function dump($list){
	echo '<pre>';
	var_dump($list);
	echo '</pre>';
}

	
/**
 * Permet de trouver la meilleur combinaison parmi la liste
 * 
 * @param listCombiIndex
 * @param num1
 * @return
 */
function findBestNum($listCombiIndex, $num1){
	$bestNum = 0;
	// On initialise l'écart minimum avec la valeur négative du nombre d'origine
	$ecartMin = -$num1;
	// On parcours les combinaisons possibles
	foreach ($listCombiIndex as $combi) {
		$combiNum = intval($combi);
		// Si la combinaison est différente du nombre d'origine
		if ($combiNum != $num1) {
			// On calcul l'écart entre la combinaison et le nombre d'origine
			$ecart = $num1-$combiNum;
			// Si l'écart est inférieur à zéro et supérieur à l'écart minimum
			if ($ecart < 0 && $ecart > $ecartMin) {
				$ecartMin = $ecart;
				$bestNum = $combiNum;
			}
		}
		
	}
	return $bestNum;
}

/**
 * Génère la liste des combinaisons de nombres possible à partir de la liste des combinaisons d'index
 * 
 * @param listCombiIndex
 * @param numTab
 * @return
 */
function generateListCombi($listCombiIndex, $numTab){
	$listeCombi = array();
	// On parcourt la liste des combinaisons d'index
	foreach ($listCombiIndex as $CombiIndex) {
		$combiArray = str_split($CombiIndex);
		$combi = "";
		// Pour chaque index, on récupère la valeur correspondante
		foreach ($combiArray as $char) {
			$index = intval($char);
			$combi .= $numTab[$index];
		}
		// On ajoute la combinaison à la liste
		$listeCombi[] = $combi;
	}
	return $listeCombi;
}

/**
 * Initialise la liste des combinaisons d'index
 * 
 * @param length
 * @return
 */
function initListCombi($length){
	$listCombi = array();
	for ($i=0; $i< $length; $i++) {
		$listCombi[] = $i;
	}
	return $listCombi;
}

/**
 * Génère la suite de la combinaison
 * 
 * @param numPart
 * @param length
 * @return
 */
function generateNext($numPart, $length){
	$combis = array();
	// On parcours la liste des indexs
	for ($i=0; $i<$length; $i++) {
		// Si le début de combinaison ne contient pas cet index
		if (strstr(strval($numPart), strval($i)) === false) {
			// On ajoute cette combinaison d'index à la liste
			$combis[] = $numPart . $i;
		}
	}
	return $combis;
}

/**
 * Génère la liste des combinaisons possibles
 * 
 * @param numTab
 * @return
 */
function generateAllCombinaisons($listCombi, $num1){
	$listCombiTemp = array();
	if (sizeof($listCombi) > 0) {
		for ($i=0; $i<sizeof($listCombi); $i++) {
			// On génère la liste des possibilités suivantes pour cette combinaison
			$listCombiNext = generateNext($listCombi[$i], strlen($num1));
			foreach ($listCombiNext as $combi) {
				$listCombiTemp[] = $combi;
			}
		}
	}
	// On vide la liste des combinaisons d'index
	$listCombi = $listCombiTemp;
	// Si la longueur des combinaisons n'est pas égale à la longueur du nombre recherché alors on recommence
	if (strlen($listCombi[0]) < strlen($num1)) {
		$listCombi = generateAllCombinaisons($listCombi, $num1);
	}
	return $listCombi;
}

main();
?>