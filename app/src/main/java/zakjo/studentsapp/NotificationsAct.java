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

import zakjo.studentsapp.Helpers.NotificationsAdapter;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Helpers.TeachersAdapter;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Groups;
import zakjo.studentsapp.model.Notifs;
import zakjo.studentsapp.model.Teachers;

public class NotificationsAct extends AppCompatActivity {



    private ArrayList<Notifs> mNotifisList;


    RecyclerView notifis_rec ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notifis_rec = (RecyclerView) findViewById(R.id.noti_rec);
        notifis_rec.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        notifis_rec.setLayoutManager(linearLayoutManager);



        getNotifis();

    }

    private void getNotifis() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.ADS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            mNotifisList = new ArrayList<>();

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                Notifs notifs = new Notifs();

                                JSONObject product = array.getJSONObject(i);

                                notifs.setT_id(product.getString("adv_id"));
                                notifs.setAdv_text(product.getString("adv_text"));
                                notifs.setAdv_date(product.getString("adv_date"));

                                mNotifisList.add(notifs);
                            }

                            NotificationsAdapter teachersAdapter = new NotificationsAdapter(NotificationsAct.this , mNotifisList );
                            notifis_rec.setAdapter(teachersAdapter);


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

                params.put("st_id" , SharedPrefManager.getInstance(NotificationsAct.this).getID());

                return params;
            }
        };


        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}
