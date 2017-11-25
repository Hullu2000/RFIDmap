<?php

ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

function getToken()
{
	$c = $_COOKIE["token"];
	$p = $_POST["token"];

	if($c != "")
		return $c;
	else
		return $p;
}

function getUname()
{
	$db = new SQLite3("kommunismivoittaa.db");

	$results = $db->query("SELECT * FROM tokens WHERE token  = '" . getToken() . "';");
	$res = $results->fetchArray();
	return $res[1];

}

echo getUname();

?>
