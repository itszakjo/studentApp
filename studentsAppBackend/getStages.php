<?php



require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();





        $stages= $db->getStages();

        $response['error'] = false;
        $response['message'] = "data";

        // add this as well to decode arabic
        echo json_encode($stages , JSON_UNESCAPED_UNICODE);




echo json_encode($response);











?>