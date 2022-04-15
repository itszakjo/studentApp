<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();






if(isset($_POST['refsalon'])) {


    $refsalon = $_POST['refsalon'];

    $Avg  = $db->getAvgRate($refsalon);


    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($Avg);

}
else{

    $response['message'] = "required fileds missing!";

}
echo json_encode($response);











?>