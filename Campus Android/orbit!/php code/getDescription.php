<?php

/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */

$response = array();
if (isset($_POST['username'])) {
    $username = $_POST['username'];
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{
        // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();
        // mysql inserting a new row
        $result = $db->prepare("SELECT description FROM users WHERE name = :username");
        // binding parameters for mysql insertion
        $result->bindParam(":username", $username);
        // mysql inserting a new row with prepared and binded statements
        $result->execute();
        // echo json_encode($username);
        // check if row inserted or not
        if (!empty($result)) {
            // check for empty result
            if ($result->rowCount() > 0) {
                $result = $result->fetch();
                // $user = array();
                // $user["useremail"] = $result["useremail"];
                // $user["password"] = $result["password"];
                // // success
                $response["success"] = 1;
                $response["message"] = "Successfully found the user";
                // user node
                $response["user"] = $result["description"];
                // array_push($response["user"], $user);
                // echoing JSON response
                echo json_encode($response);
            }
         else {
            // failed to insert row
            $response["success"] = 0;
            $response["message"] = "Oops! An error occurred.";
            
            // echoing JSON response
            echo json_encode($response);
        }
    }
    } catch (PDOException $e){
        print "Sorry, a database error occurred. Please try again later.\n";
        print $e->getMessage();
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    // echoing JSON response
    echo json_encode($response);
}
?>