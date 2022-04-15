package zakjo.studentsapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Cities;
import zakjo.studentsapp.model.Groups;
import zakjo.studentsapp.model.Levels;
import zakjo.studentsapp.model.Subjects;
import zakjo.studentsapp.model.Teachers;

public class RegisterUserGroup extends AppCompatActivity {


    Spinner govSpinner , levelSpinner, subjectSpinner , teacherSpinner  , groupSpinner ;

    Button RegisterBtn ;


    int TypeId;

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    MyLabAPI myLabAPI ;


    private ArrayList<Cities> mCitiesList;
    private ArrayList<String> mGovsStringList;

    private ArrayList<Levels> mLevelsList;
    private ArrayList<String> mLevelsStringList;

    private ArrayList<Subjects> mSubjectsList;
    private ArrayList<String> mSubjectsStringList;

    private ArrayList<Teachers> mTeachersList;
    private ArrayList<String> mTeachersStringList;

    private ArrayList<Groups> mGroupsList;
    private ArrayList<String> mGroupsStringsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_group);



        myLabAPI = Constants.getAPI();


        govSpinner = (Spinner) findViewById(R.id.govSpinner);
        levelSpinner = (Spinner) findViewById(R.id.levelSpinner);
        subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        teacherSpinner = (Spinner) findViewById(R.id.teacherSpinner);
        groupSpinner = (Spinner) findViewById(R.id.groupSpinner);


        RegisterBtn = (Button) findViewById(R.id.registerInGroup);


        mCitiesList = new ArrayList<Cities>();
        mGovsStringList = new ArrayList<>();




    }



}
