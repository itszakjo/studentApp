package zakjo.studentsapp.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.List;

import zakjo.studentsapp.Helpers.DatabaseHelper;
import zakjo.studentsapp.Helpers.UsersAdapter;
import zakjo.studentsapp.R;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Contacts;
import zakjo.studentsapp.model.User;

import static zakjo.studentsapp.Utils.Constants.contactsList;


public class UsersFragment extends Fragment {

    RecyclerView contactsRecycler ;

    EditText searchUsers ;

    public String convertedNumber ;

    final int RC_PERMISSION_CODE = 45;

    List<Contacts> PhonecontactsList ;

    DatabaseReference databaseReference ;

    DatabaseHelper myDB ;

    Button sync ;

    ProgressDialog progressDialog ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_users, container, false);

        sync = view.findViewById(R.id.sync_contacts);

        contactsRecycler = view.findViewById(R.id.contactsRecylcerView);
        contactsRecycler.setHasFixedSize(true);
        contactsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        myDB = new DatabaseHelper(getContext());

        PhonecontactsList = new ArrayList<>();

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Constants.isConnectedToInternet(getContext())){

                    progressDialog  = new ProgressDialog(getContext());
                    progressDialog.setMessage("Syncing Contacts..");
                    progressDialog.show();

                    FetchContacts();

                }else {

                    Toast.makeText(getContext(), "No Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        displayContacts();



        return view;
    }

    private void getContacts(){

        if (ActivityCompat.checkSelfPermission(getContext() , Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED ){

            requestPermission();
        }
        else {

            PhonecontactsList.clear();

            ContentResolver cr = getActivity().getApplicationContext().getContentResolver();

            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);

            if ((cur != null ? cur.getCount() : 0) > 0) {

                while (cur != null && cur.moveToNext()) {

                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

                    final String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    if (cur.getInt(cur.getColumnIndex(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",new String[]{id}, null);

                        while (pCur!=null && pCur.moveToNext()) {

                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                            if(!(phoneNo == null)){

                                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                                try {

                                    Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(phoneNo, "EG");

                                    convertedNumber  = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.E164);

                                }catch(NumberParseException e) {

                                    System.err.println("NumberParseException was thrown: " + e.toString());
                                }


//
//                                System.out.println("Name:" + name);
//                                System.out.println("Phone Number:" + convertedNumber);
//
//                                System.out.println("Name Size: " + mNames.size());
//                                System.out.println(" Number Size: " + mNumbers.size());
                                PhonecontactsList.add(new Contacts("" , name , "" , convertedNumber , "" , "" , ""  , ""));

                            }
                        }
                        pCur.close();
                    }
                }
            }
            if(cur!=null){
                cur.close();
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
//                        for(String phoneNumber : mNumbers){
//                            // if this user id eqauals the id we saved
//                            if(user.getPhone().equals(phoneNumber)){
//                                // if the user doesn't exist in the list
//                                if (!contactsList.contains(user)) {
//                                    // add them
//                                    contactsList.add(user);
//                                }
//                            }
//                        }

                        for(Contacts contacts : PhonecontactsList){
                            // if this user id eqauals the id we saved
                            if(user.getPhone().equals(contacts.getPhone())){
                                // if the user doesn't exist in the list
                                if (!contactsList.contains(user)) {
                                    // add them
                                    contactsList.add(new User(
                                            user.getId() ,
                                            contacts.getUsername() ,
                                            user.imageURL ,
                                            user.getStatus() ,
                                            user.getPhone(),
                                            user.getTyping_to() ,
                                            user.getSearch() ,
                                            "" ,
                                            user.getTimestamp() ,
                                            ""));
                                }
                            }
                        }

                        UsersAdapter usersAdapter = new UsersAdapter(getContext(), contactsList  , false);
                        contactsRecycler.setAdapter(usersAdapter);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }  // unused function

    private static final String[] PROJECTION = new String[] {
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
    };


    public void FetchContacts() {

        if (ActivityCompat.checkSelfPermission(getContext() , Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED ){

            if(progressDialog !=null){ progressDialog.dismiss(); }

            requestPermission();

        }
        else {

            PhonecontactsList.clear();

            ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
            PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

            if (cursor != null) {
                try {
                    final int nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    final int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

                    String name, number;
                    while (cursor.moveToNext()) {

                        name = cursor.getString(nameIndex);
                        number = cursor.getString(numberIndex);
                        try {

                            Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(number, "EG");
                            convertedNumber = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.E164);

                        } catch (NumberParseException e) {
                            e.printStackTrace();
                        }

                        PhonecontactsList.add(new Contacts("", name, "", convertedNumber, "", "", "", ""));
                    }

                } finally {
                    cursor.close();
                }
            }

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    contactsList.clear();

                    // this algorithm means that if there is one million records it will loop through all of them
                    // then add all of them to the list
                    // takes lots of time actually :(

                    /*
                    // the only way to check on the whole database users is to  all of them
//                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                        User user  = snapshot.getValue(User.class);
//
//                        for(Contacts contacts : PhonecontactsList){
//                            // if this user id eqauals the id we saved
//                            if(user.getPhone().equals(contacts.getPhone())){
//                                // if the user doesn't exist in the list
//                                if (!contactsList.contains(user)) {
//                                    // add them
//                                    contactsList.add(new User(
//                                            user.getId(),
//                                            contacts.getUsername(),
//                                            user.imageURL,
//                                            user.getStatus(),
//                                            user.getPhone(),
//                                            user.getTyping_to(),
//                                            user.getSearch(),
//                                            user.getTimestamp()
//
//                                    ));
//                                }
//
//                                // add them to sqlite database of your own phone
//                                // so each time it only loads the contacts from your sqlite
//                                // and if the user wanna sync contacts , they can click a button to sync contacts already ,
//                                // it may take a time but it only happens a week
//                                // orrr there could be a button to sync contacts ,
//                                // and u can run the function everyday at a specific time tho as a background task
//                            }
//                        }
//
//
//                    } // for data snapshot close
*/


                    // new solution for better performance !
                    // another solution is to send single request for each contact to check whether they're in the database or not
                    // by using .exists() method ;)

                    for(Contacts contact:PhonecontactsList){
                        // needs imporvements , it will loop through the whole db as well
                        if (dataSnapshot.child(contact.getPhone()).exists()) {

                            myDB.updateOrInsert(contact.getUsername(),contact.getPhone());

                        }else{

                            System.out.println(contact.getPhone() +"Doesn't Exist! " + contact.getUsername());
                        }
                    }
                    displayContacts();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });

        }//close else
    }

    private void displayContacts(){

        contactsList.clear();

        Cursor data = myDB.getListContents();
        if(data.getCount() == 0){


//            FetchContacts();

            if(progressDialog !=null){ progressDialog.dismiss(); }

            Toast.makeText(getContext(), "No Contacts!", Toast.LENGTH_LONG).show();

        }else{

            while(data.moveToNext()){

                User user = new User();

                user.setId(data.getString(0));
                user.setPhone(data.getString(1));
                user.setUsername(data.getString(2));
                user.setUser_type("onechat");

                contactsList.add(user);

            }

            UsersAdapter usersAdapter = new UsersAdapter(getContext(),contactsList,false);

            contactsRecycler.setAdapter(usersAdapter);

            if(progressDialog !=null){ progressDialog.dismiss(); }

        }

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.READ_CONTACTS
        }, RC_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){

            case RC_PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    FetchContacts();

                    Toast.makeText(getContext(), "we got it ", Toast.LENGTH_SHORT).show();

                }else {


                    progressDialog.dismiss();
                }
            }
            break;
        }


    }


    private void SearchUsers(String s) {

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                contactsList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    if(!user.getPhone().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())){

                        contactsList.add(user);
                    }
                }

                UsersAdapter usersAdapter = new UsersAdapter(getContext() , contactsList , false);

                contactsRecycler.setAdapter(usersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    private void loadUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (searchUsers.getText().toString().equals("")) {
                    contactsList.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        assert user != null;
                        assert firebaseUser != null;

                        if (!user.getId().equals(firebaseUser.getUid())) {

//                            contactsList.add(user);

                        }
                    }

                    UsersAdapter usersAdapter = new UsersAdapter(getContext(), contactsList , false);
                    contactsRecycler.setAdapter(usersAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
