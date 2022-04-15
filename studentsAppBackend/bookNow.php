<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST') {




    if (isset($_POST['student_id']) and isset($_POST['group_id']) ) {


        $student_id = $_POST['student_id'];
        $group_id = $_POST['group_id'];


        $info = $db->bookNow($student_id , $group_id);

        $response['error'] = false;

        $response['message'] = "Booked Successfully";

        echo json_encode($info);


    }else {

        $response['message'] = "Wrong Request";

    }


    echo json_encode($response);


}




?>