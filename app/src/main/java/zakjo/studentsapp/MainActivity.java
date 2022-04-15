package zakjo.studentsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.RegionIterator;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Utils.Constants;

public class MainActivity extends AppCompatActivity {

    Button loginToStudentApp , loginToChatApp ;

    private final int REQUEST_LOGIN_CODE = 1000 ;

    ProgressDialog dialog , another;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseAuth  auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            Intent intent = new Intent(MainActivity.this  , LoginOrChat.class);
            startActivity(intent);
            finish();
        }


        if(auth.getCurrentUser() == null){

             dialog = new ProgressDialog(MainActivity.this);
             dialog.setMessage("Loading..");
             dialog.show();

            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(
                                    Arrays.asList(
                                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                                    ))
                            .build(),
                    REQUEST_LOGIN_CODE);

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN_CODE) {

            dialog.dismiss();

            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == RESULT_OK) {

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();

                // insert if not exists , and update if it exists
                DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                HashMap<String , Object> hashMap = new HashMap<>();
                hashMap.put("id" , FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("phone" , FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                hashMap.put("imageURL" , "default");
                hashMap.put("typing_to" , "default");
                hashMap.put("status" , "offline");
                hashMap.put("timestamp" ,ts );
                reference.updateChildren(hashMap);

//                // log user into the app
//                final FirebaseAuth  auth = FirebaseAuth.getInstance();
//                SharedPrefManager.getInstance(getApplicationContext()).userAppLogin(auth.getUid());

                Intent intent = new Intent(MainActivity.this ,LoginOrChat.class);
                startActivity(intent);
                finish();

                Constants.GOLdialog = new ProgressDialog(MainActivity.this);
                Constants.GOLdialog.setMessage("Loading..");
                if(Constants.GOLdialog !=null){ Constants.GOLdialog.show();  }


            }
            else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login","Login canceled");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login","Please Check Your Internet");
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login","An Error Occurred");
                    return;
                }
            }

            Log.e("Login","Unknown sign in response");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(Constants.call !=null){
            Constants.call.hangup();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        if(Constants.call !=null){
            Constants.call.hangup();
        }

    }

}
