package zakjo.studentsapp.Rertofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class FCMClient {

    public static Retrofit retrofit = null ;

    public static Retrofit getClient(String rootUrl){

        if (retrofit == null ){

            retrofit = new Retrofit.Builder()
                    .baseUrl(rootUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
            }

        return retrofit;
    }



}
