<?php
if ($_SERVER["REQUEST_METHOD"] == "POST") {
	$name = test_input($_POST["name"]);

	echo file_get_contents($name);

	/*
	$myfile = fopen($name, "r") or die("Unable to open file!");
	// Output one line until end-of-file
	while(!feof($myfile)) {
		echo htmlspecialchars(fgets($myfile) . "<br>";
	}
	fclose($myfile);
	*/
}

function test_input($data) {
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	return $data;
}
?>