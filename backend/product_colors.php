<?php
require "DataBase.php";
$db = new DataBase();
    if ($db->dbConnect()) {
        echo $db->getProductColor($_POST['productId']);
        
    } else echo "Error: Database connection";

?>