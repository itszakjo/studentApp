<?php


require_once 'DBOpreations.php';


$response = array();


//if($_SERVER['REQUEST_METHOD']=='POST'){
//
//
//
//    if(
//
//         isset($_POST['patientname']) and
//         isset($_POST['gender']) and
//         isset($_POST['Age'])and
//         isset($_POST['agetype']) and
//         isset($_POST['tel']) and
//         isset($_POST['phone']) and
//         isset($_POST['notes']) and
//         isset($_POST['total']) and
//         isset($_POST['refmember']) and
//         isset($_POST['testname']) and
//         isset($_POST['testnotes']) and
//         isset($_POST['testno']) and
//         isset($_POST['testprice'])
//
//        ){
//
//        $db = new DBOpreations();
//
//        if($db->addPatient(
//
//            $_POST['patientname'] ,
//            $_POST['gender'],
//            $_POST['Age'],
//            $_POST['agetype'],
//            $_POST['tel'],
//            $_POST['phone'],
//            $_POST['notes'],
//            $_POST['total'],
//            $_POST['refmember'],
//            $_POST['testname'] ,
//            $_POST['testno'],
//            $_POST['testprice'],
//            $_POST['testnotes']
//
//
//        )){
//
//            $response['error']= false ;
//            $response['message']= "Data Added Successfully " ;
//
//        }else{
//
//            // this opreation's canceled , only uisng its message
//            $response['error']= true ;
//            $response['message']= "Data Added Successfully " ;
//        }
//
//    } else{
//
//            $response['error']= true ;
//            $response['message']= "Required Fields are missing" ;
//        }
//
//}else{
//
//
//
//    $response['error'] = true ;
//    $response['message'] = "Invaild Request" ;
//



if($_SERVER['REQUEST_METHOD']=='POST'){



    $recievedArray = $_POST['array'];

    $newArray = json_decode($recievedArray,true);

    if(
        isset($_POST['refday']) and
        isset($_POST['refsalon']) and
        isset($newArray)

    ){
        $db = new DBOpreations();

        if($db->addTime(

            $_POST['refday'],
            $_POST['refsalon'],
            $newArray

        )){

            $response['error']= false;
            $response['message']= "Data Added Successfully yad " ;

        }else{

            // this operation's canceled , only using its message
            $response['error']= true ;
            $response['message']= "Error !" ;
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