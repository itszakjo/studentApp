package zakjo.studentsapp.Rertofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static Retrofit retrofit = null ;

    public static Retrofit getClient(String rootUrl){



        if (retrofit== null ){

            retrofit = new Retrofit.Builder()
                    .baseUrl(rootUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();



        }

        return retrofit;
    }

    private static final String Base_URL = "http://192.168.56.1/mylabapp/getpatients.php";

    public static Retrofit getApiClient(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
