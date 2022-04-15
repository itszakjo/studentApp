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
import zakjo.studentsapp.Helpers.RequestedGroupsAdapter;
import zakjo.studentsapp.Helpers.SharedPrefManager;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.MRequestedGroups;

public class RequestedGroups extends AppCompatActivity {


    CompositeDisposable compositeDisposable = new CompositeDisposable();
    MyLabAPI myLabAPI ;

    List<MRequestedGroups> requestedGroupsList;

    RecyclerView recyclerView_RequestedGroups;

    String studentId  , LoginType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_groups);




        Intent intent = getIntent();

        if(intent !=null) {


            LoginType = intent.getStringExtra("LOGINTYPE");

        }
        myLabAPI = Constants.getAPI();


        recyclerView_RequestedGroups = findViewById(R.id.recylcerView_Requested);
        recyclerView_RequestedGroups.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false));
        recyclerView_RequestedGroups.setHasFixedSize(true);


//        requestedGroupsList = new ArrayList<>();


        if(Constants.LOGIN_TYPE.equals("PARENT")){

            studentId = Constants.currentChild.getSt_id();

        }else {

            studentId = SharedPrefManager.getInstance(this).getID();

        }

//        RequestedGroupsAdapter adapter = new RequestedGroupsAdapter(this , requestedGroupsList);
//        recyclerView_RequestedGroups.setAdapter(adapter);

        loadReuestedGroups(studentId);
    }

    private void loadReuestedGroups(String id) {

        compositeDisposable.add(myLabAPI.getRequestedGroups(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MRequestedGroups>>() {
                    @Override
                    public void accept(List<MRequestedGroups> mRequestedGroups) throws Exception {

                        displayRequests(mRequestedGroups);

                    }

                }));


    }

    private void displayRequests(List<MRequestedGroups> mRequestedGroups) {


        RequestedGroupsAdapter adapter = new RequestedGroupsAdapter(this , mRequestedGroups);
        recyclerView_RequestedGroups.setAdapter(adapter);


    }


}
