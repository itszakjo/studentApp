package zakjo.studentsapp.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import zakjo.studentsapp.Home;
import zakjo.studentsapp.R;
import zakjo.studentsapp.Utils.Constants;
import zakjo.studentsapp.model.MRegisteredGroups;

public class RegisterdGroupsAdapter extends RecyclerView.Adapter<RegisterdGroupsAdapter.RegisteredGroupsViewHolder>
{

    Context mCtx;
    List<MRegisteredGroups> MRegisteredGroupsList;




    public RegisterdGroupsAdapter(Context mCtx, List<MRegisteredGroups> MRegisteredGroupsList) {
        this.mCtx = mCtx;
        this.MRegisteredGroupsList = MRegisteredGroupsList;
    }

    @NonNull
    @Override
    public RegisteredGroupsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view  = LayoutInflater.from(mCtx).inflate(R.layout.registered_group_item, null);

        return new RegisteredGroupsViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RegisteredGroupsViewHolder holder, final int position) {

        final MRegisteredGroups MRegisteredGroups = MRegisteredGroupsList.get(position);

        holder.date.setText(MRegisteredGroups.getReg_at());
        holder.groupName.setText(MRegisteredGroups.getGroup_name());
        holder.courseName.setText(MRegisteredGroups.getC_name());


        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCofirm(MRegisteredGroups.getSt_id());

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

        StringRequest request = new StringRequest(Request.Method.POST, Constants.CANCEL_REGGG,

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
        return MRegisteredGroupsList.size();
    }

    class RegisteredGroupsViewHolder extends RecyclerView.ViewHolder  {

        TextView groupName , date , courseName;
        ImageView cancel;





        private RegisteredGroupsViewHolder(View itemView) {

            super(itemView);

            courseName = (TextView)itemView.findViewById(R.id.course_name);
            groupName = (TextView)itemView.findViewById(R.id.group_name);
            date = (TextView)itemView.findViewById(R.id.registeration_date);
            cancel = (ImageView)itemView.findViewById(R.id.cancelThis);



        }




    }

}
