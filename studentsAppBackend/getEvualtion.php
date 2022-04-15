<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();

$response = array();

if(isset($_POST['st_id'])) {

    $student_id = $_POST['st_id'];

    $evs = $db->getEvualtion($student_id);

    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($evs , JSON_UNESCAPED_UNICODE);

}
else{

    $response['message'] = "required fields missing!";

}
echo json_encode($response);











?>