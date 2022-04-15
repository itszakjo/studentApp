<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();


//if($_SERVER['REQUEST_METHOD']=='POST'){




//       $user = $db->getUserDataByUsername($member_id);
//       $membed = $user['id'];

//        $patients = $db->getPatientsDataby($membed);

        $diseases = $db->getResgisteredDisease();
//        $tests = $db->getTestsby();


        $response['error'] = false;
        $response['message'] = "data";


        echo json_encode($diseases);



//}else{
//
//
//
//    $response['error'] = true ;
//    $response['message'] = "hmmmm something's wrong!";
//}

echo json_encode($response);











?>