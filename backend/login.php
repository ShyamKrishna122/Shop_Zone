<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['personPhone']) && isset($_POST['personPassword'])) {
    if ($db->dbConnect()) {
        if ($db->logIn("person", $_POST['personPhone'], $_POST['personPassword'])) {
            echo "Login Success";
        } else echo "Username or Password wrong";
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
