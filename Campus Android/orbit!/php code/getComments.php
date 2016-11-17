<?php

/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */

$response = array();
if (isset($_POST['username']) && isset($_POST['cost']) && isset($_POST['notes']) && isset($_POST['category'])) {
    $username = $_POST['username'];
    $cost = $_POST['cost'];
    $notes = $_POST['notes'];
    $category = $_POST['category'];
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{

        // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();

        if($category == "Rideshare"){
            $result = $db->prepare("SELECT comments FROM carpool WHERE username = :username AND price = :cost AND notes = :notes");
        }
        else if($category == "Sublet"){
            $result = $db->prepare("SELECT comments FROM subletting WHERE username = :username AND price = :cost AND notes = :notes");
        }
        else if($category == "exchange"){
            $result = $db->prepare("SELECT comments FROM selling WHERE username = :username AND price = :cost AND notes = :notes");  
        }
        else if($category == "Sports"){
            $result = $db->prepare("SELECT comments FROM sports WHERE username = :username AND type = :cost AND notes = :notes"); 
        }
        else if($category == "Tutors"){
            $result = $db->prepare("SELECT comments FROM tutor WHERE username = :username AND price = :cost AND notes = :notes"); 
        }
        else if($category == "Services"){
            $result = $db->prepare("SELECT comments FROM service WHERE username = :username AND price = :cost AND notes = :notes"); 
        }
        else{
            $result = $db->prepare("SELECT comments FROM events WHERE username = :username AND price = :cost AND notes = :notes"); 
        }
        // binding parameters for mysql insertion
        $result->bindParam(":username", $username);
        $result->bindParam(":cost", $cost);
        $result->bindParam(":notes", $notes);
        // mysql inserting a new row with prepared and binded statements
        $result->execute();
        if (!empty($result)) {
            // check for empty result
                $result = $result->fetch();
                // $user = array();
                // $user["useremail"] = $result["useremail"];
                // $user["password"] = $result["password"];
                // // success
                $response["success"] = 1;
                $response["message"] = "yes";
                // user node
                $response["result"] = $result["comments"];
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