package zakjo.studentsapp;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import io.paperdb.Paper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zakjo.studentsapp.Helpers.LocaleHelper;
import zakjo.studentsapp.Helpers.RequestHandler;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Govs;
import zakjo.studentsapp.model.Cities;
import zakjo.studentsapp.model.Groups;
import zakjo.studentsapp.model.Levels;
import zakjo.studentsapp.model.Subjects;
import zakjo.studentsapp.model.Teachers;


public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Spinner citiesSpinner ,govSpinner , levelSpinner, subjectSpinner  ;

    Button RegisterBtn ;

    int  subject_id;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    MyLabAPI myLabAPI ;

    ImageView logout , toglle , notif ;  ;

    TextView username  ;

    private ArrayList<Cities> mCitiesList;
    private ArrayList<String> mCitiesStringList;


    private ArrayList<Govs> mGovsList;
    private ArrayList<String> mGovsStringList;

    private ArrayList<Levels> mLevelsList;
    private ArrayList<String> mLevelsStringList;

    private ArrayList<Subjects> mSubjectsList;
    private ArrayList<String> mSubjectsStringList;

//    private ArrayList<Teachers> mTeachersList;
//    private ArrayList<String> mTeachersStringList;
//
//    private ArrayList<Groups> mGroupsList;
//    private ArrayList<String> mGroupsStringsList;

    DatabaseReference databaseReference;

    String studentID ;

    // after choosing the subject u want , go button apprears >
    // u go to page of teachers that has teachers blocks >
    // once u click on any of them it takes u to their profile >

    String LoginType = "";

    @Override
    protected void attachBaseContext(Context newBase) {

        super.attachBaseContext(LocaleHelper.onAttach(newBase , "en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_home);


        Intent intent = getIntent();

        if(intent !=null) {

            LoginType = intent.getStringExtra("LOGINTYPE");

        }else {

            LoginType = "";
        }

//        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
//
//            finish();
//            startActivity(new Intent(this , Register.class));
//            return;
//        }


        if(Constants.SECdialog !=null){
            Constants.SECdialog.dismiss();

        }

        toglle = findViewById(R.id.drawer_controller);

        username = findViewById(R.id.username);

        notif = (ImageView) findViewById(R.id.notifications);

        if(Constants.LOGIN_TYPE.equals("PARENT")){

            username.setText(Constants.currentChild.getSt_name());
            notif.setVisibility(View.GONE);

        }else {

            username.setText(SharedPrefManager.getInstance(this).getKeyStudentName());
        }

        notif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, NotificationsAct.class));
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toglle.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                if (drawer.isDrawerOpen(GravityCompat.START)) {

                    drawer.closeDrawer(GravityCompat.START);

                }else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setting name for the user
        View headerView = navigationView.getHeaderView(0);
        ImageView profileImaaage = headerView.findViewById(R.id.profileImageDrawer);



        if(Constants.LOGIN_TYPE.equals("PARENT")){

            profileImaaage.setImageResource(R.drawable.person);


        }else {

            if (SharedPrefManager.getInstance(this).getKeyStudentImage().equals("")) {

                profileImaaage.setImageResource(R.drawable.person);

            } else {

                Glide.with(this).load(Constants.ROOT_URL + SharedPrefManager.getInstance(this).getKeyStudentImage()).into(profileImaaage);
            }


        }



        TextView textName = headerView.findViewById(R. id.userNameDrawer);

        if(Constants.LOGIN_TYPE.equals("PARENT")){

            textName.setText(Constants.currentChild.getSt_name());

            studentID = Constants.currentChild.getSt_id();

        }else {

            textName.setText(SharedPrefManager.getInstance(this).getKeyStudentName());

            studentID = SharedPrefManager.getInstance(this).getID();

        }
        profileImaaage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(Home.this , EditInfo.class);
                intent.putExtra("LOGINTYPE" ,"PARENT");
                startActivity(intent);
            }
        });



//        profileImaaage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Makin an intent to takes us from home to Login
//                Intent intent = new Intent(Home.this, EditInfo.class);
//                // Starts the Activity
//                startActivity(intent);
//
//
//            }
//        });
        myLabAPI = Constants.getAPI();

//        logout = (ImageView) findViewById(R.id.logout);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//                gout();
//                // Makin an intent to takes us from home to Login
//                Intent intent = new Intent(Home.this, UserLogin.class);
//                // Starts the Activity
//                startActivity(intent);
//                finish();

//            }
//        });

        govSpinner = (Spinner) findViewById(R.id.govSpinner);
        citiesSpinner = (Spinner) findViewById(R.id.CitiesSpinner);
        levelSpinner = (Spinner) findViewById(R.id.levelSpinner);
        subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);

        RegisterBtn = (Button) findViewById(R.id.registerInGroup);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //                Constants.SECdialog = new ProgressDialog(Home.this);
