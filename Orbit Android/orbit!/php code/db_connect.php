
<?php
/**
 * A class file to connect to database
 */
class DB_CONNECT {
    var $db;
    /**
     * Function to connect with database
     */
    function connect() {
        // import database connection variables
        require_once __DIR__ . '/db_config.php';
        // Connecting to mysql database
        $this->db = new PDO("mysql:dbname=".DB_DATABASE.";host=".DB_SERVER, DB_USER, DB_PASSWORD);
        // Allows an exception to be caught with PDO
        $this->db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
        // returning connection cursor
        return $this->db;
    }
    /**
     * Function to close db connection
     */
    function close() {
        // closing db connection
        $this->db = null;
    }
}
?>