package zakjo.studentsapp;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.fxn.pix.Pix;
import com.fxn.utility.PermUtil;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zakjo.studentsapp.Helpers.DatabaseHelper;
import zakjo.studentsapp.Helpers.MessagesAdapter;
import zakjo.studentsapp.Helpers.SinchCallClientListener;
import zakjo.studentsapp.Helpers.SinchCallListener;
import zakjo.studentsapp.Helpers.UsersAdapter;
import zakjo.studentsapp.Rertofit.IFCMService;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Chat;
import zakjo.studentsapp.model.DataMessage;
import zakjo.studentsapp.model.MyResponse;
import zakjo.studentsapp.model.Token;
import zakjo.studentsapp.model.User;

import static zakjo.studentsapp.Utils.Constants.APP_KEY;
import static zakjo.studentsapp.Utils.Constants.APP_SECRET;
import static zakjo.studentsapp.Utils.Constants.ENVIRONMENT;
import static zakjo.studentsapp.Utils.Constants.contactsList;
import static zakjo.studentsapp.Utils.Constants.currentTime;
import static zakjo.studentsapp.Utils.Constants.mNumbers;
import static zakjo.studentsapp.Utils.Constants.path;
import static zakjo.studentsapp.Utils.Constants.userPhone;


public class Messaging extends AppCompatActivity {


    TextView userName , user_status , middle_text    ;

    FirebaseUser firebaseUser ;

    DatabaseReference databaseReference ;

    StorageReference  imageStorageReference , audioStorageReference;

    Intent intent ;

    ImageButton sendBtn , pick ,pickFiles ;

    ImageView user_img ;

    ImageButton CallBtn , recordBtn ;

    EditText textArea ;

    RecyclerView messsagesREc ;

    LinearLayoutManager linearLayoutManager;

    MessagesAdapter messagesAdapter ;

    ArrayList<Chat> chatList = new ArrayList<>() ;

    ArrayList<Token> tokensList ;

    String userID , myID , myPhone  , name , timeStamp = "" , imagelink , CHATYPE = "" , USERPHONE = "", pathSave ="";

    ValueEventListener seenListener ;

    private SinchClient sinchClient;

    MediaPlayer mediaPlayer ,koftaMediaPlayer ;

    MediaRecorder mediaRecorder ;

    AudioManager audioManager ;

    private Button  acccept , reject , stop;

    final int RC_PERMISSION_CODE = 55 ;
    int btnHeight  = 0;
    int btnWidth   = 0;


    DatabaseReference reference , referenceTWo , referenceTHREE , referenceSenderMessage , referenceRecieverMessage ;

    HashMap<String , Object> hashMap , hashMapTWO , hashMapTHREE , senderHashMap  , recieverHashMap ;

    boolean isScrolling = false;
    boolean isSpeakButtonLongPressed = false;
    int currentVisibleItems , totalItems , scrolledOut , totalFetch = 250 ;

    DatabaseHelper myDB;

    private static final int IMAGE_RESULT = 1 ;
    private static final int IMAGE_RESULT_GALLERY_PICKER = 2 ;
    private Uri fileUri , audioUri ;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        intent  = getIntent();

        CHATYPE  = intent.getStringExtra("CHATYPE");

        USERPHONE  = intent.getStringExtra("USERPHONE");

        pathSave = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/"
                + UUID.randomUUID().toString() + "_audio_record.3gp";


        Constants.CHAT_TYPE = CHATYPE ;

        FirebaseAuth auth = FirebaseAuth.getInstance();

        try {

            if(!USERPHONE.equals("0")){

                Constants.userPhone = USERPHONE;

            }else{

                Constants.userPhone = Constants.currentUser.getPhone();
            }

            name = Constants.currentUser.getUsername();
            imagelink = Constants.currentUser.getImageURL();

        }catch (Exception e){e.printStackTrace();}

        myID = auth.getCurrentUser().getUid();

        myPhone = auth.getCurrentUser().getPhoneNumber();

