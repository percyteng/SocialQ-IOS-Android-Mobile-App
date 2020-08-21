<?php

/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
$comments = "Comments: ";
$category = 'Sports';
$response = array();
if (isset($_POST['username'])&&isset($_POST['location'])&&isset($_POST['type'])&&isset($_POST['notes'])&&isset($_POST['school'])) {
    $username = $_POST['username'];
    $location = $_POST['location'];
    $type = $_POST['type'];
    $school = $_POST['school'];
    $notes = $_POST['notes'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{
        // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();
        $checkAct = $db->prepare("SELECT * FROM sports WHERE username = :username AND location = :location AND type = :type AND notes = :notes AND school = :school");
        $checkAct->bindParam(":username", $username);
        $checkAct->bindParam(":location", $location);
        $checkAct->bindParam(":type", $type);
        $checkAct->bindParam(":school", $school);
        $checkAct->bindParam(":notes", $notes);
        $checkAct->execute();
        if ($checkAct->rowCount() > 0) {
            // successfully inserted into database
            $responses["success"] = 0;
            $responses["message"] = "This post already exists, you can not create an exactly the same one";
            // echoing JSON response
            echo json_encode($responses);
        }
        else{
            $result = $db->prepare("INSERT INTO sports(location, type, notes, comments, username, school) VALUES(:location, :type, :notes, :comments, :username, :school)");
            // binding parameters for mysql insertion
            $result->bindParam(":username", $username);
            $result->bindParam(":location", $location);
            $result->bindParam(":type", $type);
            $result->bindParam(":school", $school);
            $result->bindParam(":notes", $notes);
            $result->bindParam(":comments", $comments);
            // mysql inserting a new row with prepared and binded statements
            $result->execute();
            if (!empty($result)) {
                // check for empty result
                if ($result->rowCount() > 0) {
                    // successfully inserted into database
                    // echoing JSON response
                    $result = $db->prepare("INSERT INTO allposts(category, location, price, notes, comments, username, school) VALUES(:category, :location, :type, :notes, :comments, :username, :school)");
                    // binding parameters for mysql insertion
                    $result->bindParam(":category", $category);
                    $result->bindParam(":username", $username);
                    $result->bindParam(":location", $location);
                    $result->bindParam(":type", $type);
                    $result->bindParam(":school", $school);
                    $result->bindParam(":comments", $comments);
                    $result->bindParam(":notes", $notes);
                    // mysql inserting a new row with prepared and binded statements
                    $result->execute();
                    if (!empty($result)) {
                        // check for empty result
                        if ($result->rowCount() > 0) {
                            // successfully inserted into database
                            $response["success"] = 1;
                            $response["message"] = "post successfully created";
                            // echoing JSON response
                            echo json_encode($response);
                        }
                        else{
                            $response["success"] = 0;
                            $response["message"] = "Oops! An error occurred.";
                            
                            // echoing JSON response
                            echo json_encode($response);
                        }
                    }
                    else {
                        // failed to insert row
                        $response["success"] = 0;
                        $response["message"] = "Oops! An error occurred.";
                        
                        // echoing JSON response
                        echo json_encode($response);
                    }
                }
                else{
                    $response["success"] = 0;
                    $response["message"] = "Oops! An error occurred.";
                    
                    // echoing JSON response
                    echo json_encode($response);
                }
            }
            else {
                // failed to insert row
                $response["success"] = 0;
                $response["message"] = "Oops! An error occurred.";
                
                // echoing JSON response
                echo json_encode($response);
            }
        } 
    }
    catch (PDOException $e){
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