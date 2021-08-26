<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $phoneNo, $password)
    {
        $phoneNo = $this->prepareData($phoneNo);
        $password = $this->prepareData($password);
        $this->sql = "select * from " . $table . " where personPhone = '" . $phoneNo . "'";
        $result = mysqli_query($this->connect, $this->sql);
        $row = mysqli_fetch_assoc($result);
        if (mysqli_num_rows($result) != 0) {
            $dbusername = $row['personPhone'];
            $dbpassword = $row['personPassword'];
            if ($dbusername == $phoneNo && $password == $dbpassword) {
                $login = true;
            } else $login = false;
        } else $login = false;

        return $login;
    }

    function signUp($table,$id, $name,$email, $phoneNo, $password,$address)
    {
        $id = $this->prepareData($id);
        $name = $this->prepareData($name);
        $email = $this->prepareData($email);
        $phoneNo = $this->prepareData($phoneNo);
        $password = $this->prepareData($password);
        $address = $this->prepareData($address);
        // $password = password_hash($password);
        $this->sql =
            "INSERT INTO " . $table . " (personId,personName,personEmail,personPhone,personPassword,personAddress) VALUES ('" . $id . "','" . $name . "','" . $email . "','" . $phoneNo . "','" . $password . "','" . $address . "')";
        mysqli_query($this->connect, $this->sql);
        $this->sql =
            "INSERT INTO cart VALUES ('" . $id . "',0,0)";
        if (mysqli_query($this->connect, $this->sql)) {
            return true;
        } else return false;
    }

    function getProducts(){
        $response = array();
        $this->sql = "select * from products";
        $result = mysqli_query($this->connect, $this->sql);
        if(mysqli_num_rows($result)>0)
        {
            $response["products"] = array();

            while( $row = mysqli_fetch_array($result))
            {
                $product = array();
                $product["productId"] = $row["productId"];
                $product["productName"] = $row["productName"];
                $product["productDescription"] = $row["productDescription"];
                $product["productImage"] = $row["productImage"];
                $product["productCategory"] = $row["productCategory"];
                $product["productRating"] = $row["productRating"];
                $product["productPrice"] = $row["productPrice"];
                $product["productDiscount"] = $row["productDiscount"];
                $product["productWeight"] = $row["productWeight"];

                array_push($response["products"],$product);
            }
            $response["success"] = 1;
            return json_encode($response);
        }
        else{
            $response["success"] = 0;
            $response["message"] = "No products found";

            return json_encode($response);
        }
       

    }

    function getProductColor($productId){
        $response = array();
        $this->sql = "select * from product_colors where productId = '" . $productId . "'";
        $result = mysqli_query($this->connect, $this->sql);
        if(mysqli_num_rows($result)>0)
        {
            $response["productColors"] = array();

            while( $row = mysqli_fetch_array($result))
            {
                $productColors = array();
                $productColors["productId"] = $row["productId"];
                $productColors["color"] = $row["color"];
                
                array_push($response["productColors"],$productColors);
            }
            $response["success"] = 1;
            return json_encode($response);
        }
        else{
            $response["success"] = 0;
            $response["message"] = "No products found";

            return json_encode($response);
        }
       

    }


    function getProductSize($productId){
        $response = array();
        $this->sql = "select * from product_sizes where productId = '" . $productId . "'";
        $result = mysqli_query($this->connect, $this->sql);
        if(mysqli_num_rows($result)>0)
        {
            $response["productSizes"] = array();

            while( $row = mysqli_fetch_array($result))
            {
                $productSizes = array();
                $productSizes["productId"] = $row["productId"];
                $productSizes["size"] = $row["size"];
                
                array_push($response["productSizes"],$productSizes);
            }
            $response["success"] = 1;
            return json_encode($response);
        }
        else{
            $response["success"] = 0;
            $response["message"] = "No products found";

            return json_encode($response);
        }
       

    }

    function addToCart($personId,$productId){
        $personId = $this->prepareData($personId);
        $productId = $this->prepareData($productId);

        $isExist = $this->isProductExistInCart($personId,$productId);

        if(!$isExist){
            $this->sql = "INSERT INTO cart_products (personId,productId) VALUES ('" . $personId . "','" . $productId . "')";
            if (mysqli_query($this->connect, $this->sql)) {
                $this->sql = "select productPrice from products  where productId = '" . $productId . "'";
                if (mysqli_query($this->connect, $this->sql)){
                    $result = mysqli_query($this->connect, $this->sql);
                    $row = mysqli_fetch_array($result);
                    $quantity = $this->getQuantity($personId);
                    $price = $this->getPrice($personId,$row['productPrice']);

                    $this->sql = "UPDATE cart SET quantity = '$quantity'";
                    if (mysqli_query($this->connect, $this->sql)){
                        $this->sql = "UPDATE cart SET price = '$price'";
                        if (mysqli_query($this->connect, $this->sql)){
                            return "success"; 
                        }
                        return "Failed1";
                    }
                    
                    return "Failed2";
                }
                return "Failed3";
             } 
             else {

             return "Failed4";}
        

    }
}

    function getQuantity($personId){
        
        $personId = $this->prepareData($personId);
        $this->sql = "select * from cart where personId = '" . $personId . "' " ;
        $result = mysqli_query($this->connect, $this->sql);
       
        if(mysqli_num_rows($result) != 0){

            $row = mysqli_fetch_array($result);
            
            return $row["quantity"]+1;
        }
        else return 1;

    }

    function getPrice($personId,$price){
        $personId = $this->prepareData($personId);
        $this->sql = "select * from cart where personId = '" . $personId . "' " ;
        $result = mysqli_query($this->connect, $this->sql);
        if(mysqli_num_rows($result) != 0){
            $row = mysqli_fetch_array($result);
        
            return $row["price"]+$price;
        }
        else return 0;

    }

    function isProductExistInCart($personId,$productId){
        $personId = $this->prepareData($personId);
        $productId = $this->prepareData($productId);
        $this->sql = "select * from cart_products where productId = '" . $productId . "' and personId = '" . $personId . "'" ;
        $result = mysqli_query($this->connect, $this->sql);

        if (mysqli_query($this->connect, $this->sql)) {
            if(mysqli_num_rows($result)!= 0){
                return true;
            }
            else{
                return false;
            }
            
        }
        else false;
    }

    function getCart($personId)
    {
        $response = array();
        $this->sql = "select price,quantity from cart where personId = $personId";
        $result = mysqli_query($this->connect, $this->sql);
        if(mysqli_num_rows($result)>0)
        {
            $row = mysqli_fetch_array($result);

            $response["cart"] = array();

                $cart = array();
                $cart["price"] = $row["price"];
                $cart["quantity"] = $row["quantity"];

                array_push($response["cart"],$cart);

            $response["success"] = 1;
            return json_encode($response);
        }
        else{
            $response["success"] = 0;
            $response["message"] = "No products found";

            return json_encode($response);
        }
    }
    
    function getMyCartProducts($personId){
        $response = array();
        $this->sql = "select productId from cart_products where personId = '" . $personId . "'";
        $result = mysqli_query($this->connect, $this->sql);
        if(mysqli_num_rows($result)>0)
        {
            $response["cartItems"] = array();
            
            while( $row = mysqli_fetch_array($result))
            {
                $cartItems = array();
                $this->sql = "select productId,productName,productPrice,productImage from products where productId = '" . $row["productId"] . "'";
                $result1 = mysqli_query($this->connect, $this->sql);
                if(mysqli_num_rows($result1)>0)
                {
                    $row1 = mysqli_fetch_array($result1);
                
                    $cartItems["productId"] = $row1["productId"];
                    $cartItems["productName"] = $row1["productName"];
                    $cartItems["productPrice"] = $row1["productPrice"];
                    $cartItems["productImage"] = $row1["productImage"];

                    array_push($response["cartItems"],$cartItems);
                }
            }
            $response["success"] = 1;
            return json_encode($response);
        }
        else{
            $response["success"] = 0;
            $response["message"] = "No products found1";

            return json_encode($response);
        }
    }


    function addToOrder($personId,$date,$price){
        $personId = $this->prepareData($personId);

        $isExist = $this->isIdExistInOrder($personId);

        if(!$isExist){
            $this->sql = "select productId from cart_products where personId = '" . $personId . "'";
            if(mysqli_query($this->connect, $this->sql)){
                $result = mysqli_query($this->connect, $this->sql);
                $this->sql = "UPDATE cart SET quantity = 0,price = 0 where personId = '" . $personId . "'";
                mysqli_query($this->connect, $this->sql);
                if(mysqli_num_rows($result)>0)
                {
                    while( $row = mysqli_fetch_array($result))
                    {
                        $this->sql = "INSERT INTO order_details VALUES('". $personId ."','". $row['productId']."')";
                        mysqli_query($this->connect, $this->sql);
                        $this->sql = "DELETE FROM cart_products where personId = '". $personId ."' and productId = '". $row['productId']."'";
                        mysqli_query($this->connect, $this->sql);
                    }

                }
                else{
                    return "error1";
                }
            }
            else {
                return "error";
            }
            $this->sql = "INSERT INTO orders VALUES('" . $personId . "','" . $date . "','" . $price . "') ";
            if (mysqli_query($this->connect, $this->sql)){
                return "Success";
            }
            return "Failed3";

    }
}

    function isIdExistInOrder($personId){
        $personId = $this->prepareData($personId);
        $this->sql = "select * from orders where personId = '" . $personId . "' " ;
        $result = mysqli_query($this->connect, $this->sql);
        if (mysqli_query($this->connect, $this->sql)) {
            if(mysqli_num_rows($result) == 0){
                return false;
            }
            else{
                return true;
            }
            
        }
        else false;
    }


    function getOrder($personId)
    {
        $response = array();
        $this->sql = "select price,date from orders where personId = $personId";
        $result = mysqli_query($this->connect, $this->sql);
        if(mysqli_num_rows($result)>0)
        {
            $row = mysqli_fetch_array($result);

            $response["order"] = array();

                $cart = array();
                $cart["price"] = $row["price"];
                $cart["date"] = $row["date"];

                array_push($response["order"],$cart);

            $response["success"] = 1;
            return json_encode($response);
        }
        else{
            $response["success"] = 0;
            $response["message"] = "No products found";

            return json_encode($response);
        }
    }
    
    function getMyOrderProducts($personId){
        $response = array();
        $this->sql = "select productId from order_details where personId = '" . $personId . "'";
        $result = mysqli_query($this->connect, $this->sql);
        if(mysqli_num_rows($result)>0)
        {
            $response["orderItems"] = array();
            
            
            while( $row = mysqli_fetch_array($result))
            {
                $orderItems = array();
                $this->sql = "select productId,productName,productPrice,productImage from products where productId = '" . $row["productId"] . "'";
                $result1 = mysqli_query($this->connect, $this->sql);
                if(mysqli_num_rows($result1)>0)
                {
                    $row1 = mysqli_fetch_array($result1);
                
                    $orderItems["productId"] = $row1["productId"];
                    $orderItems["productName"] = $row1["productName"];
                    $orderItems["productPrice"] = $row1["productPrice"];
                    $orderItems["productImage"] = $row1["productImage"];

                    array_push($response["orderItems"],$orderItems);
                }
            }
            $response["success"] = 1;
            return json_encode($response);
        }
        else{
            $response["success"] = 0;
            $response["message"] = "No products found";

            return json_encode($response);
        }
    }


}

?>
