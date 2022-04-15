package zakjo.studentsapp.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import zakjo.studentsapp.Home;
import zakjo.studentsapp.R;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.MRequestedGroups;

import static com.facebook.accountkit.internal.AccountKitController.getApplicationContext;


public class RequestedGroupsAdapter extends RecyclerView.Adapter<RequestedGroupsAdapter.RequestedGroupsViewHolder>
{

    Context mCtx;
    List<MRequestedGroups> MRequestedGroupsList;


    public RequestedGroupsAdapter(Context mCtx, List<MRequestedGroups> MRequestedGroupsList) {
        this.mCtx = mCtx;
        this.MRequestedGroupsList = MRequestedGroupsList;
    }

    @NonNull
    @Override
    public RequestedGroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(mCtx).inflate(R.layout.registered_group_item, null);
        return new RequestedGroupsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final RequestedGroupsViewHolder holder, final int position) {

        final MRequestedGroups registeredGroups = MRequestedGroupsList.get(position);

        holder.date.setText(registeredGroups.getReg_at());
        holder.groupName.setText(registeredGroups.getGroup_name());
        holder.courseName.setText(registeredGroups.getC_name());

        if(registeredGroups.getConfirmed().equals("1")){

            holder.regCard.setCardBackgroundColor(Color.parseColor("#778BC34A"));

        }else if(registeredGroups.getConfirmed().equals("0")) {

            holder.regCard.setCardBackgroundColor(Color.parseColor("#74F44336"));

        }

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCofirm(registeredGroups.getSt_id());

            }
        });
    }





    private void showCofirm(final String st_id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        builder.setTitle("Confirm");
        builder.setMessage("Cancel this ?");




        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

            }
        });
        final AlertDialog dialog = builder.create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                cancelBook(st_id);
                dialog.dismiss();



            }
        });
    }

    private void cancelBook(final String id ){

        StringRequest request = new StringRequest(Request.Method.POST, Constants.CANCEL_REQ,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            Toast.makeText(mCtx, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        }catch (JSONException e){

                            e.printStackTrace();
                        }

                        mCtx.startActivity(new Intent(mCtx , Home.class));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(mCtx,error.getMessage() , Toast.LENGTH_SHORT).show();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                //  store the data we put into the strings up there into the database fields

                params.put("st_id" , id);
                return params;
            }
        };
        RequestHandler.getInstance(mCtx).addToRequestQueue(request);



    }

    @Override
    public int getItemCount() {
        return MRequestedGroupsList.size();
    }

    class RequestedGroupsViewHolder extends RecyclerView.ViewHolder  {

        TextView groupName , date , courseName;

        CardView regCard ;

        ImageView cancel;

        ItemClickListenter itemClickListenter  ;

        private RequestedGroupsViewHolder(View itemView) {
            super(itemView);
            regCard = (CardView)itemView.findViewById(R.id.regcard);
            groupName = (TextView)itemView.findViewById(R.id.group_name);
            courseName = (TextView)itemView.findViewById(R.id.course_name);
            date = (TextView)itemView.findViewById(R.id.registeration_date);
            cancel = (ImageView)itemView.findViewById(R.id.cancelThis);
        }
    }

}
