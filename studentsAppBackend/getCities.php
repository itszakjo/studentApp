<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();






if(isset($_POST['refgov'])) {


    $refgov = $_POST['refgov'];

    $cities = $db->getCities($refgov);


    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($cities);

}
else{

    $response['message'] = "required fileds missing!";

}
echo json_encode($response);











?>