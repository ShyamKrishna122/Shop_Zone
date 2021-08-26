<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['personId']) &&isset($_POST['personName']) && isset($_POST['personEmail']) && isset($_POST['personPhone']) && isset($_POST['personPassword'])  && isset($_POST['personAddress'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("person",$_POST['personId'], $_POST['personName'], $_POST['personEmail'], $_POST['personPhone'], $_POST['personPassword'],$_POST['personAddress'])) {
            echo "Sign Up Success";
        } else echo "Sign up Failed";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
