<?php

/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */

$response = array();

if (isset($_POST['newPass'])&&isset($_POST['username'])) {
    $username = $_POST['username'];
    $newPass = $_POST['newPass'];
//     // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{
//         // connecting to db

        $con = new DB_CONNECT();
        $db = $con->connect();

//         // mysql inserting a new row
        $result = $db->prepare("UPDATE users SET password = :newPass WHERE name = :username");
//         // binding parameters for mysql insertion
        $result->bindParam(":newPass", $newPass);
        $result->bindParam(":username", $username);

//         // // mysql executing a new row with prepared and binded statements
        $result->execute();

//         // check if row inserted or not
        if (!empty($result)) {
//         // check for empty result
            // if ($result->rowCount() > 0) {
                // successfully inserted into database
                $response["success"] = 1;
                $response["message"] = "Successfully changed password";
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
//         }
    } catch (PDOException $e){
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
            
            // echoing JSON response
        echo json_encode($response);
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