        audioStorageReference = FirebaseStorage.getInstance().getReference("voiceNotes");

        imageStorageReference = FirebaseStorage.getInstance().getReference("uploads");

        myDB = new DatabaseHelper(this);

        user_img = (ImageView) findViewById(R.id.user_image);
        try {
            if(imagelink.equals("default")){user_img.setImageResource(R.drawable.bahy);}
            else{ Glide.with(this).load(imagelink).into(user_img);}
        }catch (Exception e){e.printStackTrace();}


        middle_text = (TextView) findViewById(R.id.middle_message);

        userName = (TextView) findViewById(R.id.username);

        user_status = (TextView) findViewById(R.id.user_status);

        sendBtn = (ImageButton) findViewById(R.id.sendMsg);

        pick = (ImageButton) findViewById(R.id.pick);

        pickFiles = (ImageButton) findViewById(R.id.pickFiles);

        textArea = (EditText) findViewById(R.id.textField);

        CallBtn = (ImageButton) findViewById(R.id.callBtn);

        recordBtn = (ImageButton) findViewById(R.id.record);

        try {

            if(CHATYPE.equals("GROUP")){

                CallBtn.setVisibility(View.GONE);
            }

        }catch (Exception e){e.printStackTrace();}


        reference = FirebaseDatabase.getInstance().getReference();

        hashMap = new HashMap<>();
        hashMapTWO = new HashMap<>();
        hashMapTHREE = new HashMap<>();
        senderHashMap = new HashMap<>();
        recieverHashMap = new HashMap<>();

