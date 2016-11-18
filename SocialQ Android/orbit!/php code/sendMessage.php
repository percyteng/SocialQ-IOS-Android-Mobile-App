<?php

/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
$ifread = "no";
$response = array();
if (isset($_POST['from'])&&isset($_POST['to'])&&isset($_POST['subject'])&&isset($_POST['content'])&&isset($_POST['time'])&&isset($_POST['cameFrom'])) {
    $textFrom = $_POST['from'];
    $textTo = $_POST['to'];
    $subject = $_POST['subject'];
    $content = $_POST['content'];
    $time = $_POST['time'];
    $cameFrom = $_POST['cameFrom'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{
        // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();
        $result = $db->prepare("INSERT INTO outbox(textFrom, message, textTo, subject, ifread, time, messageFrom) VALUES(:textFrom, :content, :textTo, :subject, :ifread, :time, :cameFrom)");
        // binding parameters for mysql insertion
        $result->bindParam(":textFrom", $textFrom);
        $result->bindParam(":content", $content);
        $result->bindParam(":textTo", $textTo);
        $result->bindParam(":subject", $subject);
        $result->bindParam(":ifread", $ifread);
        $result->bindParam(":time", $time);
        $result->bindParam(":cameFrom", $cameFrom);

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
    catch (PDOException $e){
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