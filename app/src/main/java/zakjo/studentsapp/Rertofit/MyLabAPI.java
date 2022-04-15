package zakjo.studentsapp.Rertofit;

import java.util.List;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import zakjo.studentsapp.model.Average;
import zakjo.studentsapp.model.Govs;
import zakjo.studentsapp.model.Groups;
import zakjo.studentsapp.model.Levels;
import zakjo.studentsapp.model.MRegisteredGroups;
import zakjo.studentsapp.model.MRequestedGroups;
import zakjo.studentsapp.model.MServices;
import zakjo.studentsapp.model.Markers;
import zakjo.studentsapp.model.Reviews;
import zakjo.studentsapp.model.TeacherPics;
import zakjo.studentsapp.model.Subjects;
import zakjo.studentsapp.model.Teachers;
import zakjo.studentsapp.model.Times;
import zakjo.studentsapp.model.Token;

public interface MyLabAPI {


    @FormUrlEncoded
    @POST("getMarkers.php")
    io.reactivex.Observable<List<Markers>> getMarkers(@Field("heldisname") String heldisname);

    @FormUrlEncoded
    @POST("getTimesAndDays.php")
    io.reactivex.Observable <List<Times>> getTimes(@Field("refsalon") int refSalon, @Field("refday") int refday);


    @FormUrlEncoded
    @POST("getRegisteredGroups.php")
    io.reactivex.Observable <List<MRegisteredGroups>> getRegisteredGroups(@Field("st_id") String st_id);

    @FormUrlEncoded
    @POST("getRequests.php")
    io.reactivex.Observable <List<MRequestedGroups>> getRequestedGroups(@Field("st_id") String st_id);

    @FormUrlEncoded
    @POST("getTeacherPics.php")
    io.reactivex.Observable <List<TeacherPics>> getTeacherPics(@Field("refteacher") int refteacher);

    @FormUrlEncoded
    @POST("getCities.php")
    io.reactivex.Observable <List<Govs>> getCities(@Field("refgov") int refgov);

    @FormUrlEncoded
    @POST("getServices.php")
    io.reactivex.Observable <List<MServices>> getServices(@Field("refsalon") int type);



    @FormUrlEncoded
    @POST("updateToken.php")
    Call<String> updateToken(@Field("phone") String phone,
                             @Field("token") String token,
                             @Field("isServerToken") String isServerToken);

    @FormUrlEncoded
    @POST("getToken.php")
//    Call<Token> getToken(@Field("phone") String phone,
//                         @Field("isServerToken") String isServerToken);
    io.reactivex.Observable<List<Token>> getToken(@Field("phone") String phone , @Field("isServerToken") String isServerToken );

//    @FormUrlEncoded
//    @POST("getSalonPics.php")
//    io.reactivex.Observable <List<TeacherPics>> getSalonPics(@Field("refsalon") int refsalon);

    @FormUrlEncoded
    @POST("getTeacherPics.php")
    io.reactivex.Observable <List<TeacherPics>> getTeacherPicsN(@Field("refteacher") int refteacher);

    @FormUrlEncoded
    @POST("getAvgRate.php")
    io.reactivex.Observable <List<Average>> getAvgRate(@Field("refsalon") int refsalon);

    @FormUrlEncoded
    @POST("getReviews.php")
    io.reactivex.Observable <List<Reviews>> getReviews(@Field("refsalon") int refsalon);


}
