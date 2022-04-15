package zakjo.studentsapp.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;


import java.util.List;

import io.reactivex.annotations.NonNull;
import zakjo.studentsapp.R;
import zakjo.studentsapp.model.TeacherPics;

public class TeacherPicsAdapter extends RecyclerView.Adapter<TeacherPicsAdapter.SalonPicsViewHolder>
{

    Context mCtx;
    List<TeacherPics> picsList;

    public TeacherPicsAdapter(Context mCtx, List<TeacherPics> picsList) {
        this.mCtx = mCtx;
        this.picsList = picsList;
    }

    @NonNull
    @Override
    public SalonPicsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(mCtx).inflate(R.layout.slide_item, null);

        return new SalonPicsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalonPicsViewHolder holder, final int position) {

//        Picasso.with(mCtx)
//                .load(picsList.get(position).getPic())
//                .resize(1000 ,1000)
//                .centerCrop()
//                .into(holder.pic);

        Glide.with(mCtx).load(picsList.get(position).getPic()).into(holder.pic);



    }


    @Override
    public int getItemCount() {
        return picsList.size();
    }

    class SalonPicsViewHolder extends RecyclerView.ViewHolder {

        ImageView  pic;


        private SalonPicsViewHolder(View itemView) {

            super(itemView);

            pic = (ImageView) itemView.findViewById(R.id.slidePic);


        }

    }

}
