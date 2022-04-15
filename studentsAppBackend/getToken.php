<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();






if(isset($_POST['phone']) and isset($_POST['isServerToken'])) {


    $phone = $_POST['phone'];
    $isServerToken = $_POST['isServerToken'];

    $token = $db->getToken($phone,$isServerToken);


    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($token);

}
else{

    $response['message'] = "hmmmm something's wrong!";

}
echo json_encode($response);











?>