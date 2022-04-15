package zakjo.studentsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

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

import zakjo.studentsapp.Helpers.TeachersAdapter;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.AllTeachers;

public class ChatTeacher extends AppCompatActivity {


    MyLabAPI myLabAPI ;

    private ArrayList<AllTeachers> mTeachersList;


    RecyclerView teacherRecycler ;

    String  LoginType = "" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_teacher);



        Intent intent = getIntent();

        if(intent !=null) {


            LoginType = intent.getStringExtra("LOGINTYPE");

        }
        myLabAPI = Constants.getAPI();

        teacherRecycler = (RecyclerView) findViewById(R.id.teacherRecycler);
        teacherRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        teacherRecycler.setLayoutManager(linearLayoutManager);


        getAllTeachers();
    }

    private void getAllTeachers() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Constants.TEACHERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            mTeachersList = new ArrayList<>();



                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                AllTeachers teachers = new AllTeachers();

                                JSONObject product = array.getJSONObject(i);

                                teachers.setT_name(product.getString("t_name"));
                                mTeachersList.add(teachers);


                            }





//                            TeachersAdapter teachersAdapter = new TeachersAdapter(ChatTeacher.this , mTeachersList );
//
//                            teacherRecycler.setAdapter(teachersAdapter);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }





                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

}
