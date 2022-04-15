package zakjo.studentsapp;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import zakjo.studentsapp.Helpers.ImageRequestHandler;
import zakjo.studentsapp.Helpers.RequestHandler;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Utils.Constants;

public class Register extends AppCompatActivity {

    private EditText
            name ,
            address ,
            homePhoneReg   ,
            studentPhone ,
            email ,
            socialLink ,
            school ,
            parentName ,
            parentJob ,
            parentPhone ,
            parentRelation ,
            notes ,
            pass ;

    private static final int IMAGE_REUEST= 1 ;
    public static final String UPLOAD_KEY = "image";


    Bitmap FixBitmap;

    String ImageTag = "image_tag" ;

    String ImageName = "image_data" ;

    ProgressDialog progressDialog ;

    ByteArrayOutputStream byteArrayOutputStream ;


    FirebaseUser firebaseUser ;
    DatabaseReference databaseReference ;


    private Button btn_reg ;
    RadioGroup gender ;


    ImageView profileImageReg ;

    RadioButton radioMale , radioFemale ;

    String studentGender ;
    private Uri imageUri ;



    private int PICK_IMAGE_REQUEST = 1;

    private Button buttonChoose;
    private Button buttonUpload;

    private ImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;
    String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){

            finish();
            Intent intent = new Intent(Register.this, Home.class);
            intent.putExtra("LOGINTYPE" ,"STUDENT");
            Constants.LOGIN_TYPE = "STUDENT";
            startActivity(intent);

            return;
        }

        name = (EditText)findViewById(R.id.namereg);
        address = (EditText)findViewById(R.id.addressReg);
        homePhoneReg = (EditText)findViewById(R.id.homePhoneReg);
        studentPhone = (EditText)findViewById(R.id.studentPhone);
        email = (EditText)findViewById(R.id.Email);
        socialLink = (EditText)findViewById(R.id.socialLink);
        school = (EditText)findViewById(R.id.school);
        parentName = (EditText)findViewById(R.id.parentName);
        parentJob = (EditText)findViewById(R.id.parentJob);
        parentPhone = (EditText)findViewById(R.id.parentPhone);
        parentRelation = (EditText)findViewById(R.id.parentRelation);
        notes = (EditText)findViewById(R.id.notes);
        pass = (EditText)findViewById(R.id.passwordreg);

        studentPhone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());

        byteArrayOutputStream = new ByteArrayOutputStream();


        radioMale = (RadioButton)findViewById(R.id.radioMale);
        radioFemale = (RadioButton)findViewById(R.id.radioFemale);

        btn_reg =(Button)findViewById(R.id.register);

        if(radioFemale.isChecked()){

            studentGender = "Female";

        }else if(radioMale.isChecked()){

            studentGender = "Male";
        }

        // run the RegisterUser function when u click on the btn_reg button
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().trim().isEmpty() ||
                        parentPhone.getText().toString().trim().isEmpty() ||
                        address.getText().toString().trim().isEmpty() ){


                    Toast.makeText(Register.this, "Please fill the required info", Toast.LENGTH_SHORT).show();


                }else{


                    RegisterUser();


                }

            }
        });


    }


    private void RegisterUser(){

        Constants.SECdialog = new ProgressDialog(Register.this);
        Constants.SECdialog.setMessage("Loading..");
        Constants.SECdialog.show();


        //  getting data from Edit Text and putting it into those Variabls as Strings
        final String Hname = name.getText().toString().trim();
        final String Haddress = address.getText().toString().trim();
        final String HhomePhoneReg = homePhoneReg.getText().toString().trim();
        final String HstudentPhone = studentPhone.getText().toString().trim();
        final String Hemail = email.getText().toString().trim();
        final String HsocialLink = socialLink.getText().toString().trim();
        final String Hschool = school.getText().toString().trim();
        final String HparentName = parentName.getText().toString().trim();
        final String HparentJob = parentJob.getText().toString().trim();
        final String HparentPhone = parentPhone.getText().toString().trim();
        final String HparentRelation = parentRelation.getText().toString().trim();
        final String Hnotes = notes.getText().toString().trim();
        final String Hpass = pass.getText().toString().trim();


        // builing  a  request with method Post because we are inserting data
        // if we getting data we use method GET
        // then we go to the URL_REGISTER variable in Class Constants
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            login();

                        }catch (JSONException e){ e.printStackTrace(); }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                //  store the data we put into the strings up there into the database fields

                params.put("name" , Hname);
                params.put("gender" , studentGender);
                params.put("address" , Haddress);
                params.put("photo" , "");
                params.put("homePhoneReg" , HhomePhoneReg);
                params.put("studentPhone" , HstudentPhone);
                params.put("email" , Hemail);
                params.put("socailLink" , HsocialLink);
                params.put("school" , Hschool);
                params.put("parentName" , HparentName);
                params.put("parentJob" , HparentJob);
                params.put("parentPhone" , HparentPhone);
                params.put("parentRelation" , HparentRelation);
                params.put("notes" , Hnotes);
                params.put("pass" , Hpass);


                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(request);

    }

    public void login(){

        Constants.GOLdialog = new ProgressDialog(Register.this);
        Constants.GOLdialog.setMessage("Loading..");
        Constants.GOLdialog.show();

        StringRequest request = new StringRequest(
                Request.Method.POST,
                Constants.STUDENT_LOGIN,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);

                            if(!object.getBoolean("error")){
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .studentLogin(

                                                object.getString("id"),
                                                object.getString("name"),
                                                object.getString("gender"),
                                                object.getString("address"),
                                                object.getString("photo"),
                                                object.getString("homePhoneReg"),
                                                object.getString("studentPhone"),
                                                object.getString("email"),
                                                object.getString("socailLink"),
                                                object.getString("school"),
                                                object.getString("parentName"),
                                                object.getString("parentJob"),
                                                object.getString("parentPhone"),
                                                object.getString("parentRelation"),
                                                object.getString("notes")

                                        );

                                startActivity(new Intent(Register.this , Home.class));
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){e.printStackTrace();}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("st_mobile", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(request);

    }

    private void updateTokenToServer() {

        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {

                        MyLabAPI mService = Constants.getAPI();
                        mService.updateToken(
                                SharedPrefManager.getInstance(getApplicationContext()).getKeyStudentResponsiblePhone() ,
                                instanceIdResult.getToken() ,
                                "0")
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {

                                        Log.d("DEBUG" , response.body());

                                    }
                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {

                                        Log.d("DEBUG" , t.getMessage());
                                    }
                                });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Register.this , LoginOrChat.class));

    }
}

