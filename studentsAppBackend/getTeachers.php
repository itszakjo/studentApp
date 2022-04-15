<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();






if(isset($_POST['subject_id']) && isset($_POST['city_id']) ) {


    $subject_id = $_POST['subject_id'];
    $city_id = $_POST['city_id'];

    $Teachers = $db->getTeachers($subject_id ,$city_id);


    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($Teachers , JSON_UNESCAPED_UNICODE);

}
else{

    $response['message'] = "required fields are missing !";

}
echo json_encode($response);











?>