<?php
/*
 * Following code will delete a product from table
 * A product is identified by product id (pid)
 */
// array for JSON response
$response = array();
// check for required fields
if (isset($_POST['category'])&&isset($_POST['name'])&&isset($_POST['location'])&&isset($_POST['price'])&&isset($_POST['notes'])&&isset($_POST['school'])) {
    $category = $_POST['category'];
    $name = $_POST['name'];
    $location = $_POST['location'];
    $price = $_POST['price'];
    $notes = $_POST['notes'];
    $school = $_POST['school'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{
        // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();
        
        if ($category == "Rideshare"){
            $result = $db->prepare("DELETE FROM carpool WHERE username = :name AND location = :location AND price = :price AND notes = :notes AND school = :school");
            $result->bindParam(":location", $location);
        }
        else if ($category == "exchange"){
            $result = $db->prepare("DELETE FROM selling WHERE username = :name AND item = :location AND price = :price AND notes = :notes AND school = :school");
            $result->bindParam(":location", $location);
        }
        else if ($category == "Sublet"){
            $result = $db->prepare("DELETE FROM subletting WHERE username = :name AND location = :location AND price = :price AND notes = :notes AND school = :school");
            $result->bindParam(":location", $location);
        }
        else if ($category == "Sports"){
            $result = $db->prepare("DELETE FROM sports WHERE username = :name AND location = :location AND type = :price AND notes = :notes AND school = :school");
            $result->bindParam(":location", $location);
        }
        else if ($category == "Tutors"){
            $result = $db->prepare("DELETE FROM tutor WHERE username = :name AND location = :location AND price = :price AND notes = :notes AND school = :school");
            $result->bindParam(":location", $location);
        }
        else if ($category == "Services"){
            $result = $db->prepare("DELETE FROM service WHERE username = :name AND price = :price AND notes = :notes AND school = :school");
        }
        else{
            $result = $db->prepare("DELETE FROM events WHERE username = :name AND location = :location AND price = :price AND notes = :notes AND school = :school");
            $result->bindParam(":location", $location);
        }
        $result->bindParam(":name", $name);
        $result->bindParam(":school", $school);
        $result->bindParam(":price", $price);
        $result->bindParam(":notes", $notes);
        $result->execute();
             
        // check if row deleted or not
        if ($result->rowCount() > 0) {
            // successfully updated
            if ($category == "exchange"){
                $result = $db->prepare("DELETE FROM allposts WHERE username = :name AND item = :location AND price = :price AND notes = :notes AND category = :category AND school = :school");
                $result->bindParam(":location", $location);
            }
            else if ($category == "Services"){
                $result = $db->prepare("DELETE FROM allposts WHERE username = :name AND price = :price AND notes = :notes AND category = :category AND school = :school");
            }
            else{
                $result = $db->prepare("DELETE FROM allposts WHERE username = :name AND location = :location AND price = :price AND category = :category AND notes = :notes AND school = :school");
                $result->bindParam(":location", $location);
            }            
            $result->bindParam(":category", $category);
            $result->bindParam(":name", $name);
            $result->bindParam(":school", $school);
            $result->bindParam(":price", $price);
            $result->bindParam(":notes", $notes);
            $result->execute();
                 
            // check if row deleted or not
            if ($result->rowCount() > 0) {
                // successfully updated
                $response["success"] = 1;
                $response["message"] = "Post successfully deleted";
                // echoing JSON response
                echo json_encode($response);
            } else {
                // no product found
                $response["success"] = 0;
                $response["message"] = "No activity found";
                // echo no users JSON
                echo json_encode($response);
            }
        } else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No activities found";
            // echo no users JSON
            echo json_encode($response);
        }
    } catch (PDOException $e){
        // no product found
        $response["success"] = 0;
        $response["message"] = "An error occurred!";
        // echoing JSON response
        echo json_encode($response);
        echo "A database error occurred";
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
    // echoing JSON response
    echo json_encode($response);
}
?>