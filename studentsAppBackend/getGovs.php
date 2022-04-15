<?php



require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();











$govs= $db->getGovs();

$response['error'] = false;
$response['message'] = "data";

echo json_encode($govs, JSON_UNESCAPED_UNICODE);

echo json_encode($response);











?>