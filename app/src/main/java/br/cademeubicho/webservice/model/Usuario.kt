package br.cademeubicho.webservice.model

import com.google.gson.annotations.SerializedName

class Usuario {

    @SerializedName("nomeUsuario")
    var nomeUsuario: String? = null

    @SerializedName("numeroCelular")
    var numeroCelular: String? = null

    @SerializedName("dddCelular")
    var dddCelular: String? = null

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

    constructor()
    constructor(
        nomeUsuario: String?,
        numeroCelular: String?,
        dddCelular: String?,
        emailUsuario: String?,
        ufUsuario: String?,
        cidadeUsuario: String?,
        distanciaFeed: Float?,
        uidFirebase: String?
    ) {
        this.nomeUsuario = nomeUsuario
        this.numeroCelular = numeroCelular
        this.dddCelular = dddCelular
        this.emailUsuario = emailUsuario
        this.ufUsuario = ufUsuario
        this.cidadeUsuario = cidadeUsuario
        this.distanciaFeed = distanciaFeed
        this.uidFirebase = uidFirebase
    }

}