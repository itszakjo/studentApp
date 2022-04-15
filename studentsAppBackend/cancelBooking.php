<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST') {


    if (isset($_POST['id'])) {


        $id = $_POST['id'];


        $info = $db->cancelBookin($id);

        $response['error'] = false;

        $response['message'] = "Canceled Successfully";

        echo json_encode($info);


    }else {

        $response['message'] = "Wrong Request";

    }


    echo json_encode($response);


}




?>