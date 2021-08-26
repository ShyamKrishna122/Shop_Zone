<?php
require "DataBase.php";
$db = new DataBase();
    if ($db->dbConnect()) {
        echo $db->addToCart($_POST['personId'],$_POST['productId']);
        
    } else echo "Error: Database connection";

?>