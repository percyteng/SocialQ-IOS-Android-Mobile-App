<?php

/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
$comments = "Comments: ";
$category = "exchange";
$response = array();
if (isset($_POST['username'])&&isset($_POST['cost'])&&isset($_POST['notes'])&&isset($_POST['item'])&&isset($_POST['school'])) {
    $username = $_POST['username'];
    $cost = $_POST['cost'];
    $notes = $_POST['notes'];
    $item = $_POST['item'];
    $school = $_POST['school'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{
        // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();
        $checkAct = $db->prepare("SELECT * FROM selling WHERE username = :username AND price = :cost AND notes = :notes AND item = :item AND school = :school");
        $checkAct->bindParam(":username", $username);
        $checkAct->bindParam(":cost", $cost);
        $checkAct->bindParam(":school", $school);
        $checkAct->bindParam(":item", $item);
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

            $result = $db->prepare("INSERT INTO selling(item, price, notes, comments, username, school) VALUES(:item, :cost, :notes, :comments, :username, :school)");
            // binding parameters for mysql insertion
            $result->bindParam(":item", $item);
            $result->bindParam(":username", $username);
            $result->bindParam(":cost", $cost);
            $result->bindParam(":comments", $comments);
            $result->bindParam(":school", $school);
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
                    $result = $db->prepare("INSERT INTO allposts(category, item, price, notes, comments, username, school) VALUES(:category, :item, :cost, :notes, :comments, :username, :school)");
                    // binding parameters for mysql insertion
                    $result->bindParam(":category", $category);
                    $result->bindParam(":item", $item);
                    $result->bindParam(":username", $username);
                    $result->bindParam(":cost", $cost);
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