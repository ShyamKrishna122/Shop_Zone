<?php
require "DataBase.php";
$db = new DataBase();
    if ($db->dbConnect()) {
        echo $db->getOrder($_POST['personId']);
        
    } else echo "Error: Database connection";

?>