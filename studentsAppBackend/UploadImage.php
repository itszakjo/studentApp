<?php

define('DB_HOST','localhost');
define('DB_USER','root');
define('DB_PASS','');
define('DB_NAME','studentsapp');

define('UPLOAD_PATH', 'upload/');

$conn = new mysqli(DB_HOST,DB_USER,DB_PASS,DB_NAME) or die('Unable to connect');


$response = array();
$studentID = $_POST['student_id'];


    if(isset($_FILES['pic']['name'])){

        try{
            move_uploaded_file($_FILES['pic']['tmp_name'], UPLOAD_PATH . $_FILES['pic']['name']);

            $imgname = $_FILES["pic"]["name"];
            $imagePath = "upload/$imgname";

            $stmt = $conn->prepare("UPDATE `student` SET `st_image`=? WHERE st_id= ?");
            $stmt->bind_param("ss",$imagePath,$studentID);
            if($stmt->execute()){
                $response['error'] = false;
                $response['message'] = 'File uploaded successfully';
            }else{
                throw new Exception("Could not upload file");
            }
        }catch(Exception $e){
            $response['error'] = true;
            $response['message'] = 'Could not upload file';
        }

    }else{
        $response['error'] = true;
        $response['message'] = "Required params not available";
    }

echo json_encode($response);
?>