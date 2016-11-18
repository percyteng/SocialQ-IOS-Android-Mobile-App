<?php

/*
 * Following code will get single product details
 * A product is identified by product id (pid)
 */
$response = array();
if (isset($_POST['useremail']) && isset($_POST['password'])&& isset($_POST['username']) && isset($_POST['image'])&& isset($_POST['school'])) {
    $useremail = $_POST['useremail'];
    $image = $_POST['image'];
    $school = $_POST['school'];
    $password = $_POST['password'];
    $username = $_POST['username'];
    $path = $username.".JPG";
    $actualpath = "http://percyteng.com/orbit/pictures".$path;
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
    try{
        // connecting to db
        $con = new DB_CONNECT();
        $db = $con->connect();
        // mysql inserting a new row
        $checkEmail = $db->prepare("SELECT email FROM users WHERE email = :useremail");

        // binding parameters for mysql insertion
        $checkEmail->bindParam(":useremail", $useremail);
        // mysql inserting a new row with prepared and binded statements
        $checkEmail->execute();

        // check if row inserted or not
        if ($checkEmail) {
            // check for empty result
            if ($checkEmail->rowCount() > 0) {
                $checkEmail = $checkEmail->fetch();
                $response["success"] = -1;
                $response["message"] = "This email has already been created, please enter another valid email";
                echo json_encode($response);
            }
            else {
                // failed to insert row
                $checkUsername = $db->prepare("SELECT name FROM users WHERE name = :username");
                $checkUsername->bindParam(":username", $username);   
                $checkUsername->execute();

                if($checkUsername){
                    if($checkUsername->rowCount()>0){
                        $checkUsername = $checkUsername->fetch();
                        $response["success"] = -1;
                        $response["message"] = "This user name has already been created, please enter another valid user name";
                        echo json_encode($response);
                        }
                        else{

                            $result = $db->prepare("INSERT INTO users(email, name, password, photo, school) VALUES(:useremail, :username, :password, :actualpath, :school)");
                            // binding parameters for mysql insertion
                            $result->bindParam(":useremail", $useremail);
                            $result->bindParam(":password", $password);
                            $result->bindParam(":actualpath", $actualpath);
                            $result->bindParam(":username", $username);
                            $result->bindParam(":school", $school);
                            // mysql inserting a new row with prepared and binded statements
                            $result->execute();

                            // check if row inserted or not
                            if ($result) {
                                if ($result->rowCount() > 0) {
                                    file_put_contents("pictures/".$path,base64_decode($image));
                                    // successfully inserted into database
                                    $response["success"] = 1;
                                    $response["message"] = "You have now registered! Enjoy the App!.";
                                    // echoing JSON response
                                    echo json_encode($response);
                                }
                                else{
                                    // failed to insert row
                                    $response["success"] = 0;
                                    $response["message"] = "Oops! An error occurred.";
                                    echo json_encode($response);
                                }
                            } else {
                                // failed to insert row
                                $response["success"] = 0;
                                $response["message"] = "Oops! An error occurred.";
                                echo json_encode($response);
                                }
                                // echoing JSON response

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
        else {
            // failed to insert row
            $response["success"] = 0;
            $response["message"] = "Oops! An error occurred.";
            
            // echoing JSON response
            echo json_encode($response);
        }   
    } catch (PDOException $e){
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
    $response["message"] = "Please upload an image, because that's more fun";
    // echoing JSON response
    echo json_encode($response);
}
?>