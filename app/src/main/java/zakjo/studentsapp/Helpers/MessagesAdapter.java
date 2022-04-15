package zakjo.studentsapp.Helpers;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.GnssClock;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.media.midi.MidiDevice;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import zakjo.studentsapp.Messaging;
import zakjo.studentsapp.R;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.ZakjoAudioPlayer;
import zakjo.studentsapp.ZakjoVideoPlayer;
import zakjo.studentsapp.model.Chat;


public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {



    public static final int MSG_TYPE_LFFT = 0 ;
    public static final int MSG_TYPE_RIGHT = 1 ;
    public static final int MSG_TYPE_MIDDLE = 2 ;
    private Context context ;
    private List<Chat> chatList;
    private String imgUrl ;

    private FirebaseUser firebaseUser ;

    public  Handler handler = new Handler();

    public MessagesAdapter(Context context, List<Chat> chatList) {

        this.context = context;
        this.chatList = chatList;
//      this.imgUrl = imgUrl ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == MSG_TYPE_RIGHT ){

                View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right , parent , false);
                return new MessagesAdapter.ViewHolder(view);
        }else{

                View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left , parent , false);
                return new MessagesAdapter.ViewHolder(view);

        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Chat chat = chatList.get(position);

        String msgType = "voice";



        if(chat.getType().equals("text")){

            holder.message.setVisibility(View.VISIBLE);
            holder.chatImage.setVisibility(View.GONE);
            holder.imageRelativeLayout.setVisibility(View.GONE);
            holder.downloadFile.setVisibility(View.GONE);
            holder.overlay.setVisibility(View.GONE);
            holder.playPauseRestart.setVisibility(View.GONE);
            holder.overlayAudio.setVisibility(View.GONE);
            holder.playAudio.setVisibility(View.GONE);
            holder.sender_pic.setVisibility(View.GONE);
            holder.videoCard.setVisibility(View.GONE);
            holder.videocam.setVisibility(View.GONE);
            holder.picCard.setVisibility(View.GONE);




            holder.message.setText(chat.getMessage());


        }

        else if(chat.getType().equals("photo")) {

            holder.message.setVisibility(View.GONE);
            holder.chatImage.setVisibility(View.VISIBLE);
            holder.imageRelativeLayout.setVisibility(View.VISIBLE);
            holder.downloadFile.setVisibility(View.GONE);
            holder.overlay.setVisibility(View.GONE);
            holder.playPauseRestart.setVisibility(View.GONE);
            holder.overlayAudio.setVisibility(View.GONE);
            holder.playAudio.setVisibility(View.GONE);
            holder.sender_pic.setVisibility(View.GONE);
            holder.videoCard.setVisibility(View.GONE);
            holder.videocam.setVisibility(View.GONE);
            holder.picCard.setVisibility(View.VISIBLE);






            Glide.with(context).load(chat.getMessage()).into(holder.chatImage);

        }

        else if(chat.getType().equals("pdf")) {

            holder.message.setVisibility(View.GONE);
            holder.chatImage.setVisibility(View.VISIBLE);
            holder.imageRelativeLayout.setVisibility(View.VISIBLE);
            holder.downloadFile.setVisibility(View.VISIBLE);
            holder.playPauseRestart.setVisibility(View.GONE);
            holder.overlay.setVisibility(View.GONE);
            holder.overlayAudio.setVisibility(View.GONE);
            holder.playAudio.setVisibility(View.GONE);
            holder.sender_pic.setVisibility(View.GONE);
            holder.videoCard.setVisibility(View.GONE);
            holder.videocam.setVisibility(View.GONE);
            holder.picCard.setVisibility(View.VISIBLE);





        }

        else if(chat.getType().equals("video")) {

            holder.message.setVisibility(View.GONE);
            holder.chatImage.setVisibility(View.GONE);
            holder.imageRelativeLayout.setVisibility(View.GONE);
            holder.downloadFile.setVisibility(View.GONE);
            holder.playPauseRestart.setVisibility(View.VISIBLE);
            holder.overlay.setVisibility(View.VISIBLE);
            holder.overlayAudio.setVisibility(View.GONE);
            holder.playAudio.setVisibility(View.GONE);
            holder.sender_pic.setVisibility(View.GONE);
            holder.videoCard.setVisibility(View.VISIBLE);
            holder.videocam.setVisibility(View.VISIBLE);
            holder.picCard.setVisibility(View.GONE);


            Bitmap bMap = ThumbnailUtils.createVideoThumbnail(chat.getMessage(), MediaStore.Video.Thumbnails.MICRO_KIND);

            Drawable dr = new BitmapDrawable(bMap);
            holder.overlay.setBackgroundDrawable(dr);




            holder.playPauseRestart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context , ZakjoVideoPlayer.class);
                    intent.putExtra("VIDEOLINK" ,chat.getMessage());
                    context.startActivity(intent);

                }
            });



        }

        else if(chat.getType().equals("audio")) {

            holder.message.setVisibility(View.GONE);
            holder.sender_pic.setVisibility(View.VISIBLE);
            holder.chatImage.setVisibility(View.GONE);
            holder.imageRelativeLayout.setVisibility(View.GONE);
            holder.downloadFile.setVisibility(View.GONE);
            holder.playPauseRestart.setVisibility(View.GONE);
            holder.videoCard.setVisibility(View.GONE);
            holder.videocam.setVisibility(View.GONE);
            holder.overlay.setVisibility(View.GONE);
            holder.overlayAudio.setVisibility(View.VISIBLE);
            holder.playAudio.setVisibility(View.VISIBLE);
            holder.picCard.setVisibility(View.GONE);


            holder.playAudio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context , ZakjoAudioPlayer.class);
                    intent.putExtra("AUDIOLINK" ,chat.getMessage());
                    context.startActivity(intent);

                }
            });



        }

        // collect the messages for receiver
        if(!chat.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
        {
            if(Constants.CHAT_TYPE.equals("GROUP")){

                holder.userNameOrPhone.setVisibility(View.VISIBLE);

                String cropped = chat.getSender().substring(0, chat.getSender().length() - 4);

                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                holder.userNameOrPhone.setTextColor(color);
                holder.userNameOrPhone.setText(cropped);
//
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.WRAP_CONTENT,
//                        RelativeLayout.LayoutParams.WRAP_CONTENT
//                );
//
//                params.setMargins( 305 , 8 , 0,  0);
//                holder.sent_time.setLayoutParams(params);



            }else if(Constants.CHAT_TYPE.equals("ONE")){

                holder.userNameOrPhone.setVisibility(View.GONE);
            }

            try{

                // if the time elapsed between this message and the previous one is less than 10 seconds then collect the both msgs
                long  seconds = Long.valueOf(chat.getTimestamp()) - Long.valueOf(chatList.get(position-1).getTimestamp());

                if(seconds <0){seconds = - seconds; }

                if(seconds < 10){ holder.sent_time.setVisibility(View.GONE);}


            }catch (Exception e){e.printStackTrace();}
        }

        //  show if delivered or seen
        if(chat.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()))
        {
            try {


                // if the time elapsed between this message and the previous one is less than 10 seconds then collect the both msgs
//                long  seconds = Long.valueOf(chat.getTimestamp()) - Long.valueOf(chatList.get(position-1).getTimestamp());
//
//                if(seconds <0){seconds = - seconds; }
//
//                if(seconds < 10){ holder.textSeen.setVisibility(View.GONE);}

                if(chat.getIseen() == 1){

                    // seen
                    holder.img_seen.setVisibility(View.VISIBLE);
                    holder.img_delivered.setVisibility(View.GONE);
                    holder.img_waiting.setVisibility(View.GONE);
                    holder.uploadingProgress.setVisibility(View.GONE);

                }else if (chat.getIseen() == 10){

                    // waiting to be uploaded
                    holder.img_seen.setVisibility(View.GONE);
                    holder.img_delivered.setVisibility(View.GONE);
                    holder.img_waiting.setVisibility(View.VISIBLE);
                    holder.uploadingProgress.setVisibility(View.VISIBLE);

                }else {

                    // delivered
                    holder.img_seen.setVisibility(View.GONE);
                    holder.img_delivered.setVisibility(View.VISIBLE);
                    holder.img_waiting.setVisibility(View.GONE);
                    holder.uploadingProgress.setVisibility(View.GONE);

                }

            }catch (Exception e){

                e.printStackTrace();
            }

        }




        try {

            String s  = chat.getTimestamp(); // time in milli seconds
            long timestamp = Long.parseLong(s);
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
            Date netDate = (new Date(timestamp));
            sdf.format(netDate);

            holder.sent_time.setText(sdf.format(netDate));

            GetTimeAgo getTimeAgo = new GetTimeAgo();

            // get difference between this message and previous message
            long  seconds = Long.parseLong(chat.getTimestamp())/1000L - Long.parseLong(chatList.get(position-1).getTimestamp())/1000L;

            if(seconds <0){seconds = - seconds; }

            // if the time elapsed between this message and the last message is 6 hours
            if(seconds>21000){

                holder.text_timeToTime.setVisibility(View.VISIBLE);

                String time =  getTimeAgo.getTimeAgo(timestamp);

                holder.text_timeToTime.setText(time);

            }else {

                holder.text_timeToTime.setVisibility(View.GONE);

            }
        }catch (Exception e){e.printStackTrace();}



        holder.downloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  downloadFile(chat);

            }
        });


    }


    private void downloadFile(Chat chat) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(chat.getMessage()));

        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download");
        request.setDescription("Downloading File....");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS ,  "Zewel"+chat.getType()+"_"+System.currentTimeMillis());

        DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    @Override
    public int getItemCount() { return chatList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public ImageView chatImage , sender_pic ;
        public ImageButton downloadFile , playPauseRestart , playAudio;
        public TextView message , textSeen  , sent_time , text_timeToTime , userNameOrPhone ;
        public ImageView img_seen , img_delivered , img_waiting , videocam;
        public RelativeLayout imageRelativeLayout, overlay , overlayAudio;
        public ProgressBar uploadingProgress ;
        public CardView videoCard , picCard;

        public ViewHolder(View itemView){
            super(itemView);

            picCard = itemView.findViewById(R.id.picCard);
            videoCard = itemView.findViewById(R.id.videoCard);
            videocam = itemView.findViewById(R.id.videocam);
            uploadingProgress = itemView.findViewById(R.id.uploadProgress);
            imageRelativeLayout = itemView.findViewById(R.id.imageRelativeLayout);
            overlay = itemView.findViewById(R.id.overlay);
            userNameOrPhone = itemView.findViewById(R.id.userNameOrPhone);
            text_timeToTime = itemView.findViewById(R.id.text_timeToTime);
            img_seen = itemView.findViewById(R.id.imge_seen);
            img_delivered = itemView.findViewById(R.id.imge_delivered);
            img_waiting = itemView.findViewById(R.id.imge_waiting);
            sent_time = itemView.findViewById(R.id.sent_time);
            chatImage = itemView.findViewById(R.id.chatImage);
            message = itemView.findViewById(R.id.textMessage);
            textSeen = itemView.findViewById(R.id.txt_seen);
            downloadFile = itemView.findViewById(R.id.downloadFile);
            playPauseRestart = itemView.findViewById(R.id.playPauseRestart);
            overlayAudio = itemView.findViewById(R.id.overlayAudio);
            playAudio = itemView.findViewById(R.id.playAudio);
            sender_pic = itemView.findViewById(R.id.sender_pic);



        }
    }

    @Override
    public int getItemViewType(int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //gonna be changed to if chatlist.get(position).getFrom.equals(myphone) do the following
        if(chatList.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())) {
            return MSG_TYPE_RIGHT;
        }
        else {
            return MSG_TYPE_LFFT;
        }
    }
}
