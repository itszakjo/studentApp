<?php


require_once 'DBOpreations.php';


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){



    if(

        isset($_POST['rate']) and
        isset($_POST['comment']) and
        isset($_POST['refsalon']) and
        isset($_POST['client'])

    ){

        $db = new DBOpreations();

        if($db->postRate(

            $_POST['rate'],
            $_POST['comment'],
            $_POST['refsalon'],
            $_POST['client']

        )){
            $response['error']= false ;
            $response['message']= "Rated Successfully " ;

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