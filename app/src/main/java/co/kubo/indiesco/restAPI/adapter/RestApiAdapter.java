package co.kubo.indiesco.restAPI.adapter;

import com.google.gson.Gson;

import co.kubo.indiesco.restAPI.ConstantesRestApi;
import co.kubo.indiesco.restAPI.Endpoints;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Diego on 3/02/2018.
 */

public class RestApiAdapter {
    public Endpoints establecerConexionRestApi(Gson gson){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson)) //para deserializar los datos
                .build();
        return retrofit.create(Endpoints.class);
    }
    public Endpoints establecerConexionRestApiSinGson(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.URL_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(Endpoints.class);
    }
}
