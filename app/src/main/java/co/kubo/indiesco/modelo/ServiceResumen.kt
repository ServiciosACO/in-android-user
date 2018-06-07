package co.kubo.indiesco.modelo

/**
 * Created by estacion on 5/06/18.
 */
class ServiceResumen {
    var id_tipo_inmueble =""
    var pisos = "" //from 1 to 3
    var urgente = "" //si, no
    var hora = "" //militar time
    var comentario = ""
    var tipo_cobro = "" //espacios, pisos
    var category = ""
    var date = "" //yyyy-MM-dd
    var address = ""
    var id_direccion = ""
    var dimension = ""
    var id_dimension = ""
    var totalCost = ""
    var espacios = ArrayList<Espacios>()
}