<?php


require_once 'DBOpreations.php';


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){



    if(

        isset($_POST['phone']) and
        isset($_POST['service']) and
        isset($_POST['refsalon']) and
        isset($_POST['day']) and
        isset($_POST['client_name'])

    ){

        $db = new DBOpreations();

        if($db->createOrder(

            $_POST['phone'],
            $_POST['service'],
            $_POST['refsalon'] ,
            $_POST['day'] ,
            $_POST['time'],
            $_POST['client_name']

        )){
            $response['error']= false ;
            $response['message']= "Ordered Successfully " ;

        }else{
            $response['error']= true;
            $response['message']= "Some Error Occured !!  " ;


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