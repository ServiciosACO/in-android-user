package co.kubo.indiesco.restAPI;

import com.google.gson.JsonElement;

import co.kubo.indiesco.modelo.Foto;
import co.kubo.indiesco.modelo.Historial;
import co.kubo.indiesco.modelo.ValidacionDirecciones;
import co.kubo.indiesco.restAPI.modelo.ResponseAuthToken;
import co.kubo.indiesco.restAPI.modelo.ResponseCodigoDescuento;
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
import co.kubo.indiesco.restAPI.modelo.ResponseRecargo;
import co.kubo.indiesco.restAPI.modelo.ResponseRecargo2;
import co.kubo.indiesco.restAPI.modelo.ResponseRegistro;
import co.kubo.indiesco.restAPI.modelo.ResponseTasarServicio;
import co.kubo.indiesco.restAPI.modelo.ResponseValidacion;
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

    //<editor-fold desc="Google Maps API">
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
    //</editor-fold>

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_ACTUALIZAR_CONTRASEÑA)
    Call<ResponseGeneral> actualizarContraseña(@Header("X-AC-Auth-Token") String authToken,
                                               @Field("uid") String uid,
                                               @Field("pwd_nueva") String pwd_nueva,
                                               @Field("pwd_anterior") String pwd_anterior);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_ACTUALIZAR_DIRECCION)
    Call<ResponseGeneral> actualizarDireccion(@Header("X-AC-Auth-Token") String authToken,
                                              @Field("id_direccion") String id_direccion,
                                              @Field("direccion") String direccion,
                                              @Field("latitud") String latitud,
                                              @Field("longitud") String longitud,
                                              @Field("complemento") String complemento);
    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_AGREGAR_DIRECCION)
    Call<ResponseGeneral> agregarDireccion(@Header("X-AC-Auth-Token") String authToken,
                                           @Field("uid") String uid,
                                           @Field("direccion") String direccion,
                                           @Field("latitud") String latitud,
                                           @Field("longitud") String longitud,
                                           @Field("complemento") String complemento,
                                           @Field("ciudad") String ciudad,
                                           @Field("cityId") String cityId);

    @GET(ConstantesRestApi.URL_ELIMINAR_DIRECCION + "{uid}/{id_direccion}")
    Call<ResponseGeneral> eliminarDireccion(@Header("X-AC-Auth-Token") String authToken,
                                            @Path("uid") String uid,
                                            @Path("id_direccion") String id_direccion);

    @GET(ConstantesRestApi.URL_ELIMINAR_NOTIFICACION + "{uid}/{id_notificacion}")
    Call<ResponseGeneral> eliminarNotificacion(@Header("X-AC-Auth-Token") String authToken,
                                               @Path("uid") String uid,
                                               @Path("id_notificacion") String id_notificacion);

    @GET(ConstantesRestApi.URL_LISTAR_DIRECCIONES + "{uid}")
    Call<ResponseDireccion> listarDireccion(@Header("X-AC-Auth-Token") String authToken,
                                            @Path("uid") String uid);

    @GET(ConstantesRestApi.URL_LISTAR_NOTIFICACIONES + "{uid}/{inicia}")
    Call<ResponseNotificacion> listarNotificaciones(@Header("X-AC-Auth-Token") String authToken,
                                                    @Path("uid") String uid,
                                                    @Path("inicia") String inicia);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_CREAR_CUENTA)
    Call<ResponseRegistro> registro(@Header("X-AC-Auth-Token") String authToken,
                                    @Field("nombre") String nombre,
                                    @Field("email") String email,
                                    @Field("contrasena") String contrasena,
                                    @Field("token") String token,
                                    @Field("plataforma") String plataforma,
                                    @Field("telefono") String telefono);

    @Multipart
    @POST(ConstantesRestApi.URL_CREAR_FOTO)
    Call<ResponseFoto> guardarFoto(@Header("X-AC-Auth-Token") String authToken,
                                   @Part("uid") RequestBody uid,
                                   @Part MultipartBody.Part foto);

    @GET(ConstantesRestApi.URL_OBTENER_AUTH_TOKEN)
    Call<ResponseAuthToken> obtenerTokenAuth(@Header("X-AC-Access-Token") String accessToken);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_LOGIN)
    Call<ResponseLogin> login(@Header("X-AC-Auth-Token") String authToken,
                              @Field("email") String email,
                              @Field("contrasena") String contrasena,
                              @Field("token") String token,
                              @Field("plataforma") String plataforma);

    @GET
    Call<ResponseGeneral> recuperarPassword(@Header("X-AC-Auth-Token") String authToken,
                                            @Url String url);

    @GET
    Call<ResponseGeneral> validarEmail(@Header("X-AC-Auth-Token") String authToken,
                                       @Url String url);

    @GET(ConstantesRestApi.URL_CANCELAR_SERVICIO + "{id_solicitud_item}/{uid}")
    Call<ResponseGeneral> cancelarServicio(@Header("X-AC-Auth-Token") String authToken,
                                           @Path("id_solicitud_item") int id_solicitud_item,
                                           @Path("uid") String uid);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_CREAR_SERVICIO)
    Call<ResponseCrearServicio> crearServicio(@Header("X-AC-Auth-Token") String authToken,
                                              @Field("uid") String uid,
                                              @Field("valor_compra") int valor_compra,
                                              @Field("id_codigo_descuento") int id_codigo_descuento,
                                              @Field("descuento") int descuento,
                                              @Field("cantidad_fechas") int cantidad_fechas,
                                              @Field("servicios") String servicios);

    @GET(ConstantesRestApi.URL_LISTAR_HISTORIAL + "{uid}/{tipo}")
    Call<ResponseHistorial> listarHistorial(@Header("X-AC-Auth-Token") String authToken,
                                            @Path("uid") String uid,
                                            @Path("tipo") String tipo);

    @GET(ConstantesRestApi.URL_LISTAR_TIPOS_INMUEBLES)
    Call<ResponseInmueble> listarInmuebles(@Header("X-AC-Auth-Token") String authToken);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_REGISTRAR_SALTO_TX)
    Call<ResponseGeneral> registrarSaltoTX(@Header("X-AC-Auth-Token") String authToken,
                                           @Field("id_solicitud") String id_solicitud,
                                           @Field("nombre") String nombre,
                                           @Field("telefono") String telefono,
                                           @Field("email") String email,
                                           @Field("documento") String documento,
                                           @Field("valor") String valor);

    //Esta url se envia a un webView segun el id_solicitud
    @GET(ConstantesRestApi.URL_RESUMEN_PEDIDO + "{uid}/{id_solicitud}")
    Call<ResponsePedido> resumenPedido(@Header("X-AC-Auth-Token") String authToken,
                                       @Path("uid") String uid,
                                       @Path("id_solicitud") String id_solicitud);

    @GET(ConstantesRestApi.URL_TASAR_SERVICIO + "{id_tipo_inmueble}/{dimension}/{urgente}")
    Call<ResponseTasarServicio> tasarServicio(@Header("X-AC-Auth-Token") String authToken,
                                              @Path("id_tipo_inmueble") String id_tipo_inmueble,
                                              @Path("dimension") String dimension,
                                              @Path("urgente") String urgente);

    @GET(ConstantesRestApi.URL_PENDIENTE_CALIFICAR + "{uid}/{idservicio}")
    Call<ResponsePendienteCalificar> pendienteCalificar(@Header("X-AC-Auth-Token") String authToken,
                                                        @Path("uid") String uid,@Path("idservicio") String idservicio);

   /* @FormUrlEncoded
    @POST(ConstantesRestApi.URL_CALIFICAR_SERVICIO)
    Call<ResponseGeneral> calificarServicio(@Header("X-AC-Auth-Token") String authToken,
                                            @Field("id_solicitud_item") String id_solicitud_item,
                                            @Field("calificacion") String calificacion,
                                            @Field("comentario") String comentario);*/
    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_CALIFICAR_SERVICIO1)
    Call<ResponseGeneral> calificarServicio(@Header("X-AC-Auth-Token") String authToken,
                                            @Field("id_solicitud_item") String id_solicitud_item,
                                            @Field("personal") String calificacion,
                                            @Field("comentario") String comentario);

    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_VALIDAR_CUPON)
    Call<ResponseCodigoDescuento> validarCupon(@Header("X-AC-Auth-Token") String authToken,
                                               @Field("uid") String uid,
                                               @Field("codigo_descuento") String codigo_descuento);

    //@FormUrlEncoded
    //@POST(ConstantesRestApi.URL_ACTUALIZAR_TOKEN_FIREBASE)
    @GET(ConstantesRestApi.URL_ACTUALIZAR_TOKEN_FIREBASE+ "{uid}/{token}/{plataforma}")
    Call<ResponseGeneral> actualizarTokenFirebase(@Header("X-AC-Auth-Token") String authToken,
                                                  @Path("uid") String uid,
                                                  @Path("token") String token,
                                                  @Path("plataforma") String plataforma);

    @GET(ConstantesRestApi.URL_HISTORIAL_RECARGO)
    Call<ResponseRecargo> getRecargos(@Header("X-AC-Auth-Token") String authToken,
                                      @Path("uid") String uid,
                                      @Path("offset") int offset);

    @GET(ConstantesRestApi.URL_CANCEL_TRANSACTION)
    Call<ResponseGeneral> cancelTransaction(@Header("X-AC-Auth-Token") String authToken,
                                      @Path("id_solicitud") int id_solicitud,
                                      @Path("tipo") String tipo);


    @FormUrlEncoded
    @POST(ConstantesRestApi.URL_CREAR_RECARGO)
    Call<ResponseRecargo2> createRecharge(@Header("X-AC-Auth-Token") String authToken,
                                          @Field("uid") String uid,
                                          @Field("mts") int mts,
                                          @Field("valor") int valor);

    @GET(ConstantesRestApi.URL_VALIDACION_DIREC)
    Call<ResponseValidacion> validarDirec(@Header("X-AC-Auth-Token") String authToken);


    @FormUrlEncoded
    @POST("cuenta/createAddress")
    Call<ResponseValidacion> createAddress(@Header("X-AC-Auth-Token") String authToken,
                                           @Field("uid") String uid,
                                           @Field("address") String address,
                                           @Field("complement") String complement,
                                           @Field("cityId") String cityId);

    @FormUrlEncoded
    @POST("cuenta/updateAddress")
    Call<ResponseValidacion> updateAddress(@Header("X-AC-Auth-Token") String authToken,
                                           @Field("addressId") String uid,
                                           @Field("address") String address,
                                           @Field("complement") String complement,
                                           @Field("cityId") String cityId);




}//Endpoints
