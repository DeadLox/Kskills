<?php
/*
 * Zenika - Mangeurs de Hot-dogs
 */
// Exemple
//$file = array(2, 4, 20, 8, 8, 1);
// Beaucoup de plats
$file = array(8,34,13,86,3,7,9,34,8,35,98,2,5,100,54,3,2,76,4,8,23,73,93,73,46,3,2,8,54,28,4,76,34,75,25,8);
// Un seul plat
//$file = array(4,1,99);
// Un seul membre
//$file = array(1,5,38,35,2,92,26);


$nbMembreTeam = $file[0];
$nbPlats = $file[1];
$plats = array_slice($file, 2);
// On tri le nombre de plats du plus grand au plus petit
rsort($plats);
$arrayTeam = createArrayTeam($nbMembreTeam);

echo "Nombre de membres dans l'équipe: ".$nbMembreTeam."<br/>";
echo "Nombre de plats: ".$nbPlats."<br/>";
echo "Nombre total de HotDogs: ".getTotalPlats($plats)."<br/>";

echo '<pre>';
var_dump($plats);
echo '</pre>';

foreach ($plats as $key => $nbHotDogs) {
	$member = getLightMember($arrayTeam);
	$arrayTeam[$member] += $nbHotDogs;
	echo "C'est le membre ".$member." qui va mangé le plat ".$key." avec ".$nbHotDogs." hotdogs [total du membre: ".$arrayTeam[$member]." ]<br/>";
}

echo '<pre>';
var_dump($arrayTeam);
echo '</pre>';

echo "<br/>Le temps minimum est ".max($arrayTeam);

/**
 * Retourne le membre de l'équipe ayant le moins mangé
 */
function getLightMember($arrayTeam){
	$member = 0;
	$less = null;

	foreach ($arrayTeam as $key => $value) {
		//echo "Less: ".$less." value: ".$value."<br/>";
		if (is_null($less)) {
			$less = $value;
		} else if ($value < $less) {
			$less = $value;
			$member = $key;
		}
	}
	return $member;
}

/**
 * Crée un tableau de valeur pour l'équipe
 */
function createArrayTeam($nbMembre){
	$arrayTeam = array();
	for ($i=0; $i < $nbMembre; $i++) { 
		$arrayTeam[$i] = 0;
	}
	return $arrayTeam;
}

/**
 * Retourne le nombre total de HotDogs
 */
function getTotalPlats($platsArray){
	$nbHotDogs = 0;
	foreach ($platsArray as $key => $nb) {
		$nbHotDogs += $nb;
	}
	return $nbHotDogs;
}
?>
<!doctype html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>Zenika</title>
</head>
<body>
	
</body>
</html>