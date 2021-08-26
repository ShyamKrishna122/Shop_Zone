<?php
require "DataBase.php";
$db = new DataBase();
    if ($db->dbConnect()) {
        echo $db->addToOrder($_POST['personId'],$_POST['date'],$_POST['price']);
        
    } else echo "Error: Database connection";

?>