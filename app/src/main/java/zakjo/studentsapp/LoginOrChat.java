package zakjo.studentsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import zakjo.studentsapp.Helpers.RequestHandler;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Utils.Constants;

public class LoginOrChat extends AppCompatActivity {


    Button loginToStudentApp , loginToChatApp ;

    final int RC_PERMISSION_CODE = 45;


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            startActivity(new Intent(this , MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_chat);

        if(Constants.GOLdialog !=null){Constants.GOLdialog.dismiss(); }


        if (ActivityCompat.checkSelfPermission(LoginOrChat.this , Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(LoginOrChat.this , Manifest.permission.RECORD_AUDIO)
                        != PackageManager.PERMISSION_GRANTED  ){

            requestPermission();
        }

        final FirebaseAuth  auth = FirebaseAuth.getInstance();


        // if signed in go to the chat activity
        //        if(auth.getCurrentUser() != null){
//            Intent intent = new Intent(LoginOrChat.this  , MainForChat.class);
//            startActivity(intent);
//            finish();
//
//        }


        findViewById(R.id.loginToStudentBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();

            }
        });



        // open chat activity
        findViewById(R.id.loginToChatBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginOrChat.this , MainForChat.class);
                startActivity(intent);
                finish();

            }
        });


        // open parent activity
        findViewById(R.id.loginToParentBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginOrChat.this , Children.class);
                intent.putExtra("LOGINTYPE" ,"PARENT");
                Constants.LOGIN_TYPE = "PARENT";
                startActivity(intent);

                finish();

            }
        });


        // logout of the app ( booking & chat )
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.GOLdialog = new ProgressDialog(LoginOrChat.this);
                Constants.GOLdialog.setMessage("Logging out..");
                Constants.GOLdialog.show();

                AuthUI.getInstance().signOut(LoginOrChat.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                startActivity(new Intent(LoginOrChat.this , MainActivity.class));
                                finish();
                                Constants.GOLdialog.dismiss();
                            }
                        });

                // destroy the student login session
                gout();
                finish();
            }
        });


    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(LoginOrChat.this, new String[]{
                android.Manifest.permission.READ_CONTACTS ,
                android.Manifest.permission.RECORD_AUDIO ,
                android.Manifest.permission.READ_EXTERNAL_STORAGE ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ,

        }, RC_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case RC_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){

                    System.out.println("Permission Granted");

                }else{

                    System.out.println("Permission denied");
                }
            }
            break;
        }


    }

    public void login(){


        Constants.GOLdialog = new ProgressDialog(LoginOrChat.this);
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

                                startActivity(new Intent(LoginOrChat.this , Home.class));
                                finish();
                            }
                            else{

                                Toast.makeText(getApplicationContext(),"You are not registered Please Register First!", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(LoginOrChat.this , Register.class));
                                finish();

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

    private void gout() {

        if(SharedPrefManager.getInstance(this).isLoggedIn()) {
            SharedPrefManager.getInstance(this).logout();
        }

    }

}
