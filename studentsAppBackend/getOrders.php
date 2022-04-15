<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();






if(  isset($_POST['refsalon'])) {


    $refsalon = $_POST['refsalon'];

    $orders = $db->getOrders($refsalon);

    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($orders);

}

else{

    $response['message'] = "required fields missing!";

}
echo json_encode($response);











?>