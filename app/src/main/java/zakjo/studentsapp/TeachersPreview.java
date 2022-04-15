package zakjo.studentsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import zakjo.studentsapp.Helpers.TeachersAdapter;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Groups;
import zakjo.studentsapp.model.Teachers;

public class TeachersPreview extends AppCompatActivity {



    private ArrayList<Groups> mGroupsList;

    private ArrayList<String> mGroupsStringsList;




    private ArrayList<Teachers> mTeachersList;


    RecyclerView teacherRecycler ;

    int  subject_id  , city_id;

    Button click ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_preview);

        teacherRecycler = (RecyclerView) findViewById(R.id.teacherRecycler);
        teacherRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        teacherRecycler.setLayoutManager(linearLayoutManager);

        subject_id = Constants.currentSubject.getC_id();
        city_id = Constants.currentCity.getId();


        getTeachers(subject_id , city_id);

    }

    private void getTeachers(final int subject_id , final int city_id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.TEACHERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            mTeachersList = new ArrayList<>();

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                Teachers teachers = new Teachers();

                                JSONObject product = array.getJSONObject(i);

                                teachers.setT_id(product.getString("t_id"));
                                teachers.setT_name(product.getString("t_name"));
                                teachers.setBio(product.getString("bio"));
                                mTeachersList.add(teachers);
                            }

                            TeachersAdapter teachersAdapter = new TeachersAdapter(TeachersPreview.this , mTeachersList );
                            teacherRecycler.setAdapter(teachersAdapter);

                            if(Constants.SECdialog !=null){

                                Constants.SECdialog.dismiss();
                            }


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
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("subject_id" , String.valueOf(subject_id));
                params.put("city_id" , String.valueOf(city_id));

                return params;
            }
        };


        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


}
