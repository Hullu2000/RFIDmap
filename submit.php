<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include_once "auth.php";

$db = new SQLite3("kommunismivoittaa.db");

$user = getUname();
echo $user;

switch($_POST["action"])
{
case "buy":
	$hash = hash("sha256", $user . time());
	$db->exec("INSERT INTO buying VALUES ('" . $hash . "', '" . $_POST["book"] . "', '" . $_POST["user"] . "', '" . $_POST["area"] . "');");

case "sell":

	break;
}

?>
