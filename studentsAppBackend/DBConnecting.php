<?php






class DBConnect{



    private $con ;





    function __construct(){  }

    function connect(){

            $this->con= new mysqli('localhost' , 'root' , '' , 'studentsapp');

            // COL TYPE MUST BE UTF8_general_ci and add these to the database so u can read arabic in Json
            header('Content-Type: text/html; charset=utf-8' );
            mysqli_query($this->con  , "SET NAMES 'utf8mb4'");
            mysqli_query($this->con  ,"CHARACTER SET utf8mb4 COLLATE utf8_general_ci");

            if(mysqli_connect_errno()){

                 echo "Failed to connect with database".mysqli_connect_error();
            }

           return $this->con ;
    }
}

?>