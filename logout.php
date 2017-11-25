<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

$db = new SQLite3("kommunismivoittaa.db");

$results = $db->query("DELETE FROM tokens WHERE token = '" . $_POST["token"] . "';");
echo "Logged ouLogged out
