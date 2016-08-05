<?php
/*
 * Following code will delete a product from table
 * A product is identified by product id (pid)
 */
// array for JSON response
$response = array();
// check for required fields
if (isset($_POST['from'])&&isset($_POST['to'])&&isset($_POST['subject'])&&isset($_POST['content'])) {
    $textFrom = $_POST['from'];
    $textTo = $_POST['to'];
    $subject = $_POST['subject'];
    $content = $_POST['content'];

    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{
        // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();
        $result = $db->prepare("DELETE FROM outbox WHERE textFrom = :textFrom AND textTo = :textTo AND subject = :subject AND message = :content");
        $result->bindParam(":textFrom", $textFrom);
        $result->bindParam(":textTo", $textTo);
        $result->bindParam(":subject", $subject);
        $result->bindParam(":content", $content);
        $result->execute();
             
        // check if row deleted or not
        if ($result->rowCount() > 0) {
            // successfully updated
                // successfully updated
            $response["success"] = 1;
            $response["message"] = "Post successfully deleted";
            // echoing JSON response
            echo json_encode($response);
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