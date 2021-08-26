<?php
require "DataBase.php";
$db = new DataBase();
    if ($db->dbConnect()) {
        echo $db->getMyCartProducts($_POST['personId']);
        
    } else echo "Error: Database connection";

?>