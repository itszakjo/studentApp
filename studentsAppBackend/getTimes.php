<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();

if(isset($_POST['refday'])) {


    $refday = $_POST['refday'];

    $times = $db->getTimes($refday);

    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($times);

}

else{

    $response['message'] = "required fields missing!";

}
echo json_encode($response);











?>