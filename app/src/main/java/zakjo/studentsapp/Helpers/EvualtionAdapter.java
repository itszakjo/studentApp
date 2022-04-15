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
import zakjo.studentsapp.model.Evualtion;

public class EvualtionAdapter extends RecyclerView.Adapter<EvualtionAdapter.BarbersViewHolder>
{

    Context mCtx;
    List<Evualtion> mevuList;


    public EvualtionAdapter(Context mCtx, List<Evualtion> mevuList) {
        this.mCtx = mCtx;
        this.mevuList = mevuList;
    }

    @NonNull
    @Override
    public BarbersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(mCtx).inflate(R.layout.evualtion_item, null);

        return new BarbersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BarbersViewHolder holder, final int position) {

        Evualtion bar = mevuList.get(position);

        holder.percentage.setText(bar.getPercentage());
        holder.mark.setText(bar.getMarks());
        holder.title.setText(bar.getTitle());

        holder.setItemClickListenter(new ItemClickListenter() {
            @Override
            public void onClick(View v) {


            }
        });

    }


    @Override
    public int getItemCount() {
        return mevuList.size();
    }

    class BarbersViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener  {

        TextView title , mark , percentage  ;


        ItemClickListenter itemClickListenter  ;

        private void setItemClickListenter(ItemClickListenter itemClickListenter) {
            this.itemClickListenter =itemClickListenter ;

        }

        private BarbersViewHolder(View itemView) {

            super(itemView);

            title = (TextView)itemView.findViewById(R.id.ev_title);
            mark = (TextView)itemView.findViewById(R.id.ev_mark);
            percentage = (TextView)itemView.findViewById(R.id.ev_percentage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            itemClickListenter.onClick(v);

        }



    }

}
