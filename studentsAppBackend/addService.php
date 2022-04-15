<?php


require_once 'DBOpreations.php';


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){



    if(

        isset($_POST['service']) and
        isset($_POST['price']) and
        isset($_POST['refsalon'])

    ){

        $db = new DBOpreations();

        if($db->addService(

            $_POST['service'] ,
            $_POST['price'] ,
            $_POST['refsalon']


        )){
            $response['error']= false ;
            $response['message']= "Service Added Successfully " ;

        }else{
            $response['error']= true;
            $response['message']= "Some Error Occurred !!  " ;
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