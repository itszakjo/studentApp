package zakjo.studentsapp.Rertofit;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import zakjo.studentsapp.model.DataMessage;
import zakjo.studentsapp.model.MyResponse;

public interface IFCMService {


    @Headers(
            {





                    "Content-Type:application/json",
                    "Authorization:key=AAAAMmPUPLA:APA91bHjNu1voRrjgX-oML_nqFM4s9q8DvJdl7l9p5d_sIgKz3xyzZy6Y0Jj5t6BaQhYajrSsIbIiJaITOvvFvtcmdOa0vfiy9CUVPMvQadh63HwvIfpyKYYD4EaXFsoZSIgCHrzUV_n"
    }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);
}
