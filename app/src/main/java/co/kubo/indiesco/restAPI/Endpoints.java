package co.kubo.indiesco.restAPI;

import com.google.gson.JsonElement;

import co.kubo.indiesco.modelo.Foto;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.restAPI.modelo.ResponseAuthToken;
import co.kubo.indiesco.restAPI.modelo.ResponseCrearServicio;
import co.kubo.indiesco.restAPI.modelo.ResponseDireccion;
import co.kubo.indiesco.restAPI.modelo.ResponseFoto;
import co.kubo.indiesco.restAPI.modelo.ResponseGeneral;
import co.kubo.indiesco.restAPI.modelo.ResponseHistorial;
import co.kubo.indiesco.restAPI.modelo.ResponseInmueble;
import co.kubo.indiesco.restAPI.modelo.ResponseLogin;
import co.kubo.indiesco.restAPI.modelo.ResponseNotificacion;
import co.kubo.indiesco.restAPI.modelo.ResponsePedido;
import co.kubo.indiesco.restAPI.modelo.ResponsePendienteCalificar;
import co.kubo.indiesco.restAPI.modelo.ResponseRegistro;
import co.kubo.indiesco.restAPI.modelo.ResponseTasarServicio;
import co.kubo.indiesco.utils.Constantes;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Diego on 3/02/2018.
 */

public interface Endpoints {

    @GET("https://maps.googleapis.com/maps/api/place/details/json")
    Call<JsonElement> ubicacionDireccion(@Query("placeid") String placeId,
                                         @Query("sensor") String sensor,
                                         @Query("key") String key);

    @GET("https://maps.googleapis.com/maps/api/geocode/json")
    Call<JsonElement> obtenerDireccion(@Query("key") String key,
                                       @Query("latlng") String latlng,
                                       @Query("sensor") String sensor);

