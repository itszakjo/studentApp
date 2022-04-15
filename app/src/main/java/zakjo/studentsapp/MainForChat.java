package zakjo.studentsapp;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zakjo.studentsapp.Fragments.ChatFragment;
import zakjo.studentsapp.Fragments.ProfileFragment;
import zakjo.studentsapp.Fragments.UsersFragment;
import zakjo.studentsapp.Helpers.ChooseUsersAdapter;
import zakjo.studentsapp.Helpers.DatabaseHelper;
import zakjo.studentsapp.Helpers.SinchCallClientListener;
import zakjo.studentsapp.Helpers.ViewPagerAdapter;
import zakjo.studentsapp.Rertofit.IFCMService;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.DataMessage;
import zakjo.studentsapp.model.MyResponse;
import zakjo.studentsapp.model.Token;
import zakjo.studentsapp.model.User;

import static zakjo.studentsapp.Utils.Constants.APP_KEY;
import static zakjo.studentsapp.Utils.Constants.APP_SECRET;
import static zakjo.studentsapp.Utils.Constants.ENVIRONMENT;
import static zakjo.studentsapp.Utils.Constants.contactsList;
import static zakjo.studentsapp.Utils.Constants.mNames;
import static zakjo.studentsapp.Utils.Constants.mNumbers;
import static zakjo.studentsapp.Utils.Constants.userPhone;

public class MainForChat extends AppCompatActivity  {

    final int RC_PERMISSION_CODE = 45;

    ImageView logout  , userprofpic ;

    FloatingActionButton newGroup ;

    TextView username ;

    String myPhone , convertedNumber  ;

    DatabaseHelper myDB;

    String timeStamp = "" ;

    DatabaseReference reference ,  referenceSenderMessage , referenceRecieverMessage ;

    StorageReference  imageStorageReference;

    HashMap<String , Object> hashMap , hashMapTWO , hashMapTHREE , senderHashMap  , recieverHashMap;

    ArrayList<Token> tokensList ;

    private StorageTask uploadTask;


