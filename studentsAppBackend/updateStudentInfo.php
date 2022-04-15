<?php


require_once 'DBOpreations.php';


$response = array();


if($_SERVER['REQUEST_METHOD']=='POST'){

    if(

        isset($_POST['name']) and
        isset($_POST['gender']) and
        isset($_POST['address']) and
        isset($_POST['photo']) and
        isset($_POST['homePhoneReg']) and
        isset($_POST['studentPhone']) and
        isset($_POST['email']) and
        isset($_POST['socailLink']) and
        isset($_POST['school']) and
        isset($_POST['parentName']) and
        isset($_POST['parentJob']) and
        isset($_POST['parentPhone']) and
        isset($_POST['parentRelation']) and
        isset($_POST['notes']) and
        isset($_POST['id'])

    ){

        $db = new DBOpreations();

        if($db->updateStudentInfo(

            $_POST['name'] ,
            $_POST['gender'],
            $_POST['address'],
            $_POST['photo'],
            $_POST['homePhoneReg'] ,
            $_POST['studentPhone'],
            $_POST['email'],
            $_POST['socailLink'],
            $_POST['school'],
            $_POST['parentName'] ,
            $_POST['parentJob'],
            $_POST['parentPhone'] ,
            $_POST['parentRelation'],
            $_POST['notes'],
            $_POST['id']


        )){
            $response['error']= false ;
            $response['message']= "Updated Sucessfully " ;

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
    $response['message'] = "Invalid Request" ;

}

echo json_encode($response);











?>