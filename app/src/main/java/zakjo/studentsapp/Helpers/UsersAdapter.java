package zakjo.studentsapp.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import zakjo.studentsapp.Messaging;
import zakjo.studentsapp.R;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.Chat;
import zakjo.studentsapp.model.User;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {


    private Context context ;
    private List<User> userList;
    boolean isChat ;


    String theLastMessage ;


    int UNREAD_COUNT  ;


    public UsersAdapter(Context context, List<User> userList , boolean isChat) {
        this.context = context;
        this.userList = userList;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view   = LayoutInflater.from(context).inflate(R.layout.user_item , parent , false);

        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final User user = userList.get(position);

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

        if(user.getUsername().equals("")){
            try {

                Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(user.getPhone(), "EG");
                String  convertedNumber  = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

                holder.useerN.setText(convertedNumber);

            } catch (NumberParseException e) {
                e.printStackTrace();
            }
        }
        else { holder.useerN.setText(user.getUsername()); }



//      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        if(isChat){

            holder.message_time.setVisibility(View.VISIBLE);
//          LastMessage(user.getPhone() , holder.lastMessage); // usesless function


            try {

                String s  = user.getTimestamp(); // time in millie seconds
                long timestamp = Long.parseLong(s);
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
                Date netDate = (new Date(timestamp));
                sdf.format(netDate);

                holder.message_time.setText(sdf.format(netDate));

                if(user.getTyping_to().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                        && user.getStatus().equals("typing")){

                    holder.lastMessage.setTextColor(Color.parseColor("#634975"));
                    holder.lastMessage.setText("typing..");

                }else {
                    holder.lastMessage.setText(user.getLastMessage());
                }


                if(user.getUser_type().equals("onechat")){

                    if(user.getStatus().equals("online") ||  user.getStatus().equals("typing") || user.getStatus().equals("recording")  || user.getStatus().equals("calling")){
                        holder.imgOn.setVisibility(View.VISIBLE);
                        holder.imgOFf.setVisibility(View.GONE);
                    }else {
                        holder.imgOn.setVisibility(View.GONE);
                        holder.imgOFf.setVisibility(View.VISIBLE);
                    }

                }else {
                    holder.imgOn.setVisibility(View.GONE);
                    holder.imgOFf.setVisibility(View.GONE);
                }


                if(user.getImageURL().equals("default") ){

                    holder.userImg.setBackgroundResource(R.color.light_purple);
                    holder.userImg.setText(":)");

                }else{

                    Glide.with(context)
                            .load(user.getImageURL())
                            .apply(RequestOptions.circleCropTransform())
                            .apply(RequestOptions.placeholderOf(R.mipmap.ic_launcher))
                            .into(new SimpleTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource,
                                                            @Nullable Transition<? super Drawable> transition) {
                                    holder.userImg.setBackground(resource);
                                }
                            });
                }



                DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("Chats")
                        .child(user.getPhone()) // user phone
                        .child(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) // my phone
                        .child("messages");

//            Query chatQuery = databaseReference.orderByChild("timestamp");
                Query chatQuery = databaseReference.orderByKey().startAt("received").endAt("received"+"\uf8ff");
                chatQuery.addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UNREAD_COUNT = 0;

                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                            Chat chat = snapshot.getValue(Chat.class);
                            try {

                                //                            if(chat.getReciever().equals(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()) &&
//                                    chat.getSender().equals(user.getPhone())){
//
//                                if(!chat.isIseen()){
//                                    UNREAD_COUNT ++;
//                                    System.out.println("NOTSEEN  " + chat.getMessage() + " : "+ UNREAD_COUNT);
//                                }
//                            }


//                            if(chat.getSender().equals("+201067151101") && !chat.isIseen()) {
//
//                                UNREAD_COUNT ++;
//                                System.out.println("NOTSEEN  " + chat.getMessage() + " : "+ UNREAD_COUNT);
//                            }

                                if(chat.getIseen() == 0) {

                                    UNREAD_COUNT ++;
                                    System.out.println("NOTSEEN " + chat.getMessage() + " : " + UNREAD_COUNT);
                                }

                            }catch (Exception e){e.printStackTrace();}
                        }

                        if(UNREAD_COUNT > 0 ){

                            holder.unread_count.setText(String.valueOf(UNREAD_COUNT));
                            holder.unread_count.setVisibility(View.VISIBLE);
                            holder.message_time.setTextColor(Color.parseColor("#634975"));

                        }else{

                            holder.message_time.setTextColor(Color.parseColor("#46000000"));
                            holder.unread_count.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

            }catch (Exception e){e.printStackTrace();}
            // this algorithm needs improvements as well

            // Chats.child(userPhone); so we getting all the messages been sent by this user to us instead of getting the whole chat!  ,
            // then checking whether the msg iseen or not
            // then increasing the count if not seen


            //DatabaseReference  databaseReference = FirebaseDatabase.getInstance().getReference("Chats");


