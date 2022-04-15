package zakjo.studentsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import zakjo.studentsapp.Helpers.ChildrenAdapter;
import zakjo.studentsapp.Helpers.TeachersAdapter;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.MChildren;
import zakjo.studentsapp.model.Teachers;

public class Children extends AppCompatActivity {


    private ArrayList<MChildren> mChildrenList;


    RecyclerView childrenRecycler ;

    Button register_for ;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);


        register_for = (Button) findViewById(R.id.register_forsomeone);
        register_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Children.this , RegisterFor.class));
                finish();
            }
        });
        childrenRecycler = (RecyclerView) findViewById(R.id.childrenRecycler);
        childrenRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        childrenRecycler.setLayoutManager(linearLayoutManager);


        getChildren();


    }

    private void getChildren() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.CHILDREN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            mChildrenList = new ArrayList<>();

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                MChildren child = new MChildren();

                                JSONObject product = array.getJSONObject(i);

                                child.setSt_id(product.getString("st_id"));
                                child.setSt_name(product.getString("st_name"));
                                mChildrenList.add(child);
                            }

                            ChildrenAdapter childrenAdapter = new ChildrenAdapter(Children.this , mChildrenList );
                            childrenRecycler.setAdapter(childrenAdapter);

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

                params.put("parent_phone" , FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().substring(2));

                return params;
            }
        };


        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(Children.this , LoginOrChat.class));
    }
}
