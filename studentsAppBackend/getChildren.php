<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();



if(isset($_POST['parent_phone'])) {

    $parent_phone = $_POST['parent_phone'];

    $children = $db->getChildren($parent_phone);

    $response['error'] = false;
    $response['message'] = "data";

    echo json_encode($children , JSON_UNESCAPED_UNICODE);

}
else{

    $response['message'] = "required fields missing!";

}
echo json_encode($response);











?>