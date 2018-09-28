<?php
	//$number = $numberErr = $numberOutput = "";
	if ($_SERVER["REQUEST_METHOD"] == "POST") {

		$name = test_input($_POST["name"]);
		$args = "";
		if (!empty($_POST["args"])) {
			$args = test_input($_POST["args"]);
		}
		
		$output = shell_exec("java projects/java/" . $name . " " . $args . " 2>&1");
		echo $output;

		/*

		if (empty($_POST["number"])) {
			$numberErr = "Number is required";
		} else {
			$number = test_input($_POST["number"]);
			$numberOutput = shell_exec("java CreditCardCheck " . $number . " 2>&1");
    // check if number only contains numbers
			if (!preg_match("/^[0-9]*$/", $number)) {
				$numberErr = "Only numbers allowed";
			}
		}
		*/

	}

	function test_input($data) {
		$data = trim($data);
		$data = stripslashes($data);
		$data = htmlspecialchars($data);
		return $data;
	}
	?>