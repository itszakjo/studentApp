<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();







    $days = $db->getDays();

    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($days);


echo json_encode($response);











?>