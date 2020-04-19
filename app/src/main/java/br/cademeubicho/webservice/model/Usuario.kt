package br.cademeubicho.webservice.model

import com.google.gson.annotations.SerializedName

class Usuario {

    @SerializedName("nomeUsuario")
    var nomeUsuario: String? = null

    @SerializedName("telefoneCelular")
    var telefoneCelular: String? = null

    @SerializedName("emailUsuario")
    var emailUsuario: String? = null

    @SerializedName("ufUsuario")
    var ufUsuario: String? = null

    @SerializedName("cidadeUsuario")
    var cidadeUsuario: String? = null

    @SerializedName("distanciaFeed")
    var distanciaFeed: Float? = null

    @SerializedName("uidFirebase")
    var uidFirebase: String? = null

    @SerializedName("CodigoRetorno")
    var CodigoRetorno: Int? = null

}