    @GET("https://maps.googleapis.com/maps/api/place/autocomplete/json")
    Call<JsonElement> direccionPalabra(@Query("input") String direccion,
                                       @Query("sensor") String sensor,
                                       @Query("key") String key,
                                       @Query("location") String latlon,
                                       @Query("radius") String radius,
                                       @Query("components") String components,
                                       @Query("language") String language);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_ACTUALIZAR_CONTRASEÑA)
    Call<ResponseGeneral> actualizarContraseña(@Header("X-AC-Auth-Token") String authToken, @Field("uid") String uid,
                                               @Field("pwd_nueva") String pwd_nueva, @Field("pwd_anterior") String pwd_anterior);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_ACTUALIZAR_DIRECCION)
    Call<ResponseGeneral> actualizarDireccion(@Header("X-AC-Auth-Token") String authToken, @Field("id_direccion") String id_direccion,
                                              @Field("direccion") String direccion, @Field("latitud") String latitud,
                                              @Field("longitud") String longitud, @Field("complemento") String complemento);
    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_AGREGAR_DIRECCION)
    Call<ResponseGeneral> agregarDireccion(@Header("X-AC-Auth-Token") String authToken, @Field("uid") String uid,
                                           @Field("direccion") String direccion, @Field("latitud") String latitud,
                                           @Field("longitud") String longitud, @Field("complemento") String complemento, @Field("ciudad") String ciudad);

    @GET(ConstantesRestApi.URL_ELIMINAR_DIRECCION + "{uid}/{id_direccion}")
    Call<ResponseGeneral> eliminarDireccion(@Header("X-AC-Auth-Token") String authToken, @Path("uid") String uid, @Path("id_direccion") String id_direccion);

    @GET(ConstantesRestApi.URL_ELIMINAR_NOTIFICACION + "{uid}/{id_notificacion}")
    Call<ResponseGeneral> eliminarNotificacion(@Header("X-AC-Auth-Token") String authToken, @Path("uid") String uid,
                                               @Path("id_notificacion") String id_notificacion);

    @GET(ConstantesRestApi.URL_LISTAR_DIRECCIONES + "{uid}")
    Call<ResponseDireccion> listarDireccion(@Header("X-AC-Auth-Token") String authToken, @Path("uid") String uid);

    @GET(ConstantesRestApi.URL_LISTAR_NOTIFICACIONES + "{uid}/{inicia}")
    Call<ResponseNotificacion> listarNotificaciones(@Header("X-AC-Auth-Token") String authToken, @Path("uid") String uid, @Path("inicia") String inicia);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_CREAR_CUENTA)
    Call<ResponseRegistro> registro(@Header("X-AC-Auth-Token") String authToken, @Field("nombre") String nombre,
                                    @Field("email") String email, @Field("contrasena") String contrasena,
                                    @Field("token") String token, @Field("plataforma") String plataforma, @Field("telefono") String telefono);

    @Multipart
    @POST(ConstantesRestApi.URL_CREAR_FOTO)
    Call<ResponseFoto> guardarFoto(@Header("X-AC-Auth-Token") String authToken, @Part("uid") RequestBody uid, @Part MultipartBody.Part foto);

    @GET(ConstantesRestApi.URL_OBTENER_AUTH_TOKEN)
    Call<ResponseAuthToken> obtenerTokenAuth(@Header("X-AC-Access-Token") String accessToken);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_LOGIN)
    Call<ResponseLogin> login(@Header("X-AC-Auth-Token") String authToken, @Field("email") String email,
                              @Field("contrasena") String contrasena, @Field("token") String token, @Field("plataforma") String plataforma);

    @GET
    Call<ResponseGeneral> recuperarPassword(@Header("X-AC-Auth-Token") String authToken, @Url String url);

    @GET
    Call<ResponseGeneral> validarEmail(@Header("X-AC-Auth-Token") String authToken, @Url String url);

    @GET(ConstantesRestApi.URL_CANCELAR_SERVICIO + "{id_solicitud}/{uid}")
    Call<ResponseGeneral> cancelarServicio(@Header("X-AC-Auth-Token") String authToken, @Path("id_solicitud") String id_solicitud, @Path("uid") String uid);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_CREAR_SERVICIO)
    Call<ResponseCrearServicio> crearServicio(@Header("X-AC-Auth-Token") String authToken, @Field("uid") String uid,
                                     @Field("id_tipo_inmueble") String id_tipo_inmueble, @Field("dimension") String dimension,
                                     @Field("valor") String valor, @Field("id_direccion") String id_direccion,
                                     @Field("fecha_servicio") String fecha_servicio, @Field("urgente") String urgente,
                                     @Field("hora") String hora, @Field("comentario") String comentario);

    @GET(ConstantesRestApi.URL_LISTAR_HISTORIAL + "{uid}/{tipo}")
    Call<ResponseHistorial> listarHistorial(@Header("X-AC-Auth-Token") String authToken, @Path("uid") String uid, @Path("tipo") String tipo);

    @GET(ConstantesRestApi.URL_LISTAR_TIPOS_INMUEBLES)
    Call<ResponseInmueble> listarInmuebles(@Header("X-AC-Auth-Token") String authToken);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_REGISTRAR_SALTO_TX)
    Call<ResponseGeneral> registrarSaltoTX(@Header("X-AC-Auth-Token") String authToken, @Field("id_solicitud") String id_solicitud,
                                        @Field("nombre") String nombre, @Field("telefono") String telefono,
                                        @Field("email") String email, @Field("documento") String documento,
                                        @Field("valor") String valor);

    //Esta url se envia a un webView segun el id_solicitud
    @GET(ConstantesRestApi.URL_RESUMEN_PEDIDO + "{uid}/{id_solicitud}")
    Call<ResponsePedido> resumenPedido(@Header("X-AC-Auth-Token") String authToken, @Path("uid") String uid,
                                       @Path("id_solicitud") String id_solicitud);

    @GET(ConstantesRestApi.URL_TASAR_SERVICIO + "{id_tipo_inmueble}/{dimension}/{urgente}")
    Call<ResponseTasarServicio> tasarServicio(@Header("X-AC-Auth-Token") String authToken,
                                              @Path("id_tipo_inmueble") String id_tipo_inmueble,
                                              @Path("dimension") String dimension, @Path("urgente") String urgente);

    @GET(ConstantesRestApi.URL_PENDIENTE_CALIFICAR + "{uid}")
    Call<ResponsePendienteCalificar> pendienteCalificar(@Header("X-AC-Auth-Token") String authToken, @Path("uid") String uid);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_CALIFICAR_SERVICIO)
    Call<ResponseGeneral> calificarServicio(@Header("X-AC-Auth-Token") String authToken, @Field("id_solicitud") String id_solicitud,
                                            @Field("calificacion") String calificacion, @Field("comentario") String comentario);

}//Endpoints
