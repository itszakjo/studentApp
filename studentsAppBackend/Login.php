<?php


require_once 'DBOpreations.php';

$db = new DBOpreations();


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){



    if(isset($_POST['st_mobile']))  {

        if($db->login($_POST['st_mobile'])){

            $student =  $db->getStudentDataByMobile($_POST['st_mobile']);

            $response['error']= false ;

            $response['message']= "Logged in Successfully " ;

//          this is so important for goin to the next activity


            $response['id']= $student['st_id'];
            $response['name']= $student['st_name'] ;
            $response['gender']= $student['st_gender'] ;
            $response['address']= $student['st_address'] ;
            $response['photo']= $student['st_image'] ;
            $response['homePhoneReg']= $student['st_telephone'] ;
            $response['studentPhone']= $student['st_mobile'] ;
            $response['email']= $student['st_email'] ;
            $response['socailLink']= $student['st_facebook'] ;
            $response['school']= $student['st_school'] ;
            $response['parentName']= $student['st_responsible_name'] ;
            $response['parentJob']= $student['st_responsible_job'] ;
            $response['parentPhone']= $student['st_responsible_relation'] ;
            $response['parentRelation']= $student['st_responsible_telephone'] ;
            $response['notes']= $student['notes'] ;
            $response['pass']= $student['password'] ;

        } else{

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