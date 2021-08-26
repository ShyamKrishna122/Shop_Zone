<?php
require "DataBase.php";
$db = new DataBase();
    if ($db->dbConnect()) {
        echo $db->getProductSize($_POST['productId']);
        
    } else echo "Error: Database connection";

?>