    private SinchClient sinchClient;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth  auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() == null){
            startActivity(new Intent(this , MainActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_for_chat);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        FirebaseAuth  auth = FirebaseAuth.getInstance();

        myDB = new DatabaseHelper(this);

        imageStorageReference = FirebaseStorage.getInstance().getReference("uploads");

        reference = FirebaseDatabase.getInstance().getReference();

        hashMap = new HashMap<>();
        hashMapTWO = new HashMap<>();
        hashMapTHREE = new HashMap<>();
        senderHashMap = new HashMap<>();
        recieverHashMap = new HashMap<>();


        if(auth.getCurrentUser() == null){
            startActivity(new Intent(this , MainActivity.class));
        }

        username = findViewById(R.id.username);
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {

            Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber(), "EG");
            String  convertedNumber  = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
            username.setText(convertedNumber);

        }catch (NumberParseException e) {
            e.printStackTrace();
        }

        myPhone = auth.getCurrentUser().getPhoneNumber();

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(myPhone)
                .applicationKey(APP_KEY)
                .applicationSecret(APP_SECRET)
                .environmentHost(ENVIRONMENT)
                .build();
        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.setSupportManagedPush(true);
        sinchClient.start();
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener(MainForChat.this , MainForChat.this));


        newGroup = findViewById(R.id.newGroup);
        newGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCreateGroupNameDialog();
            }
        });

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //
//                DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("Users").child("+201007018949");
//
//                HashMap<String , Object> hashMap = new HashMap<>();
//                hashMap.put("id" , "");
//                hashMap.put("phone" , "+201007018949");
//                hashMap.put("imageURL" , "default");
//                hashMap.put("typing_to" , "default");
//                hashMap.put("status" , "offline");
//                hashMap.put("timestamp" ,"" );
//
//
//                reference.updateChildren(hashMap);

                Constants.GOLdialog = new ProgressDialog(MainForChat.this);
                Constants.GOLdialog.setMessage("Logging out..");
                Constants.GOLdialog.show();

                AuthUI.getInstance().signOut(MainForChat.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                startActivity(new Intent(MainForChat.this , MainActivity.class));
                                finish();
                                Constants.GOLdialog.dismiss();
                            }
                        });


            }
        });

        myPhone = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(new ChatFragment() ,"الدردشة");
        pagerAdapter.addFragment(new UsersFragment(),"جهات الاتصال");
        pagerAdapter.addFragment(new ProfileFragment(),"الملف الشخصي");

        viewPager.setAdapter(pagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        userprofpic = findViewById(R.id.myprofilepic);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

                try {
                    if(user.getImageURL().equals("default") || user.getImageURL().equals("") || user.getImageURL() == null ){

                        userprofpic.setImageResource(R.drawable.ic_person_black_24dp);

                    } else{ Glide.with(MainForChat.this).load(user.getImageURL()).into(userprofpic); }
                }catch (Exception e){e.printStackTrace();}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

       //getContacts();
        updateTokenToServer();


        //        if(Constants.isConnectedToInternet(this)){
//            fetchUnsentMessagesFromLocalDbThenSend();
//        }
    }

    private void showCreateGroupNameDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View itemView = LayoutInflater.from(this).inflate(R.layout.create_group_layout , null);

        final EditText gcName = (EditText)itemView.findViewById(R.id.group_name);

        builder.setTitle("انشاء مجموعة جديدة  ");
        builder.setMessage("  اختر على الاقل شخصين  ");


        builder.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

            }
        });

        builder.setPositiveButton("اضافة", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


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

                Toast.makeText(MainForChat.this , gcName.getText().toString(), Toast.LENGTH_SHORT).show();

                showChooseUsersDialog(gcName.getText().toString());

                dialog.dismiss();


            }
        });

    }

    private void showChooseUsersDialog(final String gName) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View itemView = LayoutInflater.from(this).inflate(R.layout.choose_users_layout , null);

        RecyclerView services_rec  = (RecyclerView)itemView.findViewById(R.id.choose_users_recyler);
        services_rec.setLayoutManager(new LinearLayoutManager(this));
        // make grid with two cloumns in the home activity
        //services_rec.setLayoutManager(new GridLayoutManager(this , 2));
        services_rec.setHasFixedSize(true);

        ChooseUsersAdapter adapter =  new ChooseUsersAdapter(this , contactsList);

        services_rec.setAdapter(adapter);

        builder.setTitle("Users");
        builder.setMessage("Choose at least two users");

        builder.setPositiveButton("add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                Constants.addedUsers.clear();

            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                Constants.addedUsers.clear();


            }
        });


        builder.setView(itemView);
        final AlertDialog dialog = builder.create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference referenceone = FirebaseDatabase.getInstance().getReference();
                String id  = referenceone.child("Groups").push().getKey();
                referenceone = FirebaseDatabase.getInstance().getReference("Groups").child(id);

                HashMap<String , Object> hashMapone = new HashMap<>();
                hashMapone.put("admin" , myPhone);
                hashMapone.put("imageURL" , "default");
                hashMapone.put("lastMessage" , "Group just created");
                hashMapone.put("name" , gName);
                hashMapone.put("phone" , id);
                hashMapone.put("timestamp" , String.valueOf(System.currentTimeMillis()));
                referenceone.updateChildren(hashMapone);


                DatabaseReference referenceTwo = FirebaseDatabase.getInstance().getReference();
                HashMap<String , Object> hashMapTwo = new HashMap<>();
                referenceTwo = FirebaseDatabase.getInstance().getReference("Groups").child(id).child("members");

                for (String phones: Constants.addedUsersID ) {
                    hashMapTwo.put(phones , phones );
                }
                referenceTwo.updateChildren(hashMapTwo);



                Constants.addedUsers.clear();
                Constants.addedUsersID.clear();

                dialog.dismiss();

            }
        });

    }

    private void fetchUnsentMessagesFromLocalDbThenSend(){

        String theUserPhone , theMessage , theType , theTimeStamp , thechatType;

        Cursor data = myDB.getUnsentMessagesList();

        if(data.getCount() == 0){
            System.out.println("");
        }else {

            while (data.moveToNext()) {

                theUserPhone = data.getString(0);
                theMessage = data.getString(1);
                theType = data.getString(2);
                theTimeStamp = data.getString(3);
                thechatType = data.getString(4);

                if(theType.equals("text")){

                    sendMessae(myPhone,theUserPhone,theMessage,theMessage ,theMessage ,"text" ,theTimeStamp,thechatType);

                }else if(theType.equals("photo")){

                    uploadFile(Uri.parse(theMessage), "photo"  ,"photo" , "photo"  ,theUserPhone , thechatType , theTimeStamp);

                }else if (theType.equals("video")){

                    uploadFile(Uri.parse(theMessage), "video"  ,"video" , "video" , theUserPhone , thechatType , theTimeStamp);
                }
            }
        }

    }

    private void uploadFile(final Uri filURI , final String fileType  ,
                            final String lastMessage , final String notifyMsg, final String thuserphone  , final String chatType , final String theTimeStamp){

        if(filURI != null){

            final StorageReference fileReference = imageStorageReference.child(System.currentTimeMillis() +"." +getFileExtentions(filURI));

            uploadTask = fileReference.putFile(filURI);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot , Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task ) throws Exception {

                    if(!task.isSuccessful()){ throw task.getException(); }

                    return fileReference.getDownloadUrl();


                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task ) {

                    if(task.isSuccessful()){
                        Uri downlaodUri = task.getResult();
                        String mUri = downlaodUri.toString();
                        sendMessae(myPhone,thuserphone,mUri,notifyMsg,lastMessage,fileType,theTimeStamp ,chatType);
                    }else{
                        Toast.makeText(MainForChat.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainForChat.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{

            Toast.makeText(MainForChat.this, "No Image Selected !", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessae(final String sender ,
                            final String reciver,
                            final String message,
                            final String notificationMessage ,
                            final String lastMessage ,
                            String type ,
                            String timeStampsss ,
                            final String chatType){


        if(timeStampsss.equals("")){
            Long tsLong = System.currentTimeMillis(); // time in millie seconds
            timeStamp = tsLong.toString();
        }else {
            timeStamp = timeStampsss;
        }

        if(Constants.isConnectedToInternet(this)){


            DatabaseReference push_message = reference.child("Chats").child(sender).child(reciver).push();

            String push_id  = push_message.getKey();

            Map messageMap = new HashMap();
            messageMap.put("message",message);
            messageMap.put("iseen" , 0);
            messageMap.put("timestamp",timeStamp);
            messageMap.put("type",type);
            messageMap.put("sender",sender);

            Map messageUserMap = new HashMap();

            if(chatType.equals("ONE")){

                String my_ref = "Chats/" + sender + "/" + reciver ;
                String user_ref = "Chats/" + reciver + "/" + sender ;

                messageUserMap.put(my_ref + "/" + "messages" + "/" +"received" + push_id , messageMap);
                messageUserMap.put(user_ref + "/" + "messages" + "/" +"sent"+ push_id , messageMap);

            }else if(chatType.equals("GROUP")) {

                messageUserMap.put("Chats/" +reciver + "/" + "messages" + "/" +"group" + push_id , messageMap);
            }

            reference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    if(chatType.equals("ONE")){

                        referenceSenderMessage = FirebaseDatabase.getInstance().getReference("TalkingTo").child(sender).child(reciver);
                        senderHashMap.put("timestamp" ,timeStamp);
                        senderHashMap.put("lastMessage" ,lastMessage);
                        referenceSenderMessage.updateChildren(senderHashMap);

                        referenceRecieverMessage = FirebaseDatabase.getInstance().getReference("TalkingTo").child(reciver).child(sender);
                        recieverHashMap.put("timestamp" ,timeStamp);
                        recieverHashMap.put("lastMessage" ,lastMessage);
                        referenceRecieverMessage.updateChildren(recieverHashMap);

                        sendNotificationToUserOnText(reciver,notificationMessage);

                    }
                    else if(chatType.equals("GROUP")) {

                        referenceSenderMessage = FirebaseDatabase.getInstance().getReference("Groups").child(reciver);
                        senderHashMap.put("phone" , reciver);
                        senderHashMap.put("timestamp" ,timeStamp);
                        senderHashMap.put("lastMessage" ,lastMessage);
                        referenceSenderMessage.updateChildren(senderHashMap);
                    }
                }
            });

        }
    }

    private void sendNotificationToUserOnText(final String phone ,final String text){

        tokensList = new ArrayList<>();

        try {

            DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("Tokens").child(phone);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    tokensList.clear();
                    Token token  = dataSnapshot.getValue(Token.class);

                    try {

                        String user_token = token.getToken();

                        System.out.println("MSGTOKEEN "+  user_token);

                        Map<String, String> contentSend = new HashMap<>();
                        contentSend.put("title", myPhone);
                        contentSend.put("userphone", myPhone);
                        contentSend.put("call_status", "nope");
                        contentSend.put("message", text);

                        DataMessage dataMessage = new DataMessage();
                        dataMessage.setTo(user_token);
                        dataMessage.setData(contentSend);

                        IFCMService ifcmService = Constants.getFCMService();
                        ifcmService.sendNotification(dataMessage).enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(retrofit2.Call<MyResponse> call, Response<MyResponse> response) {

                                if (response.code() == 200) {

                                    if (response.body().success == 1) {

                                        System.out.println("Notify Sent");

                                    } else {

                                        System.out.println("Notify Failed");
                                    }

                                }

                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {

                            }
                        });


                    }catch (Exception e){

                        e.printStackTrace();
                    }



                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e){e.printStackTrace(); }

    }

    private String getFileExtentions(Uri uri){

        ContentResolver contentResolver  = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void getContacts(){

        if (ActivityCompat.checkSelfPermission(MainForChat.this , Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED ){

            requestPermission();

        }
        else {

            ContentResolver cr = getContentResolver();

            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {
                while (cur != null && cur.moveToNext()) {

                    String id = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts._ID));

                    final String name = cur.getString(
                            cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                        Cursor pCur = cr.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                                new String[]{id}, null);

                        while (pCur!=null && pCur.moveToNext()) {

                            String phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                            if(!(phoneNo == null)){


                                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                                try {

                                    Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(phoneNo, "EG");

                                     convertedNumber  = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.E164) ;

                                }catch (NumberParseException e) {
                                    System.err.println("NumberParseException was thrown: " + e.toString());
                                }

                                mNames.add(name);
                                mNumbers.add(convertedNumber);

                                System.out.println("Name:" + name);
                                System.out.println("Phone Number:" + convertedNumber);

                                System.out.println("Name Size: " + mNames.size());
                                System.out.println(" Number Size: " + mNumbers.size());


                            }
                        }
                        pCur.close();

                    }
                }
            }
            if(cur!=null){
                cur.close();
            }
            if(Constants.GOLdialog !=null){
                Constants.GOLdialog.dismiss();
            }



            DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    contactsList.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                        User user  = snapshot.getValue(User.class);

                        // this algorithm means that if there is one million msgs it will loop through all of them
                        // then add all of them to the list
                        // takes lots of time actually :(

                        for(String phoneNumber : mNumbers){


//                            // if this user id eqauals the id we saved
                            if(user.getPhone().equals(phoneNumber)){
//
//                                System.out.println("DBB " + phoneNumber);

//                                // if the user doesn't exist in the list
                                if (!contactsList.contains(user)) {
                                    // add them
                                    contactsList.add(user);
                                }
//
                            }
                        }





                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_CONTACTS
        }, RC_PERMISSION_CODE);

    }

    private void updateTokenToServer() {

        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(final InstanceIdResult instanceIdResult) {

                        // insert if not exists , and update if it exists
                        DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("Tokens").child(myPhone);
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("phone" , myPhone);
                        hashMap.put("token" , instanceIdResult.getToken());
                        hashMap.put("isServerToken" , "0");

                        reference.updateChildren(hashMap);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainForChat.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case RC_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(MainForChat.this, "Permission Granted", Toast.LENGTH_SHORT).show();

//                    getContacts();




                }else {

                    Toast.makeText(MainForChat.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }


    }

    private void status(String status){

       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myPhone);
       HashMap<String , Object> hashMap = new HashMap<>();
       hashMap.put("status" , status);
       databaseReference.updateChildren(hashMap);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}

