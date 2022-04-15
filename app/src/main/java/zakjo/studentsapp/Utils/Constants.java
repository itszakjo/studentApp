package zakjo.studentsapp.Utils;


import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;

import com.sinch.android.rtc.calling.Call;

import java.util.ArrayList;
import java.util.List;

import zakjo.studentsapp.Rertofit.FCMClient;
import zakjo.studentsapp.Rertofit.IFCMService;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Rertofit.RetrofitClient;
import zakjo.studentsapp.model.Chat;
import zakjo.studentsapp.model.Cities;
import zakjo.studentsapp.model.Days;
import zakjo.studentsapp.model.Groups;
import zakjo.studentsapp.model.MChildren;
import zakjo.studentsapp.model.MServices;
import zakjo.studentsapp.model.Plans;
import zakjo.studentsapp.model.Subjects;
import zakjo.studentsapp.model.Teachers;
import zakjo.studentsapp.model.Times;
import zakjo.studentsapp.model.Types;
import zakjo.studentsapp.model.User;


public class Constants {

    // Register , Login ,  Choose Gov then city  , Choose Salon ,
    // view profile , view pics n reviews , book now ,
    // choose day then time  , choose services , then confirm your booking ,
    // oder is added to your meetings , u can cancel your booking , the time is then available for others
    //
    // Home  Maps , healthPro , Statistics  , About




    // this is the local host of the emulator
//  public static final String ROOT_URL = "http://192.168.56.1/studentsAppBackend/";
//  public static final String ROOT_URL = "https://appplaygroundbby10.000webhostapp.com/studentsAppBackend/";
//    public static final String ROOT_URL = "https://www.serveizo.com/zcenter/android-api/";
    public static final String ROOT_URL = "https://rozewail.com/zcenter/android-api/";



    // SINCH API ACCESS
    public static final String APP_KEY = "7fcd27f1-783d-404a-9846-ff41312c2672";
    public static final String APP_SECRET = "aarghwWbpEqpuwwWxh9iLQ==";
    public static final String ENVIRONMENT = "clientapi.sinch.com";

    public static Call call ;


    public static Vibrator vnn ;

    public static long[] pattern = {0, 1000, 1000};


    public  static boolean isCall3active = false;


    public static MediaPlayer mediaPlayer, koftaMediaPlayer ;
    public static Uri uri , koftaUri; ;
    public static String path , koftaPath ;
    public static Button callBtn  ;

    public static String callerId;
    public static String recipientId;

    public static TextView state;

    public static String LOGIN_TYPE = "STUDENT";

    public static final String URL_REGISTER = ROOT_URL + "Register.php";

    public static final String STUDENT_LOGIN   = ROOT_URL + "Login.php";

    public static final String CITIES   = ROOT_URL + "getCountries.php";

    public static final String SUBJECTS   = ROOT_URL + "getSubjects.php";

    public static final String GET_EUALTION   = ROOT_URL + "getEvualtion.php";

    public static final String GET_ATTENDENCE   = ROOT_URL + "getAttendence.php";

    public static final String GET_ABSENCE   = ROOT_URL + "getAbsence.php";

    public static final String LEVELS   = ROOT_URL + "getStages.php";

    public static final String TEACHERS   = ROOT_URL + "getTeachers.php";

    public static final String ALL_TEACHERS   = ROOT_URL + "getAllTeachers.php";

    public static final String CHILDREN   = ROOT_URL + "getChildren.php";

    public static final String ADS   = ROOT_URL + "getAds.php";

    public static final String TEACHER_PICS  = ROOT_URL + "getTeacherPics.php";

    public static final String GROUPS  = ROOT_URL + "getGroups.php";

    public static final String UPDATE_STUDENT_INFO = ROOT_URL + "updateStudentInfo.php";

    public static final String UPDATE_IMAGE = ROOT_URL + "UploadImage.php";

    public static final String ASk_TEACHER = ROOT_URL + "askTeacher.php";

    public static final String BOOK_NOW = ROOT_URL + "bookNow.php";

    public static final String REGISTERED_GROUPS = ROOT_URL + "getRegisteredGroups.php";

    public static final String REQUESTED = ROOT_URL + "getRequests.php";

    public static ArrayList<String> mNames = new ArrayList<>();

    public static ArrayList<String> mNumbers = new ArrayList<>();

    public static ArrayList<String> unReadStringlist = new ArrayList<>();

    public static final String GOVS    = ROOT_URL + "getGovs.php";
    public static final String GET_DAYS = ROOT_URL + "getDays.php";
    public static final String CANCEL_REQ = ROOT_URL + "cancelRequested.php";
    public static final String CANCEL_REGGG = ROOT_URL + "cancelRegistered.php";
    public static final String CREATE_CALL_COUNT = ROOT_URL + "createCallCount.php";

    public static List<String> addedUsers = new ArrayList<>();
    public static List<String> addedUsersID = new ArrayList<>();

    public static String CHAT_TYPE = "";

    public static String ProfileImage_Link = "default";

    public static final String UPDATE = "Update";
    public static final String DELETE = "Delete";

    public static String userPhone = null;

    public static String imcalling_status = "ImCalling" ;

    public static Plans currentPlan = null; // id  , feature1 , feature2 , price

    public static Types currentType = null;

    public static Times currentTime = null;

    public static User currentUser = null;

    public static Days currentDay = null;

    public static Chat currentMessage = null;

    public static MChildren currentChild = null;

    public static Teachers currentTeacher = null;

    public static Subjects currentSubject = null;

    public static Cities currentCity = null;

    public static Groups currentGroup = null;

    public static String currentSalID = null ;

    public static  ProgressDialog GOLdialog , SECdialog , another;



    public static List<MServices> servicesList = new ArrayList<>();

    public static List<User> contactsList = new ArrayList<>();


//   public static final String CHOICE_ID = "";

    public static  double PRICE = 0.0;

    public static List<String> addedServices = new ArrayList<>();

    public static MyLabAPI getAPI(){

        return RetrofitClient.getClient(ROOT_URL).create(MyLabAPI.class);
    }

    private static final String FCMAPI ="https://fcm.googleapis.com/" ;

    public static IFCMService getFCMService()
    {
        return FCMClient.getClient(FCMAPI).create(IFCMService.class);

    }

    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivityManager !=null)
        {
            NetworkInfo[]  info = connectivityManager.getAllNetworkInfo();

            if(info != null)
            {
                for (int i=0; i<info.length; i++)
                {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)

                        return true;
                }
            }
        }

        return false ;
    }

    public static Vibrator getVibrator(Context context){

        return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }


}

