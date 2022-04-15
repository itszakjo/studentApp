<?php


require_once 'DBOpreations.php';


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){




        $db = new DBOpreations();

        $db->cancelOrder( $_POST['refsalon'],$_POST['time'], $_POST['day']);



}else{



    $response['error'] = true ;
    $response['message'] = "Invaild Request" ;

}

echo json_encode($response);











?>