<?php
/*
 * Following code will update a product information
 * A product is identified by product id (pid)
 */
// array for JSON response
// $oldusername = "percy";
// $username = "pgod,";
// $tmp = "percy, is so cool";
// $newJoined = str_replace($oldusername.",", $username, $tmp);
// echo $newJoined;
// check for required fields
if (isset($_POST['username']) && isset($_POST['userintro'])) {
    
    $username = $_POST['username'];
    $userintro = $_POST['userintro'];
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try {
        // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();

        $result = $db->prepare("UPDATE users SET description = :userintro WHERE name = :username");

        $result->bindParam(":userintro", $userintro);
        $result->bindParam(":username", $username);
        $result->execute();
        // check if row inserted or not
        if ($result) {
            // successfully updated
            $responses["success"] = 1;
            $responses["message"] = "information successfully updated.";
            
            // echoing JSON response
            echo json_encode($responses);
        }
        else{
            $responses["success"] = 0;
            $responses["message"] = "Naw";
            
            // echoing JSON response
            echo json_encode($responses);
        }
        // mysql update row with matched pid
    } catch(PDOException $e){
        print "Sorry, a database error occurred. Please try again later";
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