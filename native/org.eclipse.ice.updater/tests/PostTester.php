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

//The output filename for the post test
$output_file = "PostTest.txt";

//Delete the test output file if it exists
if(file_exists($output_file)){
	unlink($output_file);
}

//If POST is set and our "post" key exists
if(isset($_POST) && array_key_exists("post", $_POST)){

	//Append the post contents to the output file
	file_put_contents($output_file, $_POST["post"], FILE_APPEND);

	//Change group to users
	chgrp($output_file, "users");

	//Change permissions to rw-rw----
	chmod($output_file, 0660);
}
?>
