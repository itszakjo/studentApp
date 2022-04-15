<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();






if(isset($_POST['st_id'])) {


    $st_id = $_POST['st_id'];

    $Requests = $db->getRequests($st_id);


    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($Requests , JSON_UNESCAPED_UNICODE);

}
else{

    $response['message'] = "required fields are missing !";

}
echo json_encode($response);











?>