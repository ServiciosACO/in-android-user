package co.kubo.indiesco.restAPI;

/**
 * Created by Diego on 3/02/2018.
 */

public class ConstantesRestApi {
    //public static final String URL_BASE = "http://indiescoapi.inkubo.co/";
    //public static final String URL_BASE = "http://api.indiescoltda.com/"; //url cambiada el 19/06/2018
    public static final String URL_BASE = "http://api.serviciosaco.com/"; //url cambiada el 27/07/2018
    public static final String URL_CREAR_CUENTA = "cuenta/crear_cuenta/";
    public static final String URL_CREAR_FOTO = "cuenta/crear_usuario_foto/";
    public static final String URL_OBTENER_AUTH_TOKEN = "cuenta/jwt_api/";
    public static final String URL_LOGIN = "cuenta/login/";
    public static final String URL_RECUPERAR_CONTRASEÑA = "cuenta/recuperar_contrasena/";
    public static final String URL_VALIDAR_EMAIL = "cuenta/validar_email/";
    public static final String URL_ACTUALIZAR_CONTRASEÑA = "cuenta/actualizar_contrasena/";
    public static final String URL_ACTUALIZAR_DIRECCION = "cuenta/actualizar_direccion/";
    public static final String URL_AGREGAR_DIRECCION = "cuenta/agregar_direccion/";
    public static final String URL_ELIMINAR_DIRECCION = "cuenta/eliminar_direccion/";
    public static final String URL_ELIMINAR_NOTIFICACION = "cuenta/eliminar_notificacion/";
    public static final String URL_LISTAR_DIRECCIONES = "cuenta/listar_direcciones/";
    public static final String URL_LISTAR_NOTIFICACIONES = "cuenta/listar_notificaciones/";
    public static final String URL_CANCELAR_SERVICIO = "servicios/cancelar_servicio/";
    public static final String URL_RESUMEN_PEDIDO = "servicios/resumen_pedido/";
    public static final String URL_CREAR_SERVICIO = "servicios/crear_servicio";
    public static final String URL_LISTAR_HISTORIAL = "historial/lista_historial/";
    public static final String URL_LISTAR_TIPOS_INMUEBLES = "servicios/lista_tipos_inmuebles";
    public static final String URL_TASAR_SERVICIO = "servicios/tasar_servicio/id_tipo_inmueble/dimension";
    public static final String URL_REGISTRAR_SALTO_TX = "servicios/registrar_salto_tx/";
    public static final String URL_CALIFICAR_SERVICIO = "servicios/calificar_servicio/";
    public static final String URL_PENDIENTE_CALIFICAR = "servicios/pendientes_calificar/";
    public static final String URL_ACTUALIZAR_TOKEN_FIREBASE = "cuenta/actualizar_token/";
    public static final String URL_VALIDAR_CUPON = "servicios/validar_codigo_descuento/";
    public static final String URL_CREAR_RECARGO = "recargos/crear_recargo";
    public static final String URL_HISTORIAL_RECARGO = "recargos/historial_recargos/{uid}/{offset}";
    public static final String URL_CANCEL_TRANSACTION = "servicios/cancelar_transaccion/{id_solicitud}/{tipo}";
    public static final String URL_VALIDACION_DIREC = "servicios/getAvailableCities/";

}
