 <?php

 Class DBOpreations
    {


        private $con;

        function __construct()
        {

            require_once dirname(__FILE__) . '/DBConnecting.php';

            $db = new DBConnect();

            $this->con = $db->connect();

//                $date = date("m-d-Y", strtotime('-3 day'));
//
//                mysqli_query($this->con, "DELETE FROM days WHERE day < '".$date."'");
//


        }


        // Student App Functions
        function registerStudent($name, $gender, $address, $photo, $homePhoneReg, $studentPhone,
                                 $email, $socailLink, $school, $parentName, $parentJob,
                                 $parentPhone, $parentRelation, $notes)
        {


            $result = mysqli_query($this->con, "SELECT st_id FROM student WHERE st_mobile=$studentPhone");
            $row = mysqli_fetch_array($result);
            $id = $row['st_id'];


            $stmt = $this->con->prepare("INSERT INTO `student` ( 
                            `st_id` , 
                            `st_name` , `st_gender`  ,`st_address` ,
                              `st_image` ,`st_telephone` ,`st_mobile` , 
                              `st_email`, `st_facebook` ,
                              `st_school` , `st_responsible_name`  ,
                               `st_responsible_job` ,`st_responsible_telephone` ,
                               `st_responsible_relation`, `notes` ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
        
           ON DUPLICATE KEY UPDATE 
                                   `st_name`=? , `st_gender`=?  ,`st_address`=? , 
                                    `st_image`=? ,`st_telephone`=? ,
                                    `st_email`=?, `st_facebook`=? , 
                                    `st_school`=? , `st_responsible_name` =? , 
                                     `st_responsible_job`=? ,`st_responsible_telephone`=? ,
                                     `st_responsible_relation`=?, `notes`=? ;") or die($this->con->error);

            $stmt->bind_param("ssssssssssssssssssssssssssss",
                $id, $name, $gender, $address, $photo, $homePhoneReg, $studentPhone,
                $email, $socailLink, $school, $parentName, $parentJob,
                $parentPhone, $parentRelation, $notes,

                $name, $gender, $address, $photo, $homePhoneReg,
                $email, $socailLink, $school, $parentName, $parentJob,
                $parentPhone, $parentRelation, $notes);

            $result = $stmt->execute();
            $stmt->close();

            //  Check For Successful Storing
            if ($result) {

                return true;

            } else {

                return false;
            }


        }

        function login($mobile)
        {

//         $password = md5($pass);

            $stmt = $this->con->prepare("SELECT st_id FROM student WHERE st_mobile=?");
            $stmt->bind_param("s", $mobile);
            $stmt->execute();
            $stmt->store_result();

            return $stmt->num_rows > 0;

        }

        function getStudentDataByMobile($mobile)
        {

//                $password = md5($pass);

            $stmt = $this->con->prepare("SELECT * FROM student WHERE st_mobile=?");
            $stmt->bind_param("s", $mobile);
            $stmt->execute();
            return $stmt->get_result()->fetch_assoc();

        }

        function getGovs(){


            $result = $this->con->query("SELECT * FROM govs ");


            $data = array();
            while ($item = $result->fetch_assoc())

                $data[] = $item;


            return $data;


        }

        function getCountries($refgov)
        {

            $result = $this->con->query("SELECT * FROM countries WHERE ref_gov='" . $refgov . "'");

            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;

            return $data;


        }

        function getStages()
        {

            $result = $this->con->query("SELECT * FROM stages ");

            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;

            return $data;


        }

        function getSubjects($stage_id)
        {

            $result = $this->con->query("SELECT * FROM course WHERE stage_id='" . $stage_id . "'");


            while ($item = $result->fetch_assoc())

                $data[] = $item;


            return $data;


        }

        function getAttendence($student_id)
        {

            $result = $this->con->query("SELECT * FROM student_attendence WHERE st_id='" . $student_id . "'");

            $data = array();

            while ($item = $result->fetch_assoc())

                $data[] = $item;

            return $data;

        }

        function getTeachers($subject_id,$city_id)
        {


            $result = $this->con->query("SELECT * FROM groups LEFT JOIN teachers
                              ON groups.t_id = teachers.t_id
                              WHERE groups.c_id ='" . $subject_id . "'
                               WHERE teacher.city_id= '".$city_id."'");


            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;
            return $data;


        }

        function getAds($student_id)
        {


            $result = $this->con->query("SELECT * FROM advertisment LEFT JOIN registeration
                              ON registeration.group_id = advertisment.group_id
                              WHERE registeration.st_id ='" . $student_id . "' ");


            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;
            return $data;


        }

        function getEvualtion($student_id)
        {


            $result = $this->con->query("SELECT * FROM evaluation LEFT JOIN student_evaluation
                              ON student_evaluation.ev_id = evaluation.ev_id
                              WHERE student_evaluation.st_id ='" . $student_id . "' ");

            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;
            return $data;


        }

        function getTeacherPics($id)
        {

//                $password = md5($pass);


            $stmt = $this->con->prepare("SELECT * FROM teacher_pics WHERE refteacher=?");
            $stmt->bind_param("s", $id);
            $stmt->execute();
            return $stmt->get_result()->fetch_assoc();

        }

        function getAllTeachers()
        {


            $result = $this->con->query("SELECT * FROM teachers");

            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;
            return $data;


        }

        function getGroups($subject_id, $teacher_id)
        {


            $result = $this->con->query("SELECT * FROM groups 
                    WHERE c_id='" . $subject_id . "'
                    AND t_id= '" . $teacher_id . "'");

            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;
            return $data;


        } /* done web*/

        function askTeacher($teacher_id, $message, $student_id)
        {


            $stmt = $this->con->prepare("INSERT INTO `teacher_messages` (`teacher_id`, `message`, `student_id`  ) VALUES ( ? , ? , ? );");

            $stmt->bind_param("sss", $teacher_id, $message, $student_id);

            if ($stmt->execute()) {
                return true;
            } else {
                return false;
            }
        }

        function updateStudentInfo($name, $gender, $address, $photo, $homePhoneReg, $studentPhone, $email, $socailLink, $school, $parentName, $parentJob, $parentPhone, $parentRelation, $notes, $id)
        {

            $stmt = $this->con->prepare("UPDATE `student` SET `st_name`=? , `st_gender`=?  ,`st_address`=? , `st_image`=? ,`st_telephone`=? ,`st_mobile`=? , `st_email`=?, `st_facebook`=? , `st_school`=? , `st_responsible_name` =? , `st_responsible_job`=? ,`st_responsible_telephone`=? ,`st_responsible_relation`=?, `notes`=?  WHERE st_id=? ;");

            $stmt->bind_param("sssssssssssssss", $name, $gender, $address, $photo, $homePhoneReg, $studentPhone, $email, $socailLink, $school, $parentName, $parentJob, $parentPhone, $parentRelation, $notes, $id);

            $result = $stmt->execute();

            return $result;

        }

        function getRegisteredGroups($st_id)
        {

            $result = $this->con->query("SELECT * FROM registeration 
                              LEFT JOIN groups  ON registeration.group_id = groups.group_id 
                              LEFT JOIN course ON groups.c_id = course.c_id 
                              WHERE registeration.st_id ='" . $st_id . "' ");

            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;
            return $data;

        }

        function cancelRegisteration($st_id)
        {


            $sql = "DELETE FROM registeration WHERE st_id='" . $st_id . "'";

            if ($this->con->query($sql) === TRUE) {
                echo "Canceled !";
            } else {
                echo "Error deleting record: " . $this->con->error;
            }


        }

        function cancelRequested($st_id)
        {


            $sql = "DELETE FROM requests WHERE st_id='" . $st_id . "'";

            if ($this->con->query($sql) === TRUE) {
                echo "Canceled !";
            } else {
                echo "Error deleting record: " . $this->con->error;
            }


        }

        function bookNow($student_id, $group_id)
        {


            $stmt = $this->con->prepare("INSERT INTO `requests` (`st_id`, `group_id` ) VALUES ( ? , ? );");

            $stmt->bind_param("ss", $student_id, $group_id);

            if ($stmt->execute()) {
                return true;
            } else {
                return false;
            }

        }

        function getRequests($st_id)
        {

            $result = $this->con->query("SELECT * FROM requests 
                              LEFT JOIN groups ON requests.group_id = groups.group_id
                              LEFT JOIN course ON groups.c_id = course.c_id 
                              WHERE requests.st_id ='" . $st_id . "' ");

            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;
            return $data;


        }

        function getChildren($parent_phone)
        {

            $result = $this->con->query("SELECT * FROM student WHERE st_responsible_telephone='" . $parent_phone . "'");

            $data = [];

            while ($item = $result->fetch_assoc()) {

                $data[] = $item;
            }

            return $data;

        }

        function getCities($rfgov)
        {

            $result = $this->con->query("SELECT * FROM cities WHERE refgov='" . $rfgov . "'");


            while ($item = $result->fetch_assoc())

                $data[] = $item;


            return $data;


        }

        function getDays(){

            $result = $this->con->query("SELECT * FROM days ");

            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;

            return $data;

        }

        function insertToken($phone, $token, $isServerToken)
        {

            $stmt = $this->con->prepare("
                    INSERT INTO `token` (`id`, `phone`, `token`, `isServerToken`) VALUES (NULL,  ? , ? , ? )
            ON DUPLICATE KEY UPDATE token=?,isServerToken=?;") or die($this->con->error);

            $stmt->bind_param("sssss", $phone, $token, $isServerToken, $token, $isServerToken);
            $result = $stmt->execute();
            $stmt->close();

            //  Check For Successful Storing
            if ($result) {

                $stmt = $this->con->prepare("SELECT * FROM token WHERE phone=?");
                $stmt->bind_param("s", $phone);
                $stmt->execute();
                $user = $stmt->get_result()->fetch_assoc();
                $stmt->close();
                return $user;

            } else {

                return false;
            }

        }

        function getToken($phone, $isServerToken)
        {

//        $stmt = $this->con->prepare("SELECT * FROM token WHERE phone=? AND isServerToken=?") or die($this->con->error);
//        $stmt->bind_param("ss",$phone,$isServerToken);
//        $result = $stmt->execute();
//        $token = $stmt->get_result()->fetch_assoc();
//        $stmt->close();
//        return $token;
//


            $result = $this->con->query("SELECT * FROM token WHERE phone='" . $phone . "' and isServerToken='" . $isServerToken . "' ");

            $data = array();

            while ($item = $result->fetch_assoc())
                $data[] = $item;

            return $data;

        }


    }


?>