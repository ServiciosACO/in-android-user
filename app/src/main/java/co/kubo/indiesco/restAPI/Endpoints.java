package co.kubo.indiesco.restAPI;

import co.kubo.indiesco.modelo.Foto;
import co.kubo.indiesco.restAPI.modelo.ResponseAuthToken;
import co.kubo.indiesco.restAPI.modelo.ResponseFoto;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.restAPI.modelo.ResponseLogin;
import co.kubo.indiesco.restAPI.modelo.ResponseRegistro;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Diego on 3/02/2018.
 */

public interface Endpoints {

    @GET(ConstantesRestApi.URL_OBTENER_AUTH_TOKEN)
    Call<ResponseAuthToken> obtenerTokenAuth(@Header("X-AC-Access-Token") String accessToken);

    @GET
    Call<ResponseGeneral> validarEmail(@Header("X-AC-Auth-Token") String authToken, @Url String url);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_LOGIN)
    Call<ResponseLogin> login(@Header("X-AC-Auth-Token") String authToken, @Field("email") String email,
                              @Field("contrasena") String contrasena, @Field("token") String token, @Field("plataforma") String plataforma);

    @GET
    Call<ResponseGeneral> recuperarPassword(@Header("X-AC-Auth-Token") String authToken, @Url String url);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_CREAR_CUENTA)
    Call<ResponseRegistro> registro(@Header("X-AC-Auth-Token") String authToken, @Field("nombre") String nombre, @Field("email") String email,
                                    @Field("contrasena") String contrasena, @Field("token") String token, @Field("plataforma") String plataforma,
                                    @Field("telefono") String telefono);

    @Multipart
    @POST(ConstantesRestApi.URL_CREAR_FOTO)
    Call<ResponseFoto> guardarFoto(@Header("X-AC-Auth-Token") String authToken, @Part("uid") RequestBody uid, @Part MultipartBody.Part foto);


}//Endpoints
