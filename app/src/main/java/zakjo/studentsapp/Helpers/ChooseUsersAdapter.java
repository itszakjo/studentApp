package zakjo.studentsapp.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zakjo.studentsapp.R;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.User;

public class ChooseUsersAdapter extends RecyclerView.Adapter<ChooseUsersAdapter.ServicesViewHolder>
{

    Context mCtx;
    List<User> userList;

    public ChooseUsersAdapter(Context mCtx, List<User> userList) {
        this.mCtx = mCtx;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(mCtx).inflate(R.layout.choose_user_item, null);

        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesViewHolder holder, final int position) {

        holder.user.setHint(new StringBuilder(userList.get(position).getPhone()).toString());
        holder.user.setText(new StringBuilder(userList.get(position).getUsername()).toString());
        holder.user.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){

                    Toast.makeText(mCtx, buttonView.getHint().toString(), Toast.LENGTH_SHORT).show();

                    Constants.addedUsers.add(buttonView.getText().toString());
                    Constants.addedUsersID.add(buttonView.getHint().toString());

                    //                    for( int i = 0 ; i< Constants.addedUsers.size(); i++){
//
//                        Toast.makeText(mCtx, Constants.addedUsers.get(i), Toast.LENGTH_SHORT).show();
//                    }

                }else{

                    Constants.addedUsers.remove(buttonView.getText().toString());
                    Constants.addedUsersID.remove(buttonView.getHint().toString());

                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class ServicesViewHolder extends RecyclerView.ViewHolder {

        CheckBox user ;

        private ServicesViewHolder(View itemView) {

            super(itemView);

            user = (CheckBox) itemView.findViewById(R.id.checUser);

        }





    }
}
