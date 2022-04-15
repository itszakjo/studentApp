<?php



require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();





        $refgov = $_POST['refgov'];

        $countries= $db->getCountries($refgov);

        $response['error'] = false;
        $response['message'] = "data";

        echo json_encode($countries, JSON_UNESCAPED_UNICODE);




echo json_encode($response);











?>