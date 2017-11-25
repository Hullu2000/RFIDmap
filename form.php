<html>
	<body>
		<form action="./login.php" method="post">
			Username: <input type="text" name="username"><br>
			Password: <input type="text" name="password"><br>
			E-mail: <input type="text" name="email"><br>
			Phone: <input type="text" name="phone"><br>
			<input type="hidden" name="action" value="create">
			<input type="submit" value="Create">
		</form>
		<form action="./login.php" method="post">
			Username: <input type="text" name="username"><br>
			Password: <input type="text" name="password"><br>
			<input type="hidden" name="action" value="login">
			<input type="submit" value="Login">
		</form>
		<form action="./auth.php" method="post">
			Token: <input type="text" name="token"><br>
			<input type="submit" value="Get user">
		</form>
	</body>
</html>
