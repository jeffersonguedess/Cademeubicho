package br.cademeubicho.webservice.model

import com.google.gson.annotations.SerializedName


data class Usuario (
    var nomeUsuario: String,
    var numeroCelular: String,
    var dddCelular: String,
    var emailUsuario: String,
    var distanciaFeed: Int,
    var uidFirebase: String,
    var idFacebook : String
){


}