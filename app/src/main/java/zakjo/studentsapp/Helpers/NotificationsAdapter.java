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
import zakjo.studentsapp.R;
import zakjo.studentsapp.Rertofit.IFCMService;
import zakjo.studentsapp.Rertofit.MyLabAPI;
import zakjo.studentsapp.TeacherProfile;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.DataMessage;
import zakjo.studentsapp.model.MyResponse;
import zakjo.studentsapp.model.Notifs;
import zakjo.studentsapp.model.Token;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.BarbersViewHolder>
{

    Context mCtx;
    List<Notifs> NotifisList;


    public NotificationsAdapter(Context mCtx, List<Notifs> NotifisList) {
        this.mCtx = mCtx;
        this.NotifisList = NotifisList;
    }

    @NonNull
    @Override
    public BarbersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(mCtx).inflate(R.layout.notification_item, null);

        return new BarbersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BarbersViewHolder holder, final int position) {

        Notifs bar = NotifisList.get(position);

        holder.Name.setText(bar.getAdv_text());

        holder.setItemClickListenter(new ItemClickListenter() {
            @Override
            public void onClick(View v) {


            }
        });

    }


    @Override
    public int getItemCount() {
        return NotifisList.size();
    }

    class BarbersViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener  {

        TextView Name  ;


        ItemClickListenter itemClickListenter  ;

        private void setItemClickListenter(ItemClickListenter itemClickListenter) {
            this.itemClickListenter =itemClickListenter ;

        }

        private BarbersViewHolder(View itemView) {

            super(itemView);

            Name = (TextView)itemView.findViewById(R.id.notii_nae);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            itemClickListenter.onClick(v);

        }



    }

}
