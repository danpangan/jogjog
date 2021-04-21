//package com.cck.jogjog.API;
//
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class RetrofitClient {
//
//    private static final String BASE_URL = "https://jogjog.com/api/appapi.php";
//    private static RetrofitClient myInstance;
//    private Retrofit retrofit;
//
//    private RetrofitClient() {
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//    }
//
//    public static synchronized RetrofitClient getInstance() {
//        if(myInstance == null){
//            myInstance = new RetrofitClient();
//        }
//        return myInstance;
//
//    }
//    public RetroAPI getApi() {
//        return retrofit.create(RetroAPI.class);
//    }
//}
