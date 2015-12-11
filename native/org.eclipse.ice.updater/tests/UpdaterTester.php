<?php

/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Eric J. Lingerfelt, Alexander J. McCaskey
 *******************************************************************************/

//This is the output file for the test's name/value pairs in plain text
$output_file_text = "UpdaterTester.txt";

//This is the output file of the JSON posts separated by a new line character
$output_file_json = "UpdaterTester.json";

//If POST is set and our "post" key exists
if(isset($_POST) && array_key_exists("post", $_POST)){

	//Get the JSON string from POST
	$json = trim($_POST["post"]);

	//Get an associative array from the JSON string
	$array = json_decode($json, true);

	//Get the posts array from the JSON string array
	$post_array = $array["posts"];

	//Create a string variable to all name/value pairs extracted from the post array
	$post_name_value_pairs = "";

	//Cycle over all entries in the post array
	foreach($post_array as $post){

		//If updater initialized is called 
		//delete the output file if it exists
		if(trim($post["type"])=="UPDATER_STARTED"){
			if(file_exists($output_file_text)){
				unlink($output_file_text);
			}
			if(file_exists($output_file_json)){
				unlink($output_file_json);
			}
		}

		//Append each name/value pair to the name/value pairs string
		$post_name_value_pairs .= get_name_value_pair($post["type"], $post["message"]);
	}

	//Append the post contents to the text output file
	file_put_contents($output_file_text, $post_name_value_pairs, FILE_APPEND);

	//Append the post contents to the json output file
	file_put_contents($output_file_json, $json."\n\n", FILE_APPEND);

	//Change group to users
	chgrp($output_file_text, "users");

	//Change permissions to rw-rw----
	chmod($output_file_text, 0660);

	//Change group to users
	chgrp($output_file_json, "users");

	//Change permissions to rw-rw----
	chmod($output_file_json, 0660);

	//End the script
	exit();
}

/**
* This function creates a name/value pair ending with a new line character.
*
* @param $name 	the name
* @param $value the value
* @return 	the name/value pair
*/
function get_name_value_pair($name, $value){

	//Create and return the name/value pair
	return $name."=".$value."\n";
}

?>

