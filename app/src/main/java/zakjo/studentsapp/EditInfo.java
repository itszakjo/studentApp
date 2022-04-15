package zakjo.studentsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import zakjo.studentsapp.Helpers.RequestHandler;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Utils.Constants;


public class EditInfo extends AppCompatActivity   {



    private EditText
            name ,
            address ,
            homePhoneReg   ,
            studentPhone ,
            email ,
            socialLink ,
            school ,
            parentName ,
            parentJob ,
            parentPhone ,
            parentRelation ,
            notes ;

    ImageView editImage;

    RadioButton radioMale , radioFemale ;

    RadioGroup gender;

    Bitmap bitmap ;

    Button updateData ;

    String studentGender ="" ;

    public String UserId ;

    private int PICK_IMAGE_REQUEST = 1;



    String  LoginType = "" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);


        Intent intent = getIntent();

        if(intent !=null) {


            LoginType = intent.getStringExtra("LOGINTYPE");

        }

        name = (EditText)findViewById(R.id.namereg);
        address = (EditText)findViewById(R.id.addressReg);
        homePhoneReg = (EditText)findViewById(R.id.homePhoneReg);
        studentPhone = (EditText)findViewById(R.id.studentPhone);
        email = (EditText)findViewById(R.id.Email);
        socialLink = (EditText)findViewById(R.id.socialLink);
        school = (EditText)findViewById(R.id.school);
        parentName = (EditText)findViewById(R.id.parentName);
        parentJob = (EditText)findViewById(R.id.parentJob);
        parentPhone = (EditText)findViewById(R.id.parentPhone);
        parentRelation = (EditText)findViewById(R.id.parentRelation);
        notes = (EditText)findViewById(R.id.notes);

        editImage = (ImageView)findViewById(R.id.profileImageReg);

        if(Constants.LOGIN_TYPE.equals("PARENT")){

            editImage.setImageResource(R.drawable.person);

        }else {

            if (SharedPrefManager.getInstance(this).getKeyStudentImage().equals("")) {
                editImage.setImageResource(R.drawable.person);
            } else {

                Glide.with(this).load(Constants.ROOT_URL + SharedPrefManager.getInstance(this).getKeyStudentImage()).into(editImage);

            }

        }
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showFileChooser();

            }
        });

        radioMale = (RadioButton)findViewById(R.id.radioMale);
        radioFemale = (RadioButton)findViewById(R.id.radioFemale);

        if(Constants.LOGIN_TYPE.equals("PARENT")){

            if(Constants.currentChild.getSt_gender() !=null) {


                if (Constants.currentChild.getSt_gender().equals("Male")) {

                    radioMale.setChecked(true);

                } else {

                    radioFemale.setChecked(true);
                }

            }

        }else {

            if(SharedPrefManager.getInstance(this).getKeyStudentGender().equals("Male")){

                radioMale.setChecked(true);

            }else{

                radioFemale.setChecked(true);
            }
        }



        if(Constants.isConnectedToInternet(this)){





            if(Constants.LOGIN_TYPE.equals("PARENT")){

                UserId = Constants.currentChild.getSt_id();

                name.setText(Constants.currentChild.getSt_name());
                address.setText(Constants.currentChild.getSt_address());
                homePhoneReg.setText(Constants.currentChild.getSt_telephone());
                studentPhone.setText(Constants.currentChild.getSt_mobile());
                email.setText(Constants.currentChild.getSt_email());
                socialLink.setText(Constants.currentChild.getSt_facebook());
                school.setText(Constants.currentChild.getSt_school());
                parentName.setText(Constants.currentChild.getSt_responsible_name());
                parentJob.setText(Constants.currentChild.getSt_responsible_job());
                parentPhone.setText(Constants.currentChild.getSt_responsible_telephone());
                parentRelation.setText(Constants.currentChild.getSt_responsible_relation());
                notes.setText(Constants.currentChild.getNotes());

            }else {
                UserId = SharedPrefManager.getInstance(this).getID();
                name.setText(SharedPrefManager.getInstance(this).getKeyStudentName());
                address.setText(SharedPrefManager.getInstance(this).getKeyStudentAddress());
                homePhoneReg.setText(SharedPrefManager.getInstance(this).getKeyStudentTelephone());
                studentPhone.setText(SharedPrefManager.getInstance(this).getKeyStudentMobile());
                email.setText(SharedPrefManager.getInstance(this).getKeyStudentEmail());
                socialLink.setText(SharedPrefManager.getInstance(this).getKeyStudentSociallink());
                school.setText(SharedPrefManager.getInstance(this).getKeyStudentSchool());
                parentName.setText(SharedPrefManager.getInstance(this).getKeyStudentResponsibleName());
                parentJob.setText(SharedPrefManager.getInstance(this).getKeyStudentResponsibleJob());
                parentPhone.setText(SharedPrefManager.getInstance(this).getKeyStudentResponsiblePhone());
                parentRelation.setText(SharedPrefManager.getInstance(this).getKeyStudentResponsibleRelation());
                notes.setText(SharedPrefManager.getInstance(this).getKeyStudentNotes());

            }
        }else {

            Toast.makeText(this, "No Internet!", Toast.LENGTH_SHORT).show();
        }



        updateData = (Button) findViewById(R.id.update);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Constants.isConnectedToInternet(EditInfo.this)){

                    updateDataN();
                    uploadBitmap(bitmap);

                }else {

                    Toast.makeText(EditInfo.this, "No Internet!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            try {

                 bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                editImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {


        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,Constants.UPDATE_IMAGE ,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
//                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("student_id", UserId);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }



    private void updateDataN(){

        Constants.SECdialog = new ProgressDialog(EditInfo.this);
        Constants.SECdialog.setMessage("Loading..");
        Constants.SECdialog.show();


        if(radioFemale.isChecked()){

            studentGender = "Female";

        }else if(radioMale.isChecked()){

            studentGender = "Male";
        }


        //  getting data from Edit Text and putting it into those Variabls as Strings
        final String Hname = name.getText().toString();
        final String Haddress = address.getText().toString();
        final String HhomePhoneReg = homePhoneReg.getText().toString();
        final String HstudentPhone = studentPhone.getText().toString();
        final String Hemail = email.getText().toString().trim();
        final String HsocialLink = socialLink.getText().toString();
        final String Hschool = school.getText().toString().trim();
        final String HparentName = parentName.getText().toString();
        final String HparentJob = parentJob.getText().toString();
        final String HparentPhone = parentPhone.getText().toString();
        final String HparentRelation = parentRelation.getText().toString();
        final String Hnotes = notes.getText().toString();



        // builing  a  request with method Post because we are inserting data
        // if we getting data we use method GET
        // then we go to the URL_REGISTER variable in Class Constants
        StringRequest request = new StringRequest(Request.Method.POST,
                Constants.UPDATE_STUDENT_INFO,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);

//                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            Toast.makeText(EditInfo.this, "Data Updated Successfully!", Toast.LENGTH_SHORT).show();
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
//
                params.put("name" , Hname);
                params.put("gender" , studentGender);
                params.put("address" , Haddress);
                params.put("photo" , "");
                params.put("homePhoneReg" , HhomePhoneReg);
                params.put("studentPhone" , HstudentPhone);
                params.put("email" , Hemail);
                params.put("socailLink" , HsocialLink);
                params.put("school" , Hschool);
                params.put("parentName" , HparentName);
                params.put("parentJob" , HparentJob);
                params.put("parentPhone" , HparentPhone);
                params.put("parentRelation" , HparentRelation);
                params.put("notes" , Hnotes);
                params.put("id" , String.valueOf(UserId));



                return params;
            }
        };



        RequestHandler.getInstance(this).addToRequestQueue(request);






    }

}
