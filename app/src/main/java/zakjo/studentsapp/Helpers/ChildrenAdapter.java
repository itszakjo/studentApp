package zakjo.studentsapp.Helpers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import zakjo.studentsapp.Home;
import zakjo.studentsapp.Messaging;
import zakjo.studentsapp.R;
import zakjo.studentsapp.Rertofit.IFCMService;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.TeacherProfile;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.MChildren;
import zakjo.studentsapp.model.DataMessage;
import zakjo.studentsapp.model.MyResponse;
import zakjo.studentsapp.model.MChildren;
import zakjo.studentsapp.model.Token;

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.BarbersViewHolder>
{

    Context mCtx;
    List<MChildren> childrenList;


    public ChildrenAdapter(Context mCtx, List<MChildren> childrenList) {
        this.mCtx = mCtx;
        this.childrenList = childrenList;
    }

    @NonNull
    @Override
    public BarbersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(mCtx).inflate(R.layout.teacher_item, null);

        return new BarbersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BarbersViewHolder holder, final int position) {

        MChildren bar = childrenList.get(position);

        holder.teacherName.setText(bar.getSt_name());

        holder.setItemClickListenter(new ItemClickListenter() {
            @Override
            public void onClick(View v) {

                Constants.currentChild = childrenList.get(position);


                  // when u click on disease name on the home page just go to the Diseaseloc_act(that has the map )
                mCtx.startActivity(new Intent(mCtx , Home.class));

                Intent intent = new Intent(mCtx , Home.class);
                intent.putExtra("LOGINTYPE" ,"PARENT");
                Constants.LOGIN_TYPE = "PARENT";

                mCtx.startActivity(intent);


            }
        });

    }


    @Override
    public int getItemCount() {
        return childrenList.size();
    }

    class BarbersViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener  {

        TextView teacherName  ;


        ItemClickListenter itemClickListenter  ;

        private void setItemClickListenter(ItemClickListenter itemClickListenter) {
            this.itemClickListenter =itemClickListenter ;

        }

        private BarbersViewHolder(View itemView) {

            super(itemView);

            teacherName = (TextView)itemView.findViewById(R.id.teacherName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            itemClickListenter.onClick(v);

        }



    }

}
