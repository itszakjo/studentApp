package zakjo.studentsapp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {


    private static SharedPrefManager mInstance ;
    private static Context mCtx;


    private static final String SHARED_PREF_NAME = "mysharedpref12";
    private static final String SEC_SAHRED_PREF = "anotheronelol";

    private static final String USERID = "USERID";

    private static final String KEY_STUDENT_ID = "st_id";
    public static String KEY_STUDENT_NAME = "st_name";
    private static final String KEY_STUDENT_GENDER= "st_gender";
    private static final String KEY_STUDENT_ADDRESS = "st_address";
    private static final String KEY_STUDENT_IMAGE = "st_image";
    private static final String KEY_STUDENT_TELEPHONE = "st_telephone";
    private static final String KEY_STUDENT_MOBILE= "t_mobile";
    private static final String KEY_STUDENT_EMAIL = "st_email";
    private static final String KEY_STUDENT_SOCIALLINK= "st_facebook";
    private static final String KEY_STUDENT_SCHOOL = "st_school";
    private static final String KEY_STUDENT_RESPONSIBLE_NAME = "st_responsible_name";
    private static final String KEY_STUDENT_RESPONSIBLE_JOB = "st_responsible_job";
    private static final String KEY_STUDENT_RESPONSIBLE_RELATION = "st_responsible_relation";
    private static final String KEY_STUDENT_RESPONSIBLE_PHONE= "st_responsible_telephone";
    private static final String KEY_STUDENT_NOTES = "notes";
    private static final String KEY_STUDENT_PASSWORD = "password";





    private SharedPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if(mInstance == null){
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }




    public boolean studentLogin(String id , String name , String gender ,
                                String addresss , String image , String telephone ,
                                String phone , String email , String sociallink  ,
                                String school , String respobsibleName , String responsibleJob , String responsibleRelation ,
                                String responsiblePhone , String notes){


        SharedPreferences sharedPreferences  = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();

        editor.putString(KEY_STUDENT_ID , id);
        editor.putString(KEY_STUDENT_NAME , name);
        editor.putString(KEY_STUDENT_GENDER , gender);
        editor.putString(KEY_STUDENT_ADDRESS , addresss);
        editor.putString(KEY_STUDENT_IMAGE , image);
        editor.putString(KEY_STUDENT_TELEPHONE , telephone);
        editor.putString(KEY_STUDENT_MOBILE , phone);
        editor.putString(KEY_STUDENT_EMAIL , email);
        editor.putString(KEY_STUDENT_SOCIALLINK , sociallink);
        editor.putString(KEY_STUDENT_SCHOOL , school);
        editor.putString(KEY_STUDENT_RESPONSIBLE_NAME , respobsibleName);
        editor.putString(KEY_STUDENT_RESPONSIBLE_JOB , responsibleJob);
        editor.putString(KEY_STUDENT_RESPONSIBLE_RELATION , responsibleRelation);
        editor.putString(KEY_STUDENT_RESPONSIBLE_PHONE , responsiblePhone);
        editor.putString(KEY_STUDENT_NOTES , notes);

        editor.apply();

        return true;
    }

    public boolean userAppLogin(String id){

        SharedPreferences sharedPreferences  = mCtx.getSharedPreferences(SEC_SAHRED_PREF , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putString(USERID , id);

        editor.apply();

        return true;
    }

    public boolean isAppLogged(){
        SharedPreferences sharedPreferences  = mCtx.getSharedPreferences(SEC_SAHRED_PREF , Context.MODE_PRIVATE);
        if(sharedPreferences.getString(USERID  , null) !=null ) // if it is full
        {
            return true;

        }

        return false;
    }


    public boolean isLoggedIn(){

        SharedPreferences sharedPreferences  = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        if(sharedPreferences.getString(KEY_STUDENT_NAME, null )!=null )
        {
            return true;

        }

        return false;
    }

    public boolean logout(){

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    public String getID(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_ID, null);
    }

    public String getKeyStudentName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_NAME, null);
    }

    public String getKeyStudentGender(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_GENDER, null);
    }

    public String getKeyStudentAddress(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_ADDRESS, null);
    }

    public String getKeyStudentImage(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_IMAGE, null);
    }

    public String getKeyStudentTelephone(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_TELEPHONE, null);
    }

    public String getKeyStudentMobile(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_MOBILE, null);
    }

    public String getKeyStudentEmail(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_EMAIL, null);
    }

    public String getKeyStudentSociallink(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_SOCIALLINK, null);
    }

    public String getKeyStudentSchool(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_SCHOOL, null);
    }

    public String getKeyStudentResponsibleName(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_RESPONSIBLE_NAME, null);
    }

    public String getKeyStudentResponsibleJob(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_RESPONSIBLE_JOB, null);
    }

    public String getKeyStudentResponsibleRelation(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_RESPONSIBLE_RELATION, null);
    }

    public String getKeyStudentResponsiblePhone(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_RESPONSIBLE_PHONE, null);
    }

    public String getKeyStudentNotes(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME , Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_STUDENT_NOTES, null);
    }








}
