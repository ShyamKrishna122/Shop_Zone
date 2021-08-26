<?php
require "DataBase.php";
$db = new DataBase();
    if ($db->dbConnect()) {
        echo $db->getProducts();
        
    } else echo "Error: Database connection";

?>