package zakjo.studentsapp.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


import zakjo.studentsapp.R;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.User;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    ImageView profile ;
    TextView userName ;

    FirebaseUser firebaseUser ;
    DatabaseReference databaseReference ;


    StorageReference storageReference ;
    private static final int IMAGE_REUEST= 1 ;
    private Uri imageUri ;
    private StorageTask uploadTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);



//      userName = view.findViewById(R.id.username);
        profile = view.findViewById(R.id.profilePic);


        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getPhoneNumber());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

//                    userName.setText(user.getPhone());
                    if(user.getImageURL() == null || user.getImageURL().equals("default")){
                        profile.setImageResource(R.drawable.person);
                    }else {

                        Glide.with(getContext()).load(user.getImageURL()).into(profile);
                        Constants.ProfileImage_Link = user.getImageURL();
                    }

//
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        return view ;
    }

    private void openImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , IMAGE_REUEST);
    }

    private String getFileExtentions(Uri uri){

        ContentResolver contentResolver  = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){


        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUri != null){

            final StorageReference fileReference = storageReference.child( System.currentTimeMillis()
            +"." +getFileExtentions(imageUri) );

            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot , Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {


                    if(!task.isSuccessful()){

                        throw task.getException();

                    }


                    return fileReference.getDownloadUrl();


                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task ) {

                    if(task.isSuccessful()){

                        Uri downlaodUri = task.getResult();
                        String mUri = downlaodUri.toString();

                        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getPhoneNumber());
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("imageURL" , mUri);

                        databaseReference.updateChildren(hashMap);

                        pd.dismiss();



                    }else{

                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });



        }else{

            Toast.makeText(getContext(), "No Image Selected !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REUEST && resultCode == RESULT_OK
                && data != null && data.getData() !=null){

            imageUri = data.getData();
            if(uploadTask !=null && uploadTask.isInProgress()){

                Toast.makeText(getContext(), "Upload in Progress", Toast.LENGTH_SHORT).show();

            }else{

                uploadImage();
            }
        }
    }
}
