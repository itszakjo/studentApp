package zakjo.studentsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import zakjo.studentsapp.Helpers.AttendenceAdapter;
import zakjo.studentsapp.Helpers.NotificationsAdapter;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Attendence;
import zakjo.studentsapp.model.Cities;
import zakjo.studentsapp.model.MRegisteredGroups;
import zakjo.studentsapp.model.Notifs;
import zakjo.studentsapp.model.Subjects;

public class AttendenceAct extends AppCompatActivity {

    private ArrayList<Attendence> mAttendenceList;


    RecyclerView attendence_rec ;

    Spinner registeredGroupsSpinner;

    private ArrayList<MRegisteredGroups> registeredGroupsList;
    private ArrayList<String> mRegisteredGroupsStringList;

    String studentId ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);

        registeredGroupsSpinner = (Spinner) findViewById(R.id.registeredGroupsSpinner);

        attendence_rec = (RecyclerView) findViewById(R.id.attendence_rec);
        attendence_rec.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        attendence_rec.setLayoutManager(linearLayoutManager);

        if(Constants.LOGIN_TYPE.equals("PARENT")){

            studentId = Constants.currentChild.getSt_id();

        }else {

            studentId = SharedPrefManager.getInstance(this).getID();
        }

        loadRegisteredGroups(studentId);

        registeredGroupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {


                String refgroup = registeredGroupsList.get(position).getGroup_id();

                getAttendence(refgroup);


                //                DayFromSpinner = mDaysList.get(position).getDay();

                // or u can use this for direct result without using the array
                //
                // DayFromSpinner  = (String) parent.getItemAtPosition(position);

//                Toast.makeText(Home.this, String.valueOf(position), Toast.LENGTH_SHORT).show();



            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void loadRegisteredGroups(final String st_id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.REGISTERED_GROUPS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            registeredGroupsList = new ArrayList<MRegisteredGroups>();
                            mRegisteredGroupsStringList = new ArrayList<>();

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                MRegisteredGroups mRegisteredGroups = new MRegisteredGroups();

                                JSONObject product = array.getJSONObject(i);

                                mRegisteredGroups.setGroup_id(product.getString("group_id"));
                                mRegisteredGroups.setGroup_name(product.getString("group_name"));
                                registeredGroupsList.add(mRegisteredGroups);



                            }

                            for(int i = 0; i < registeredGroupsList.size(); i++){

                                mRegisteredGroupsStringList.add(registeredGroupsList.get(i).getGroup_name().toString());
                            }


                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(AttendenceAct.this, R.layout.spinner_item, mRegisteredGroupsStringList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
                            registeredGroupsSpinner.setAdapter(spinnerArrayAdapter);


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

                params.put("st_id" , st_id);

                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

    private void getAttendence(final String refgroup) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.GET_ATTENDENCE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            mAttendenceList = new ArrayList<>();

                            JSONArray array = new JSONArray(response);



                            for (int i = 0; i < array.length(); i++) {

                                Attendence attendence = new Attendence();

                                JSONObject product = array.getJSONObject(i);

                                attendence.setId(product.getString("id"));
                                attendence.setStatus(product.getString("status"));
                                attendence.setCreated_at(product.getString("created_at"));
                                attendence.setSt_id(product.getString("st_id"));
                                attendence.setGroup_id(product.getString("group_id"));

                                mAttendenceList.add(attendence);

                            }


                            AttendenceAdapter attendenceAdapter = new AttendenceAdapter(AttendenceAct.this , mAttendenceList );
                            attendence_rec.setAdapter(attendenceAdapter);


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

                params.put("st_id" , studentId);
                params.put("group_id" , refgroup);

                return params;
            }
        };


        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}