        messsagesREc = (RecyclerView) findViewById(R.id.messagesRecycler);
        messsagesREc.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true); // for scrolling to the last message
        messsagesREc.setLayoutManager(linearLayoutManager);

        fetchMessagesFromLocalDb();

        loadChats(myPhone,Constants.userPhone,totalFetch);

        intent = getIntent();

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
        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener(Messaging.this , Messaging.this));

        path = "android.resource://" + Messaging.this.getPackageName() + "/raw/ringing";
        Constants.uri =  Uri.parse(path);

        //Constants.vnn = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        if(name == null || name.equals("") ){
            userName.setText(Constants.userPhone);
        }else {
            userName.setText(name);
        }

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        try {

            if(CHATYPE.equals("ONE")){

                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Constants.userPhone);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);

                        if(user.getStatus().equals("online") || user.getStatus().equals("typing") || user.getStatus().equals("recording") ){

                            user_status.setText("online");
                        }
                        else if(user.getStatus().equals("calling")) {

                            user_status.setText("On a Call");

                        }else {

                            user_status.setText("offline");
                        }
                        // if the user's field typing_to  equals my Id , then set their status to typing
                        if(user.getTyping_to().equals(myPhone) && user.getStatus().equals("typing")){

                            user_status.setText("typing...");
                        }

                        if(user_status.getText().equals("online")){

                            user_status.setTextColor(Color.parseColor("#634975"));

                        }else if(user_status.getText().equals("offline")){

                            user_status.setTextColor(Color.parseColor("#46000000"));

                        }else if(user_status.getText().equals("typing...")){

                            user_status.setTextColor(Color.RED);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                textArea.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    if(!textArea.equals("")){
//
//                        pick.setVisibility(View.GONE);
//
//                    }else {
//
//                        pick.setVisibility(View.VISIBLE);
//                    }

//                    Long tsLong = System.currentTimeMillis(); // time in millie seconds
//                    ts = tsLong.toString();

                        // or ServerValue.TIMESTAMP

//                        typing_to(Constants.userPhone);
//                        status("typing");
                        // removed from here due to lots of sent requets to the database!

                        if(textArea.getText().toString().trim().isEmpty()){

                            sendBtn.setVisibility(View.GONE);
                            recordBtn.setVisibility(View.VISIBLE);

                        }else {

                            sendBtn.setVisibility(View.VISIBLE);
                            recordBtn.setVisibility(View.GONE);

                        }


                    }
                    @Override
                    public void afterTextChanged(Editable editable) {

//                    if(textArea.equals("")){
//
//                        pick.setVisibility(View.VISIBLE);
//
//                    }else {
//
//                        pick.setVisibility(View.GONE);
//
//                    }

                        typing_to(Constants.userPhone);
                        status("typing");


                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                typing_to("0");

                            }
                        }, 5000);


                    }
                });

            }
            else if(CHATYPE.equals("GROUP")){

                textArea.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                        if(textArea.getText().toString().trim().isEmpty()){

                            sendBtn.setVisibility(View.GONE);
                            recordBtn.setVisibility(View.VISIBLE);

                        }else {

                            sendBtn.setVisibility(View.VISIBLE);
                            recordBtn.setVisibility(View.GONE);

                        }

                    }
                    @Override
                    public void afterTextChanged(Editable editable) {




                    }
                });

                user_status.setText("");
            }

        }catch(Exception e){ e.printStackTrace();}

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  openImagePicker();

            }
        });

        pickFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   openFilesPicker();

            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Constants.isConnectedToInternet(Messaging.this)){

                    String message = textArea.getText().toString();
                    assert firebaseUser != null;
                    if(!message.trim().isEmpty()){
                        // sender , reciever  , msg  , notifmsg , lastmsg  , ..etc
                        sendMessae(myPhone,Constants.userPhone,message,message,message ,"text" ,"");
                        textArea.setText("");
                        typing_to("0");
                        sendBtn.setVisibility(View.GONE);
                        recordBtn.setVisibility(View.VISIBLE);
                    }
                    else { Toast.makeText(Messaging.this, "Empty Chat", Toast.LENGTH_SHORT).show(); }


                }else {

                    Toast.makeText(Messaging.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        CallBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(Messaging.this , Manifest.permission.RECORD_AUDIO)
                                != PackageManager.PERMISSION_GRANTED  ){
                    requestPermission();
                }
                else {

                    if (Constants.call == null) {

                        sendNotificationToUserOnCall(Constants.userPhone);
                        Intent intent = new Intent(Messaging.this,Call3.class);
                        typing_to(Constants.userPhone);
                        status("calling");
                        intent.putExtra("INTENT" , "FROMSG");
                        startActivity(intent);
                        finish();

                        Constants.call = sinchClient.getCallClient().callUser(Constants.userPhone);
                        Constants.call.addCallListener(new SinchCallListener(Messaging.this , Messaging.this));

                    }else {if(Constants.call !=null){ Constants.call.hangup();}}

                    if(Constants.mediaPlayer != null){Constants.mediaPlayer.stop();}
                }
            }
        });



        recordBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
            btnHeight = recordBtn.getHeight();
            btnWidth  = recordBtn.getWidth();

            if (ActivityCompat.checkSelfPermission(Messaging.this , Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(Messaging.this , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED ){

                requestPermission();

            }
            else {

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recordBtn.getLayoutParams();
                params.height = btnHeight + 30;
                params.width = btnWidth + 30;



//                        params.bottomMargin = -80;
//                        params.rightMargin = -80;
                recordBtn.setLayoutParams(params);

                startRecording();

                typing_to(userPhone);
                status("recording..");
            }
            // Do something when your hold starts here.
            isSpeakButtonLongPressed = true;
            return true;
            }
        });

        recordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                // We're only interested in when the button is released.
               if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                   if (isSpeakButtonLongPressed) {

                       RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) recordBtn.getLayoutParams();
                       params.height = btnHeight ;
                       params.width = btnWidth ;

                       recordBtn.setLayoutParams(params);

                       stopRecording();

                       typing_to("");

                       status("online");
                       isSpeakButtonLongPressed = false;
                   }


                }

                return false;
            }
        });


        if(CHATYPE.equals("ONE")){ seenMessage(Constants.userPhone); }

        checkForMiddleText();

