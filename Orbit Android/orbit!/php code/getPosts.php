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
    $query = "SELECT * FROM allposts";
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
            $events["category"] = $row["category"];
            if ($events["category"] == "Events" OR $events["category"] == "Tutors" OR $events["category"] == "Services"){
                $events["title"] = $row['title'];
            }
            $events["item"] = $row["item"];
            $events["username"] = $row["username"];
            $events["location"] = $row["location"];
            $events["school"] = $row["school"];
            $events["price"] = $row["price"];
            $events["notes"] = $row["notes"];
            $events["comments"] = $row["comments"];

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