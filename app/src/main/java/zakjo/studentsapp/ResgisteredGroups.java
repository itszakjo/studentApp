package zakjo.studentsapp;

import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zakjo.studentsapp.Helpers.RegisterdGroupsAdapter;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.MRegisteredGroups;

public class ResgisteredGroups extends AppCompatActivity {


    List<MRegisteredGroups> registeredGroupsList;

    RecyclerView recyclerView_RegisteredGroups;
    String studentId ;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MyLabAPI myLabAPI ;

    String  LoginType = "" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgistered_groups);

        Intent intent = getIntent();

        if(intent !=null) {


            LoginType = intent.getStringExtra("LOGINTYPE");

        }

        myLabAPI = Constants.getAPI();


        recyclerView_RegisteredGroups = findViewById(R.id.recylcerView_registered);
        recyclerView_RegisteredGroups.setLayoutManager(new LinearLayoutManager(ResgisteredGroups.this, LinearLayoutManager.VERTICAL ,false));
        recyclerView_RegisteredGroups.setHasFixedSize(true);


        registeredGroupsList = new ArrayList<>();



        if(Constants.LOGIN_TYPE.equals("PARENT")){

            studentId = Constants.currentChild.getSt_id();

        }else {
            studentId = SharedPrefManager.getInstance(this).getID();

        }
//        registeredGroupsList.add(new MRegisteredGroups("" , "" , "" , "" , "" , "20/10/2019" ,
//                "" ,    "بنات قرى"   , "" , "" , "" ,"" , "" , ""
//        , "" ,"" , "" , "" , "" ,"" ));
//
//
//
//        RegisterdGroupsAdapter adapter = new RegisterdGroupsAdapter(this , registeredGroupsList);
//        recyclerView_RegisteredGroups.setAdapter(adapter);





        loadRegisteredGroups(studentId);
    }




    private void loadRegisteredGroups(String id) {

        compositeDisposable.add(myLabAPI.getRegisteredGroups(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MRegisteredGroups>>() {
                    @Override
                    public void accept(List<MRegisteredGroups> mRegisteredGroups) throws Exception {

                        displayReqisters(mRegisteredGroups);

                    }

                }));


    }

    private void displayReqisters(List<MRegisteredGroups> mRegisteredGroups) {


        RegisterdGroupsAdapter adapter = new RegisterdGroupsAdapter(this , mRegisteredGroups);
        recyclerView_RegisteredGroups.setAdapter(adapter);


    }

}