//                Constants.SECdialog.setMessage("Loading..");
//                Constants.SECdialog.show();

//                startActivity(new Intent(Home.this , TeachersPreview.class));

                Intent intent = new Intent(Home.this, TeachersPreview.class);
                intent.putExtra("LOGINTYPE" ,"PARENT");
                startActivity(intent);

            }
        });

        if(Constants.isConnectedToInternet(this)){

            if(Constants.GOLdialog !=null){

                Constants.GOLdialog.dismiss();
            }


            Constants.another = new ProgressDialog(Home.this);

            Constants.another.setMessage("Loading..");

            Constants.another.show();


            getGovs ();


        }else {

            Toast.makeText(this, "No Internet! ", Toast.LENGTH_SHORT).show();
        }

        govSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {


                int refgov = mGovsList.get(position).getId();


                //                DayFromSpinner = mDaysList.get(position).getDay();

                // or u can use this for direct result without using the array
                //
                // DayFromSpinner  = (String) parent.getItemAtPosition(position);

//                Toast.makeText(Home.this, String.valueOf(position), Toast.LENGTH_SHORT).show();


                getCities(String.valueOf(refgov));

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        citiesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {


                //                DayFromSpinner = mDaysList.get(position).getDay();

                // or u can use this for direct result without using the array
                //
                // DayFromSpinner  = (String) parent.getItemAtPosition(position);

//                Toast.makeText(Home.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

                Constants.currentCity = mCitiesList.get(position);


                getLevels();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

                int stage_id = mLevelsList.get(position).getStage_id();

                // or u can use this for direct result without using the array
                //
                // attrfromSpinner  = (String) parent.getItemAtPosition(position);

//                Toast.makeText(Home.this, String.valueOf(stage_id), Toast.LENGTH_SHORT).show();

//                try {
                    getSubjects(stage_id);

//                }catch (Exception e){
//
//                    e.printStackTrace();
//                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {

//                 subject_id = position + 1 ;

                 Constants.currentSubject = mSubjectsList.get(position);

//                DayFromSpinner = mDaysList.get(position).getDay();

                // or u can use this for direct result without using the array
                //
                // DayFromSpinner  = (String) parent.getItemAtPosition(position);

                RegisterBtn.setVisibility(View.VISIBLE);
                Constants.another.dismiss();



            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



//
//        RegisterBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                RegisterInGroup();
//            }
//        });

        Paper.init(this); // for logging out


        // Defualt is En
        String Language = Paper.book().read("language");
        if(Language == null)
            Paper.book().write("language" , "en");

        updateView((String)Paper.book().read("language"));

        updateUserFirebaseId(studentID);

    }

    private void getGovs (){

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.GOVS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        mGovsList = new ArrayList<>();
                        mGovsStringList = new ArrayList<>();

                        try {
                            JSONArray array  = new JSONArray(response);

                            for(int i = 0; i < array.length(); i++) {

                                Govs govs = new Govs();
                                JSONObject product = array.getJSONObject(i);
                                govs.setGov(product.getString("gov"));
                                govs.setId(product.getInt("id"));
                                mGovsList.add(govs);

                            }


                            for(int i = 0; i < mGovsList.size(); i++){

                                mGovsStringList.add(mGovsList.get(i).getGov().toString());
                            }
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Home.this, R.layout.spinner_item, mGovsStringList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
                            govSpinner.setAdapter(spinnerArrayAdapter);


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

    private void updateUserFirebaseId(String id){

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Query pendingTasks = databaseReference.orderByChild("id").equalTo("random");

        pendingTasks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    snapshot.getRef().child("id").setValue(studentID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void getCities(final String refGov) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.CITIES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            mCitiesList = new ArrayList<>();
                            mCitiesStringList = new ArrayList<>();

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                Cities city = new Cities();

                                JSONObject product = array.getJSONObject(i);

                                city.setId(product.getInt("id"));
                                city.setName(product.getString("name"));
                                city.setRef_gov(product.getString("ref_gov"));
                                mCitiesList.add(city);


                            }

                            for(int i = 0; i < mCitiesList.size(); i++){

                                mCitiesStringList.add(mCitiesList.get(i).getName().toString());
                            }


                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Home.this, R.layout.spinner_item, mCitiesStringList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
                            citiesSpinner.setAdapter(spinnerArrayAdapter);


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

                params.put("refgov" , refGov);

                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

    private void getLevels() {

        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Constants.LEVELS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {

                            mLevelsList = new ArrayList<>();
                            mLevelsStringList = new ArrayList<>();
//                            mLevelsList.add(new Levels(0 , "Select Your level"));

                            JSONArray array = new JSONArray(response);

                            for(int i = 0; i < array.length(); i++) {

                                Levels level = new Levels();

                                JSONObject product = array.getJSONObject(i);

                                level.setStage_id(product.getInt("stage_id"));
                                level.setStage_name(product.getString("stage_name"));
                                mLevelsList.add(level);

                                //  or
                                //
                                // mDaysList.add(product.getString("day"));
                                //
                                // and delete the mDaysListStrinlist

                            }

                            for(int i =0 ; i < mLevelsList.size(); i++){

                                mLevelsStringList.add(mLevelsList.get(i).getStage_name().toString());
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Home.this, R.layout.spinner_item, mLevelsStringList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
                            levelSpinner.setAdapter(spinnerArrayAdapter);

                        }catch (JSONException e) {
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

    private void getSubjects(final int stage_id) {

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.SUBJECTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            mSubjectsList = new ArrayList<Subjects>();
                            mSubjectsStringList = new ArrayList<>();

                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {

                                Subjects subjects = new Subjects();

                                JSONObject product = array.getJSONObject(i);

                                subjects.setC_id(product.getInt("c_id"));
                                subjects.setC_name(product.getString("c_name"));
                                subjects.setStage_id(product.getString("stage_id"));
                                mSubjectsList.add(subjects);



                            }

                            for(int i = 0; i < mSubjectsList.size(); i++){

                                mSubjectsStringList.add(mSubjectsList.get(i).getC_name().toString());
                            }


                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Home.this, R.layout.spinner_item, mSubjectsStringList);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
                            subjectSpinner.setAdapter(spinnerArrayAdapter);


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

                params.put("stage_id" , String.valueOf(stage_id));

                return params;
            }
        };

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }

    private void RegisterInGroup(){


        //  getting data from Edit Text and putting it into those Variabls as Strings
//        final String Hname = name.getText().toString().trim();
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
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL_REGISTER,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                            // Start a new Act when the registeration is done
                            startActivity(new Intent(Home.this , UserLogin.class));

                            //update Token
//                            updateTokenToServer();


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
                params.put("name" , "");
                params.put("email" , "");
                params.put("password" , "");
                params.put("phone" , "");
                params.put("Haddress" , "");
                params.put("HresponsibleReg" , "");
                params.put("HresponsibleReg" , "");
                params.put("HresponsilbePhoneReg" , "");

                return params;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(request);

    }

    private void updateView(String language) {

        Context context = LocaleHelper.setLocale(this , language);

        Resources resources = context.getResources();

//        hel.setText(resources.getString(R.string.hello));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
            startActivity(new Intent(Home.this , LoginOrChat.class));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();

        // Define the Login Text in home Act that takes us to the Login Act
       if (id == R.id.groupRegisterRequests) {



          // Makin an intent to takes us from home to Login
          Intent intent = new Intent(Home.this, RequestedGroups.class);
          startActivity(intent);

        }else if (id == R.id.my_score) {


            // Makin an intent to takes us from home to Login
          Intent intent = new Intent(Home.this, EvualtionAct.class);
          intent.putExtra("LOGINTYPE" ,"STUDENT");
          startActivity(intent);

        }else if (id == R.id.singup_for_someone) {


            // Makin an intent to takes us from home to Login
          Intent intent = new Intent(Home.this, RegisterFor.class);
          intent.putExtra("LOGINTYPE" ,"STUDENT");
          startActivity(intent);

        } else if (id == R.id.my_attendence) {


            // Makin an intent to takes us from home to Login
          Intent intent = new Intent(Home.this, AttendenceAct.class);
          intent.putExtra("LOGINTYPE" ,"STUDENT");
          startActivity(intent);

        }else if (id == R.id.alreadyRegisteredGroups) {


          // Makin an intent to takes us from home to Login
          Intent intent = new Intent(Home.this, ResgisteredGroups.class);
          startActivity(intent);

        }else if (id == R.id.askTeacherM) {


            // Makin an intent to takes us from home to Login
            Intent intent = new Intent(Home.this, AskTeacher.class);
            intent.putExtra("LOGINTYPE" ,"PARENT");
            startActivity(intent);

        }else if (id == R.id.teachersToChat) {


            // Makin an intent to takes us from home to Login
            Intent intent = new Intent(Home.this, ChatTeacher.class);
            intent.putExtra("LOGINTYPE" ,"PARENT");
            startActivity(intent);
        }
       else if (id == R.id.platform) {


           if(Constants.isConnectedToInternet(this)){
               Intent intent = new Intent(Home.this, PlatForm.class);
               startActivity(intent);

           }else {

               Toast.makeText(this, "no internet!", Toast.LENGTH_SHORT).show();
           }


        } else if (id == R.id.privacy_page) {


           try {

               Uri uri = Uri.parse("https://rozewail.com/privacy-policy");
               Intent intent = new Intent(Intent.ACTION_VIEW, uri);
               startActivity(intent);


           } catch (Exception e) {

               e.printStackTrace();
           }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void gout() {

        SharedPrefManager.getInstance(this).logout();
        finish();
        startActivity(new Intent(this, UserLogin.class));

    }

}
