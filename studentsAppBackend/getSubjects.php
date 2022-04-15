<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();



if(isset($_POST['stage_id'])) {


    $stage_id = $_POST['stage_id'];

    $subjects = $db->getSubjects($stage_id);

    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($subjects , JSON_UNESCAPED_UNICODE);

}
else{

    $response['message'] = "required fileds missing!";

}
echo json_encode($response);











?>