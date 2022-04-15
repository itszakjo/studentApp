package zakjo.studentsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import zakjo.studentsapp.Helpers.EvualtionAdapter;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Attendence;
import zakjo.studentsapp.model.Evualtion;

public class EvualtionAct extends AppCompatActivity {

    private ArrayList<Evualtion> mEvulist;


    RecyclerView evu_rec ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evualtion);


        evu_rec = (RecyclerView) findViewById(R.id.marks_rec);
        evu_rec.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        evu_rec.setLayoutManager(linearLayoutManager);



        getEvu();
    }

    private void getEvu() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.GET_EUALTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            mEvulist = new ArrayList<>();

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                Evualtion evualtion = new Evualtion();

                                JSONObject product = array.getJSONObject(i);

                                evualtion.setPercentage(product.getString("percentage"));
                                evualtion.setCreated_at(product.getString("created_at"));
                                evualtion.setMarks(product.getString("marks"));
                                evualtion.setTitle(product.getString("title"));
                                evualtion.setT_name(product.getString("t_name"));
                                evualtion.setC_name(product.getString("c_name"));

                                mEvulist.add(evualtion);
                            }

                            EvualtionAdapter evualtionAdapter = new EvualtionAdapter(EvualtionAct.this , mEvulist );
                            evu_rec.setAdapter(evualtionAdapter);


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

                params.put("st_id" , SharedPrefManager.getInstance(EvualtionAct.this).getID());

                return params;
            }
        };


        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}
