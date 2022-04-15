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

public class RegisterFor extends AppCompatActivity {

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
        setContentView(R.layout.activity_register_for);



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


                    Toast.makeText(RegisterFor.this, "Please fill the required info", Toast.LENGTH_SHORT).show();


                }else{


                    RegisterUser();


                }

            }
        });


    }


    private void RegisterUser(){

        Constants.SECdialog = new ProgressDialog(RegisterFor.this);
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

                            Constants.SECdialog.dismiss();


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
                params.put("studentPhone" , "+2"+HstudentPhone);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterFor.this , LoginOrChat.class));

    }
}

