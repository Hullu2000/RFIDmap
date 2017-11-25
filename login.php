<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$db = new SQLite3("kommunismivoittaa.db");

switch($_POST["action"])
{
case "create":
	$results = $db->query("SELECT * FROM users WHERE username = '" . $_POST["username"] . "';");
	if($results->fetchArray())
		die("Username taken.");

	if(!$_POST["username"])
		die("No username");

	if(!$_POST["password"])
		die("No password");

	$pwh = hash("sha256", $_POST["password"]);

	$db->exec("INSERT INTO users VALUES ('" . $_POST['username'] . "', '" . $pwh . "', '" . $_POST['email'] . "', '" . $_POST['phone'] . "');");

	echo "User created.";

case "login":
	$results = $db->query("SELECT * FROM users WHERE username = '" . $_POST["username"] . "';");
	$res = $results->fetchArray();
	if(!$res)
		die("User doesn't exist.");

	if($res[1] == hash("sha256", $_POST["password"]))
	{
		$token = hash("sha256", $_POST["username"] . time());
		$db->exec("INSERT INTO tokens VALUES ('" . $token . "', '" . $_POST["username"] . "');");
		echo "Your token: " . $token;
		setcookie("token", $token);
	}
	else
		die("Wrong password.");

	break;
}

?>
