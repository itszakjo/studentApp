<?php


require_once 'DBOpreations.php';


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){



    if(

        isset($_POST['teacher_id']) and
        isset($_POST['message']) and
        isset($_POST['student_id'])

    ){

        $db = new DBOpreations();

        if($db->askTeacher(

            $_POST['teacher_id'] ,
            $_POST['message'] ,
            $_POST['student_id']


        )){
            $response['error']= false ;
            $response['message']= "Question sent " ;

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