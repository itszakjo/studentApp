package zakjo.studentsapp.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import io.reactivex.annotations.NonNull;
import zakjo.studentsapp.R;
import zakjo.studentsapp.model.Attendence;
import zakjo.studentsapp.model.Notifs;

public class AttendenceAdapter extends RecyclerView.Adapter<AttendenceAdapter.BarbersViewHolder>
{

    Context mCtx;
    List<Attendence> mAttendenceList;


    public AttendenceAdapter(Context mCtx, List<Attendence> mAttendenceList) {
        this.mCtx = mCtx;
        this.mAttendenceList = mAttendenceList;
    }

    @NonNull
    @Override
    public BarbersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(mCtx).inflate(R.layout.attendence_item, null);

        return new BarbersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BarbersViewHolder holder, final int position) {

        Attendence bar = mAttendenceList.get(position);

        holder.Status.setText(bar.getStatus());
        holder.Date.setText(bar.getCreated_at());


        holder.setItemClickListenter(new ItemClickListenter() {
            @Override
            public void onClick(View v) {


            }
        });

    }


    @Override
    public int getItemCount() {
        return mAttendenceList.size();
    }

    class BarbersViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener  {

        TextView Status  , Date ;


        ItemClickListenter itemClickListenter  ;

        private void setItemClickListenter(ItemClickListenter itemClickListenter) {
            this.itemClickListenter =itemClickListenter ;

        }

        private BarbersViewHolder(View itemView) {

            super(itemView);

            Status = (TextView)itemView.findViewById(R.id.attence_status);
            Date = (TextView)itemView.findViewById(R.id.attence_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            itemClickListenter.onClick(v);

        }



    }

}
