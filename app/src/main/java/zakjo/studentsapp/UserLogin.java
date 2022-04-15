package zakjo.studentsapp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import zakjo.studentsapp.Helpers.RequestHandler;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Utils.Constants;

public class UserLogin extends AppCompatActivity {

    public static EditText phoneNumber, Password;
    Button LogIn  ;
    TextView regButton ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){

            finish();
            startActivity(new Intent(this , Home.class));
            return;
        }



        phoneNumber = (EditText)findViewById(R.id.phoneNumberLogin);

        Password = (EditText)findViewById(R.id.passwordLogin);

        regButton = (TextView) findViewById(R.id.regBtn);

        LogIn = (Button)findViewById(R.id.loginBtn);

        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Constants.isConnectedToInternet(UserLogin.this)){

                    login();

                }else {

                    Toast.makeText(UserLogin.this, "No Internet", Toast.LENGTH_SHORT).show();
                }
            }
        });

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserLogin.this, Register.class);
                startActivity(intent);


            }
        });

        if(Constants.SECdialog !=null){

            Constants.SECdialog.dismiss();
        }

    }

    public void login(){

        Constants.GOLdialog = new ProgressDialog(UserLogin.this);
        Constants.GOLdialog.setMessage("Loading..");
        Constants.GOLdialog.show();

        final String phonNmber = phoneNumber.getText().toString().trim();
        final String passs = Password.getText().toString().trim();

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

                                startActivity(new Intent(UserLogin.this , Home.class));
                            }
                            else{
                                Toast.makeText(getApplicationContext(),object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
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
                params.put("st_mobile",phonNmber);
                params.put("password",passs);
                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(request);

    }

}
