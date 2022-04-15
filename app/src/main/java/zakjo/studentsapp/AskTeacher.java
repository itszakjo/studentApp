package zakjo.studentsapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zakjo.studentsapp.Helpers.RequestHandler;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.AllTeachers;


public class AskTeacher extends AppCompatActivity {





    Spinner askTeacherSpinner;
    EditText askedQuestion;

    Button askButton ;

    MyLabAPI myLabAPI ;

    private ArrayList<AllTeachers> mTeachersList;
    private ArrayList<String> mTeachersStringList;

    int teacherid = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_teacher);


        myLabAPI = Constants.getAPI();

        askTeacherSpinner = (Spinner) findViewById(R.id.aTeacherSpinner);
        askButton = (Button) findViewById(R.id.askBtn);
        askedQuestion = (EditText) findViewById(R.id.questionText);


        getAllTeachers();


        askTeacherSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                 teacherid = position  ;



//                DayFromSpinner = mDaysList.get(position).getDay();

                // or u can use this for direct result without using the array
                //
                // DayFromSpinner  = (String) parent.getItemAtPosition(position);

//                Toast.makeText(AskTeacher.this, String.valueOf(position), Toast.LENGTH_SHORT).show();


            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendQuestion();

            }
        });





    }


    private void getAllTeachers() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Constants.ALL_TEACHERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            mTeachersList = new ArrayList<>();
                            mTeachersStringList = new ArrayList<>();

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                AllTeachers teachers = new AllTeachers();

                                JSONObject product = array.getJSONObject(i);

                                teachers.setT_id(product.getString("t_id"));
                                teachers.setT_name(product.getString("t_name"));
                                mTeachersList.add(teachers);


                            }

                            for(int i =0 ; i < mTeachersList.size(); i++){

                                mTeachersStringList.add(mTeachersList.get(i).getT_name().toString());
                            }


                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AskTeacher.this, R.layout.spinner_item, mTeachersStringList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
                            askTeacherSpinner.setAdapter(spinnerArrayAdapter);





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }


    private void sendQuestion(){


        //  getting data from Edit Text and putting it into those Variabls as Strings
        final String askedQuestionString = askedQuestion.getText().toString().trim();
//        final String Hemail = email.getText().toString().trim();
//        final String Hpass = pass.getText().toString().trim();
//        final String Hphone = phone.getText().toString().trim();
//        final String Haddress = address.getText().toString().trim();
//        final String HhomePhoneReg = homePhoneReg.getText().toString().trim();
//        final String HresponsibleReg = responsibleReg.getText().toString().trim();
//        final String HresponsilbePhoneReg = responsilbePhoneReg.getText().toString().trim();
//
//
//
        // builing  a  request with method Post because we are inserting data
        // if we getting data we use method GET
        // then we go to the URL_REGISTER variable in Class Constants
        StringRequest request = new StringRequest(Request.Method.POST, Constants.ASk_TEACHER,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                            finish();

                        }catch (JSONException e){

                            e.printStackTrace();
                        }
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


                params.put("teacher_id" , String.valueOf(teacherid));
                params.put("message" , askedQuestionString);
                params.put("student_id" , SharedPrefManager.getInstance(AskTeacher.this).getID());


                return params;
            }
        };



        RequestHandler.getInstance(this).addToRequestQueue(request);





    }


}
