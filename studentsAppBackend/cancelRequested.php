<?php


require_once 'DBOpreations.php';


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){




        $db = new DBOpreations();

        $db->cancelRequested( $_POST['st_id']);



}else{



    $response['error'] = true ;
    $response['message'] = "Invaild Request" ;

}

echo json_encode($response);











?>