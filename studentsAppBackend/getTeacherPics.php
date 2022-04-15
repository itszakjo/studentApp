<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();






if(isset($_POST['refteacher'])) {


    $refteacher = $_POST['refteacher'];

    $pics = $db->getTeacherPics($refteacher);


    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($pics);

}
else{

    $response['message'] = "hmmmm something's wrong!";

}
echo json_encode($response);











?>