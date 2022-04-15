<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){



    if(isset($_POST['email']) and isset($_POST['password'])){

        if($db->clientLogin($_POST['email'], $_POST['password'])){


            $client =  $db->getClientDataByUser($_POST['email']);

            $id = $client['id'] ;
            $phone = $client['phone'] ;
            $name = $client['name'] ;


            $response['error']= false ;

            $response['message']= "Logged in Successfully " ;

            // this is so important for goin to the next activity
            $response['id']= $client['id'] ;
            $response['name']= $client['name'] ;
            $response['phone']= $client['phone'] ;
            $response['email']= $client['email'] ;


        }

        else{

            $response['error']= true;
            $response['message']= "Invaild name or password !!  " ;


        }

    } else{

            $response['error']= true ;
            $response['message']= "Required Fields are missing" ;
        }

}else{



    $response['error'] = true ;
    $response['message'] = "Invaild Request" ;

}

echo json_encode($response);











?>