//            startAt("received").endAt("received"+"\uf8ff")
            // the ones i received from him for him


        }
        else {


            holder.message_time.setVisibility(View.GONE);

            try {

                Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(user.getPhone(), "EG");
                String  convertedNumber  = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);

                holder.lastMessage.setText(convertedNumber);


                holder.userImg.setBackgroundResource(R.color.light_purple);

                holder.userImg.setText(String.valueOf(user.getUsername().charAt(0)).toUpperCase());
                holder.useerN.setText(user.getUsername());

                holder.imgOn.setVisibility(View.GONE);
                holder.imgOFf.setVisibility(View.GONE);

            }catch (NumberParseException e) {e.printStackTrace();}


        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Constants.currentUser = userList.get(position);
                Constants.userPhone = userList.get(position).getPhone(); // userphone is also considered as groupchat_id when the chat type is group

                if(user.getUser_type().equals("onechat")){

                    Intent intent = new Intent(context , Messaging.class);
                    intent.putExtra("CHATYPE" ,"ONE");
                    intent.putExtra("USERPHONE" ,"0");
                    context.startActivity(intent);

                }else if(user.getUser_type().equals("groupchat")){

                    Intent intent = new Intent(context , Messaging.class);
                    intent.putExtra("CHATYPE" ,"GROUP");
                    intent.putExtra("USERPHONE" ,"0");
                    context.startActivity(intent);

                }

            }
        });

    }

    @Override
    public int getItemCount() { return userList.size();  }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView useerN , userImg ,lastMessage , unread_count , message_time ;
        public ImageView  imgOn , imgOFf ;
        public View view ;

        public ViewHolder(View itemView){

            super(itemView);

            imgOFf = itemView.findViewById(R.id.img_offline);
            imgOn = itemView.findViewById(R.id.img_online);

            unread_count = itemView.findViewById(R.id.unseen_count);
            message_time = itemView.findViewById(R.id.msg_time);

            userImg = itemView.findViewById(R.id.userPic);
            useerN = itemView.findViewById(R.id.username);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }

    // check for last message , needs improvements > edited > useless function !!
    private void LastMessage(final String user_phone , final TextView lastMsg){

        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        // changes to  , filter the whole chat to only our chat , before retrieving data
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
              .getReference("Chats")
              .child(firebaseUser.getPhoneNumber())
              .child(user_phone)
              .child("messages");

        Query lastMessageQuery = databaseReference.orderByChild("timestamp").limitToLast(1);

        lastMessageQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Chat chat  = snapshot.getValue(Chat.class);
                    assert chat != null;
                    assert firebaseUser != null;

                    try {

                        theLastMessage = chat.getMessage();

                    }catch (Exception e){ e.printStackTrace();}
                }

                switch(theLastMessage){

                    case "default":

                        lastMsg.setText("Say hi :)");
                        break;

                        default:
                            lastMsg.setText(theLastMessage);
                            break;
                }
                theLastMessage = "default";
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }
}
