<?php
/*
 * Following code will list all the activities
 */
// array for JSON response
$response = array();
// include db connect class
require_once __DIR__ . '/db_connect.php';
// connecting to db
try {
    $con = new DB_CONNECT();
    $db = $con->connect();
    $query = "SELECT * FROM outbox";
    // get all activities from products table
    $rows = $db->query($query);
    // check for empty result
    if ($rows->rowCount() > 0) {
        // looping through all results
        // products node
        $response["all"] = array();
        
        foreach ($rows as $row) {
            // temp user array
            $events = array();
            $events["fromText"] = $row["textFrom"];
            $events["toText"] = $row["textTo"];
            $events["subject"] = $row["subject"];
            $events["content"] = $row["message"];
            $events["ifRead"] = $row["ifread"];
            $events["time"] = $row["time"];
            $events["cameFrom"] = $row["messageFrom"];
            // push single activity into final response array
            array_push($response["all"], $events);
        }
        // success
        $response["success"] = 1;
        // echoing JSON response
        echo json_encode($response);
    } else {
        // no products found
        $response["success"] = 0;
        $response["message"] = "No activities found";
        // echo no users JSON
        echo json_encode($response);
    }
} catch (PDOException $ex){
    print "Sorry, a database error occurred. Please try again later.";
    print $ex->getMessage();
}
?>