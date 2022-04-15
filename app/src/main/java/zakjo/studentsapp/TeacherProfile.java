package zakjo.studentsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zakjo.studentsapp.Helpers.RequestHandler;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Helpers.TeacherPicsAdapter;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Groups;
import zakjo.studentsapp.model.TeacherPics;

public class TeacherProfile extends AppCompatActivity {

    TextView teacherName ,teacher_bio ;


    String teacher_id , student_id  ,  mGroupId ;

    int subject_id , group_id  ;

    RecyclerView images_rec;

    ArrayList<TeacherPics> PicsArrayList ;

    Button bookNowBtn ;

    Spinner  groupsSpinner;

    ArrayList<Groups> mGroupsList;
    ArrayList<String> mGroupsStringsList;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MyLabAPI myLabAPI ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        teacher_id = Constants.currentTeacher.t_id ;

        subject_id = Constants.currentSubject.c_id ;

        student_id = SharedPrefManager.getInstance(this).getID();

        teacherName = (TextView)findViewById(R.id.teacher_name);
        teacherName.setText(Constants.currentTeacher.getT_name());

        teacher_bio = (TextView)findViewById(R.id.teacher_bio);
        teacher_bio.setText(Constants.currentTeacher.getBio());

        bookNowBtn = (Button) findViewById(R.id.bookNowBtn);

        myLabAPI = Constants.getAPI();


        images_rec = findViewById(R.id.pics_recycler);
        images_rec.setLayoutManager(new GridLayoutManager(TeacherProfile.this , 3));
        //images_rec.setLayoutManager(new LinearLayoutManager((), LinearLayoutManager.VERTICAL ,false));
        images_rec.setHasFixedSize(true);



//        getSalonPics(0);



        bookNowBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ShowChooseGroupDialog();
            }
        });

    }




    private void getSalonPics(int refteacher) {

        compositeDisposable.add(myLabAPI.getTeacherPicsN(refteacher)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<TeacherPics>>() {
                        @Override
                        public void accept(List<TeacherPics> mSalonpics) throws Exception {

                            displayTeachernPics(mSalonpics);
                        }

                    }));
    }

    private void displayTeachernPics(List<TeacherPics> mTeacherPics) {

        TeacherPicsAdapter adapter = new TeacherPicsAdapter(this , mTeacherPics);

        images_rec.setAdapter(adapter);
    }


    private void ShowChooseGroupDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View itemView = LayoutInflater.from(this).inflate(R.layout.choose_service_layout , null);

        builder.setTitle("Available Groups");
        builder.setMessage("Choose your group");

        groupsSpinner  = itemView.findViewById(R.id.groupSpinner);

        mGroupsList = new ArrayList<>();
        mGroupsStringsList = new ArrayList<>();

        getGroups(subject_id,teacher_id);



        groupsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                String mo  = mGroupsList.get(position).getGroup_name() ;
                 mGroupId  = mGroupsList.get(position).group_id ;



            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        builder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        builder.setView(itemView);
        final AlertDialog dialog = builder.create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(mGroupsList.size()>0){
                    showConfirmDialog();

                    dialog.dismiss();

                }else{


                    Toast.makeText(TeacherProfile.this, "No Groups Available!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void getGroups(final int subject_id, final String teacher_id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.GROUPS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        mGroupsList = new ArrayList<>();
                        mGroupsStringsList = new ArrayList<>();

                        try {
                            JSONArray array  = new JSONArray(response);

                            for(int i = 0; i < array.length(); i++) {

                                Groups groups = new Groups();
                                JSONObject product = array.getJSONObject(i);
                                groups.setGroup_id(product.getString("group_id"));
                                groups.setGroup_name(product.getString("group_name"));
                                mGroupsList.add(groups);

                            }

                            for(int i =0 ; i < mGroupsList.size(); i++){

                                mGroupsStringsList.add(mGroupsList.get(i).getGroup_name().toString());
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(TeacherProfile.this, R.layout.spinner_black_item, mGroupsStringsList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_black_item); // The drop down view
                            groupsSpinner.setAdapter(spinnerArrayAdapter);


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
                params.put("teacher_id" , teacher_id);

                return params;
            }
        };


        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showConfirmDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);



        builder.setTitle("Confirm");
        builder.setMessage("Are You Sure u want to register ?");

        builder.setPositiveButton("Book", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


            }
        });

        final AlertDialog dialog = builder.create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bookNow(student_id , Integer.parseInt(mGroupId));

                dialog.dismiss();

            }
        });

    }

    private void bookNow(final String student , final int group){

        StringRequest request = new StringRequest(Request.Method.POST, Constants.BOOK_NOW,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            JSONObject jsonObject = new JSONObject(response);

//                          Toast.makeText(mCtx, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        startActivity(new Intent(TeacherProfile.this , Home.class));

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
                params.put("student_id" , student);
                params.put("group_id" , String.valueOf(group));

                return params;
            }
        };

        //Volley.newRequestQueue(this).add(request); // or
        RequestHandler.getInstance(this).addToRequestQueue(request);

    }
}
