<?php

/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */

$response = array();

if (isset($_POST['username'])&& isset($_POST['cost'])&& isset($_POST['notes']) && isset($_POST['newComment'])) {

    $newComment = $_POST['newComment'];
    $username = $_POST['username'];
    $notes = $_POST['notes'];
    $cost = $_POST['cost'];
//     // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{
//         // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();
        $result = $db->prepare("UPDATE carpool SET comments = concat(ifnull(comments,:newComment),:newComment) WHERE username = :username AND price = :cost AND notes = :notes");

        $result->bindParam(":username", $username);
        $result->bindParam(":cost", $cost);
        $result->bindParam(":notes", $notes);
        $result->bindParam(":newComment", $newComment);
//         // // mysql executing a new row with prepared and binded statements
        $result->execute();
//         // check if row inserted or not
        if (!empty($result)) {
//         // check for empty result
            // if ($result->rowCount() > 0) {
                // successfully inserted into database
                $response["success"] = 1;
                $response["message"] = "Successfully commented";
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