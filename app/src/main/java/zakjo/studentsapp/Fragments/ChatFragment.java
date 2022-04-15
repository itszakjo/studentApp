package zakjo.studentsapp.Fragments;

import android.database.Cursor;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import zakjo.studentsapp.Helpers.DatabaseHelper;
import zakjo.studentsapp.Helpers.UsersAdapter;
import zakjo.studentsapp.R;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Chat;
import zakjo.studentsapp.model.ChatContacts;
import zakjo.studentsapp.model.GroupChats;
import zakjo.studentsapp.model.GroupMembers;
import zakjo.studentsapp.model.TalkingTo;
import zakjo.studentsapp.model.User;

import static zakjo.studentsapp.Utils.Constants.contactsList;


public class ChatFragment extends Fragment {

    private RecyclerView chatsRecycler ;

    private List<User> mUsers ;

    FirebaseUser firebaseUser ;
    DatabaseReference databaseReference ;

    private  List<String> userStringlist ;

    DatabaseHelper myDB ;

    String userID , myID , myPhone , userPhone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_chat, container, false);

        chatsRecycler = view.findViewById(R.id.chatsRecycler);
        chatsRecycler.setHasFixedSize(true);

        //chatsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        chatsRecycler.setLayoutManager(linearLayoutManager);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        myDB = new DatabaseHelper(getContext());

        myID = auth.getCurrentUser().getUid();
        myPhone = auth.getCurrentUser().getPhoneNumber();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userStringlist = new ArrayList<>();
        Constants.unReadStringlist = new ArrayList<>();

        // load all from local DB
        fetchFromLocalDb();

        // this is a heavy algorithm since it needs to loop through the whole chat ,
        // then filter the chat and add the filtered ones to array list
        // then loop again through the whole users again!
        // then filter the users then display !
        // displayLatestChats();


        // load all the contacts u talked with or they talked with u
        fetchLatestChat();
        // load all the groups u r in
        fetchLatestGroupChats();

        return view;
    }

    private void fetchLatestChat() {

        databaseReference = FirebaseDatabase.getInstance().getReference("TalkingTo").child(myPhone);
        Query userQuery = databaseReference.orderByChild("timestamp").limitToLast(200);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    TalkingTo talkingTo  = snapshot.getValue(TalkingTo.class);

                    // if there r some parameters in my talkingto list , but there is no lastMessage aka there is no chat between us then don't add this one to the local database ,
                    if(talkingTo.getLastMessage() != null ){

                        if(talkingTo.getImageURL() == null || talkingTo.getImageURL().equals("")){ talkingTo.setImageURL("default");}

                        myDB.updateOrInsertInTalkingTo(talkingTo.getImageURL(),talkingTo.getLastMessage(), talkingTo.getName() , talkingTo.getPhone() , talkingTo.getStatus() , talkingTo.getTimestamp() , talkingTo.getTyping_to() , "onechat");

                    }
                    // mUsers.add(new User("" , talkingTo.getName() , talkingTo.getImageURL() , talkingTo.getStatus() , talkingTo.getPhone() , talkingTo.getTyping_to() , "" , talkingTo.getLastMessage()  , talkingTo.getTimestamp() , "onechat"));

                }
                fetchFromLocalDb();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    private void fetchLatestGroupChats() {


        final List<GroupChats> listRes = new ArrayList<>();

        //wil need some improvements later , to create many to many relation
        // probably won't need hehe

        // this algorithm loops through all the groups in the database:(  ,
        // checks whether u r a member in the group members ,
        // then displays the group if u r in it
        databaseReference = FirebaseDatabase.getInstance().getReference("Groups");
        Query userQuery = databaseReference.orderByChild("timestamp").limitToLast(200);
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    String gid = snapshot.child("phone").getValue(String.class);
                    String gadmin = snapshot.child("admin").getValue(String.class);
                    String imageurl = snapshot.child("imageURL").getValue(String.class);
                    String lastmessage = snapshot.child("lastMessage").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String timesamp = snapshot.child("timestamp").getValue(String.class);

                    // if you exist in the group members or an admin, display the group
                    if(snapshot.child("members").child(myPhone).exists() || gadmin.equals(myPhone)  ){

                        myDB.updateOrInsertInTalkingTo(imageurl ,lastmessage ,name, phone , "" , timesamp ,"" , "groupchat" );

                         fetchFromLocalDb();
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void fetchFromLocalDb(){

        mUsers  =  new ArrayList<>();

        Cursor data = myDB.getListTalkingTo();
        if(data.getCount() == 0){

            Toast.makeText(getContext(), "There are no contents in this list!", Toast.LENGTH_LONG).show();

        }else{
            while(data.moveToNext()){

                User user = new User();

                user.setImageURL(data.getString(1) );
                user.setLastMessage(data.getString(2));
                user.setUsername(data.getString(3));
                user.setPhone(data.getString(4));
                user.setStatus(data.getString(5));
                user.setTimestamp(data.getString(6));
                user.setTyping_to(data.getString(7));
                user.setUser_type(data.getString(8)); // onechat or groupchat

                mUsers.add(user);
            }
            UsersAdapter usersAdapter = new UsersAdapter(getContext(), mUsers , true);
            chatsRecycler.setAdapter(usersAdapter);
        }

    }




    // unsed funcs
    private void displayLatestChats() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        Query chatQuery = databaseReference.orderByChild("timestamp" );

        chatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userStringlist.clear();

                // it is gonna loop through the whole chat ! ,
                // if we got billion chat msgs which is possible to it is gonna loop through all of them first ,
                // and that will happen each time ,
                // VERY BAD ALGORITHM !!

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat = snapshot.getValue(Chat.class);

                    // if i sent him any message , add them to the string list
//                    if(chat.getSender().equals(myPhone)){
//
//                        userStringlist.add(chat.getReciever());
//                    }
                    // if i received any message , add them to the string list
//                    if(chat.getReciever().equals(myPhone)){
//
//                        userStringlist.add(chat.getSender());
//
//                    }

                }
                loadChats();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    } // unused

    private void loadChats() {

        mUsers  =  new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        Query userQuery = databaseReference.orderByChild("timestamp" ).limitToLast(200);

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mUsers.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    User user  = snapshot.getValue(User.class);

                    // this algorithm means that if there is one million msgs it will loop through all of them
                    // then add all of them to the list
                    // takes lots of time actually :(

                    // check on those users to load them
                    for(String phoneNumber : userStringlist){

                        // if this user id eqauals the id we saved
                        if(user.getPhone().equals(phoneNumber)){

                            // if the user doesn't exist in the list
                            if (!mUsers.contains(user)) {
                                    // add them
                                mUsers.add(user);
                            }
                        }
                    }
                }

                UsersAdapter usersAdapter = new UsersAdapter(getContext(), mUsers , true);
                chatsRecycler.setAdapter(usersAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });


//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////
//////                mContacts.clear();
////                mUsers.clear();
////
////                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
////
////                    User user  = snapshot.getValue(User.class);
////
////                    // this algorithm means that if there is one million msgs it will loop through all of them
////                    // then add all of them to the list
////                    // takes lots of time actually :(
////
////
////                    System.out.println("USERNOW " + user.getPhone() + " : " +user.getTimestamp());
////
////                    // check on those users to load them
////                    for(String phoneNumber : userStringlist){
////
////                        // if this user id eqauals the id we saved
////                        if(user.getPhone().equals(phoneNumber)){
////
////                            // if the user doesn't exist in the list
//////                            if (!mUsers.contains(user)) {
//////                                    // add them
//////                                mUsers.add(user);
//////
//////
//////
//////                            }
////
////                        }
////                    }
////                }
////
////                UsersAdapter usersAdapter = new UsersAdapter(getContext(), mUsers , true);
////                chatsRecycler.setAdapter(usersAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

    } // unused

}
