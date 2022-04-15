<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();










if(  isset($_POST['subject_id']) and isset($_POST['teacher_id'])) {




    $groups = $db->getGroups($_POST['subject_id'],$_POST['teacher_id']);

    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($groups ,JSON_UNESCAPED_UNICODE);

}

else{

    $response['message'] = "required fields are missing!";

}
echo json_encode($response);











?>