//        fetchUnsentMessagesFromLocalDbThenSend();
    }

    private void checkForMiddleText() {

        // for middle text if there is no chat !

        if(chatList.size() == 0){

            middle_text.setVisibility(View.VISIBLE);
            if(CHATYPE.equals("ONE")){
                middle_text.setText("Say Hi to "+ userPhone  + " :)" );
            }else {
                middle_text.setText("Start conversation" );
            }

        }
        else {

            middle_text.setVisibility(View.GONE);
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(Messaging.this, new String[]{
                Manifest.permission.READ_CONTACTS ,
                Manifest.permission.RECORD_AUDIO ,
                Manifest.permission.READ_EXTERNAL_STORAGE ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE ,

        }, RC_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case RC_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){

                    System.out.println("Granted");
                }else {
                    System.out.println("Denied");
                }
            }
            break;
        }
    }

    // edited v1 > done
    private void seenMessage(final String user_phone){

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats")
                .child(user_phone) // user phone
                .child(myPhone) // myphone
                .child("messages");

        Query seenQuery = databaseReference.orderByKey().startAt("received").endAt("received"+"\uf8ff");
        seenListener = seenQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getIseen() == 0){

                         HashMap<String , Object> hashMap = new HashMap<>();
                         hashMap.put("iseen" ,1);
                         snapshot.getRef().updateChildren(hashMap);
                     }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //  edited v1 > done
    private void sendMessae(final String sender,
                            final String reciver,
                            final String message,
                            final String notificationMessage,
                            final String lastMessage,
                            String type,
                            String timeStampsss){


        if(timeStampsss.equals("")){
            Long tsLong = System.currentTimeMillis(); // time in millie seconds
            timeStamp = tsLong.toString();
        }else {
            timeStamp = timeStampsss;
        }

        // old algorithm to send a message ,
        /*
//        reference = FirebaseDatabase.getInstance().getReference();
//        hashMap.put("sender" , sender);
//        hashMap.put("reciever" , reciver);
//        hashMap.put("message" , message);
//        hashMap.put("type" , "text");
//        hashMap.put("iseen" , false);
//        hashMap.put("timestamp" ,ts );
//        reference.child("Chats").push().setValue(hashMap);

        //////////////////////////////////////////////////////

//        referenceSenderMessage = FirebaseDatabase.getInstance().getReference("TalkingTo").child(sender).child(reciver);
//        senderHashMap.put("phone" , reciver);
//        senderHashMap.put("name" , name);
//        senderHashMap.put("imageURL" , "default");
//        senderHashMap.put("timestamp" ,ts);
//        referenceSenderMessage.updateChildren(senderHashMap);

        ///////////////////////////////////////////////////////////////
//        referenceRecieverMessage = FirebaseDatabase.getInstance().getReference("TalkingTo").child(reciver).child(sender);
//        recieverHashMap.put("phone" , sender);
//        recieverHashMap.put("name" , "");
//        recieverHashMap.put("imageURL" , "default");
//        recieverHashMap.put("timestamp" ,ts);
//        referenceRecieverMessage.updateChildren(recieverHashMap);


//        sendNotificationToUserOnText(reciver , message);

*/


        // send msgs offline
        if(type.equals("text")) {

            myDB.updateOrInsertInMessages(reciver,sender,message,type,10,timeStamp ,CHATYPE);
            fetchMessagesFromLocalDb();

        }

        // new algorithm , to increase performance for sending messages !
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

            if(CHATYPE.equals("ONE")){

                String my_ref = "Chats/" + sender + "/" + reciver ;
                String user_ref = "Chats/" + reciver + "/" + sender ;

                messageUserMap.put(my_ref + "/" + "messages" + "/" +"received" + push_id , messageMap);
                messageUserMap.put(user_ref + "/" + "messages" + "/" +"sent"+ push_id , messageMap);

            } else if(CHATYPE.equals("GROUP")) {

                messageUserMap.put("Chats/" +reciver + "/" + "messages" + "/" +"group" + push_id , messageMap);
            }

            reference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                    if(CHATYPE.equals("ONE")){

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
                    else if(CHATYPE.equals("GROUP")) {

                        referenceSenderMessage = FirebaseDatabase.getInstance().getReference("Groups").child(reciver);
                        senderHashMap.put("timestamp" ,timeStamp);
                        senderHashMap.put("lastMessage" ,lastMessage);
                        referenceSenderMessage.updateChildren(senderHashMap);
                    }
                }
            });

        }
        else{
            Toast.makeText(this, "check your internet connection!", Toast.LENGTH_SHORT).show();
        }

    }

    // edited v1 > done > done v2
    private void loadChats(final String myPhone,final String userPhone,final int totFetch) {
        // will also load only 25 messages and load more on scroll
        // that if you're gonna load the whole messages from the server
        //  but if you load all of them from the local db u can load all of them
        // or we can think of doing both of them tho !



//      databaseReference = FirebaseDatabase.getInstance().getReference("Chats");


        // loading my own chats as myphone then userphone ,  and they effect on these messages if it is seen or nah ,
        // then i will count all the messages myphone, userphone , that wasn't seen by them

        if(CHATYPE.equals("ONE")){

            databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(myPhone).child(Constants.userPhone).child("messages");

        }
        else if(CHATYPE.equals("GROUP")){

            databaseReference = FirebaseDatabase.getInstance().getReference("Chats").child(Constants.userPhone).child("messages");
        }
        // need to increase the total fetch i guess since if u have sent the use 200 message when they r offline it will only fetch last 25 msgs ,
        // but it will keep working as long as the user is online and has some internet !
        Query dataQuery = databaseReference.orderByChild("timestamp").limitToLast(totFetch);
        dataQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    // old algorithm
                    /*
//                    if(chat.getReciever().equals(myPhone) && chat.getSender().equals(userPhone)  ||
//                            chat.getReciever().equals(userPhone) && chat.getSender().equals(myPhone))
//                    {
//
//                        chatList.add(chat);
//                    }
                    */

                    //new
                    myDB.updateOrInsertInMessages(Constants.userPhone,chat.getSender(),chat.getMessage(),chat.getType(),chat.getIseen(),chat.getTimestamp(),CHATYPE);

                    // chatList.add(new Chat("" , chat.getSender() ,chat.getMessage() ,chat.getType() , chat.getIseen() , chat.getTimestamp()));
                }

                fetchMessagesFromLocalDb();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}

        });
    }

    private void fetchMessagesFromLocalDb() {

        chatList  =  new ArrayList<>();

        Cursor data = myDB.getMessagesList(userPhone);

        if(data.getCount() == 0){
            System.out.println("");
        }
        else {

            while (data.moveToNext()) {

                Chat chat = new Chat();

                chat.setSender(data.getString(0));
                chat.setMessage(data.getString(1));
                chat.setType(data.getString(2));
                chat.setIseen(data.getInt(3));
                chat.setTimestamp(data.getString(4));

                chatList.add(chat);

            }

        }

        messagesAdapter = new MessagesAdapter(Messaging.this,chatList);
        messsagesREc.setAdapter(messagesAdapter);

        checkForMiddleText();

    }

    private void fetchUnsentMessagesFromLocalDbThenSend(){

        // getting the messages that its iseen equals 10 , if it is 10 it means the message been sent offline
        String theUserPhone , theMessage , theType , theTimeStamp;

        Cursor data = myDB.getUnsentMessagesList();

        if(data.getCount() == 0){
            System.out.println("");
        }else {

            while (data.moveToNext()) {

                theUserPhone = data.getString(0);
                theMessage = data.getString(1);
                theType = data.getString(2);
                theTimeStamp = data.getString(3);

                if(theType.equals("text")){

                    sendMessae(myPhone,theUserPhone,theMessage,theMessage ,theMessage ,"text" ,theTimeStamp);

                }else if(theType.equals("photo")){

                    sendMessae(myPhone,theUserPhone,theMessage,"photo" ,"photo sent" ,"photo" ,theTimeStamp);

                }
                else if (theType.equals("video")){

                    sendMessae(myPhone,theUserPhone,theMessage,"video" ,"video sent" ,"video" ,theTimeStamp);
                }
                else if (theType.equals("audio")){

                    sendMessae(myPhone,theUserPhone,theMessage,"audio" ,"audio sent" ,"video" ,theTimeStamp);
                }
            }
        }

    }

    private void openImagePicker() {

        if (ActivityCompat.checkSelfPermission(Messaging.this , Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  ){
            requestPermission();
        }
        else {

            // for uploading images

//            Options options = Options.init()
//                    .setRequestCode(100)                                                 //Request code for activity results
//                    .setCount(3)                                                         //Number of images to restict selection count
//                    .setFrontfacing(false)                                                //Front Facing camera on start
//                    .setImageQuality(ImageQuality.HIGH)                                  //Image Quality
//                    .setPreSelectedUrls(returnValue)                                     //Pre selected Image Urls
//                    .setScreenOrientation(Options.SCREEN_ORIENTATION_PORTRAIT)           //Orientaion
//                    .setPath("/pix/images");

         Pix.start(Messaging.this, PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS ,10);

        }

    }

    private void openFilesPicker() {

        if (ActivityCompat.checkSelfPermission(Messaging.this , Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED  ){
            requestPermission();
        }
        else {


//          for uploading all files
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            String[] mimetypes = {"image/*", "video/*"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, IMAGE_RESULT); // startActivityForResult to take u back again



        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // uploading images using Pix
        if (resultCode == Activity.RESULT_OK && requestCode == PermUtil.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS ){

            ArrayList<String> returnValues = data.getStringArrayListExtra(Pix.IMAGE_RESULTS);

            for (String s:returnValues){

                fileUri  = Uri.fromFile(new File(s));

                uploadFile(fileUri , "photo"  ,"photo"  , "photo");
            }

            // uploading images & files using the normal way
        }
        else if(requestCode == IMAGE_RESULT && resultCode == RESULT_OK
                && data != null && data.getData() !=null){


            if(data.getData() != null){

                fileUri = data.getData();

                String fileExtention = getFileExtentions(fileUri);

                if(
                        fileExtention.equals("mp4") ||
                        fileExtention.equals("3gp") ||
                        fileExtention.equals("wmv")
                ){

                    uploadFile(fileUri , "video" , "video sent" , "video sent");

                }else if(

                        fileExtention.equals("png") ||
                        fileExtention.equals("jpg") ||
                        fileExtention.equals("jpeg")
                ){

                    uploadFile(fileUri , "photo" , "photo sent" , "photo sent");
                }
                else if(fileExtention.equals("pdf")){

                    uploadFile(fileUri , "pdf" , "pdf sent" , "pdf sent");
                }

            }
        }
    }

    private void startRecording(){

            // upload the file to the phone storage
            setUpMediaRecorder();

            try {

                mediaRecorder.prepare();
                mediaRecorder.start();

            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(Messaging.this, "Recording ...", Toast.LENGTH_SHORT).show();


    }

    private void setUpMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }

    private void stopRecording(){

        try {

            mediaRecorder.stop();

            // upload the record to firebase
            uploadRecord();

        }catch (Exception e){


        }



    }

    private void uploadRecord() {

        if(Constants.isConnectedToInternet(this)) {

            Uri uri = Uri.fromFile(new File(pathSave));

            // important algorithm for uploading files offline  ,working but disabled for now
//            myDB.updateOrInsertInMessages(Constants.userPhone,myPhone,String.valueOf(uri),"audio" ,10 ,timeStamp ,CHATYPE);
            fetchMessagesFromLocalDb();


            if(uri != null) {

                final StorageReference audiofileReference = audioStorageReference.child(System.currentTimeMillis()
                        + "." + audioUri);

                uploadTask = audiofileReference.putFile(uri);

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {

                            throw task.getException();

                        }

                        return audiofileReference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {

                            Uri downlaodUri = task.getResult();
                            String audioUri = downlaodUri.toString();

                            sendMessae(myPhone, userPhone, audioUri, "voice note sent", "voice note sent", "audio", "");

                            Toast.makeText(Messaging.this, "Uploaded", Toast.LENGTH_SHORT).show();

                        } else {

                            Toast.makeText(Messaging.this, "Failed", Toast.LENGTH_SHORT).show();

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Messaging.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }else{

            Toast.makeText(this, "check your internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadFile(final Uri filURI , final String fileType  , final String lastMessage , final String notifyMsg){

        if(Constants.isConnectedToInternet(this)){

            // important algorithm for uploading files offline  ,working but disabled for now
//           myDB.updateOrInsertInMessages(Constants.userPhone,myPhone,String.valueOf(filURI),fileType ,10 ,timeStamp ,CHATYPE);
           fetchMessagesFromLocalDb();

           if(filURI != null){

            final StorageReference fileReference = imageStorageReference.child(System.currentTimeMillis()
                    +"." +getFileExtentions(filURI));

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
                        sendMessae(myPhone,userPhone,mUri,notifyMsg,lastMessage,fileType,timeStamp);

                    }else{
                        Toast.makeText(Messaging.this, "Failed", Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(Messaging.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
           else{

                Toast.makeText(Messaging.this, "No Image Selected !", Toast.LENGTH_SHORT).show();
            }

        }
        else {

            Toast.makeText(this, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtentions(Uri uri){

        ContentResolver contentResolver  = this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void sendNotificationToUserOnCall(final String phone){

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

                        System.out.println("USERTOKEN "+  user_token);

                        Map<String, String> contentSend = new HashMap<>();
                        contentSend.put("title", "New Call");
                        contentSend.put("message", myPhone + " is calling ");
                        contentSend.put("userphone", myPhone);
                        contentSend.put("call_status", "ImCalling");

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
                            public void onFailure(Call<MyResponse> call, Throwable t) { }
                        });

                    }catch (Exception e){ e.printStackTrace(); }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) { }
            });

        }catch (Exception e){ e.printStackTrace(); }

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

    private void status(String status){

        if(CHATYPE.equals("ONE")){

            if(status == null || status.equals("")){ status = "default"; }

            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myPhone);
            HashMap<String , Object> hashMap = new HashMap<>();
            hashMap.put("status" , status);
            databaseReference.updateChildren(hashMap);

            // once I get in the chat I set my info in the user's chatlist  , but it also won't show in their chatlist if lastMessage is null
            referenceTWo = FirebaseDatabase.getInstance().getReference("TalkingTo").child(Constants.userPhone).child(myPhone);
            HashMap<String , Object> hashMapTWO = new HashMap<>();
            hashMapTWO.put("phone" , myPhone);
            hashMapTWO.put("imageURL" , Constants.ProfileImage_Link);
            hashMapTWO.put("status" , status);
            referenceTWo.updateChildren(hashMapTWO);

            // once the I get in the chat I set these parameters for this user in my chat list but it wont show if lastMessage is null
            referenceSenderMessage = FirebaseDatabase.getInstance().getReference("TalkingTo").child(myPhone).child(Constants.userPhone);
            senderHashMap.put("phone" , Constants.userPhone);
            senderHashMap.put("name" , name);
            referenceSenderMessage.updateChildren(senderHashMap);

        }


    }

    private void typing_to(String typing_to){

        if(CHATYPE.equals("ONE")) {

            if (typing_to == null || typing_to.equals("")) {
                typing_to = "default";
            }

            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(myPhone);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("typing_to", typing_to);
            databaseReference.updateChildren(hashMap);

            referenceTWo = FirebaseDatabase.getInstance().getReference("TalkingTo").child(Constants.userPhone).child(myPhone);
            HashMap<String, Object> hashMapTWO = new HashMap<>();
            hashMapTWO.put("typing_to", typing_to);
            referenceTWo.updateChildren(hashMapTWO);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(CHATYPE.equals("ONE")){
            databaseReference.removeEventListener(seenListener);
        }
        status("offline");

    }

    @Override
    public void onBackPressed() {  finish(); }


}
