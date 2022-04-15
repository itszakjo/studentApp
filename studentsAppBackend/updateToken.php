<?php


require_once 'DBOpreations.php';


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){



    if(

        isset($_POST['phone']) and
        isset($_POST['token']) and
        isset($_POST['isServerToken'])
    ){

        $db = new DBOpreations();

        if($db->insertToken(

            $_POST['phone'],
            $_POST['token'],
            $_POST['isServerToken']

        )){
            $response['error']= false ;
            $response['message']= "Done Updating Token Successfully" ;

        }else{
            $response['error']= true;
            $response['message']= "Token updating failed !!  " ;
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