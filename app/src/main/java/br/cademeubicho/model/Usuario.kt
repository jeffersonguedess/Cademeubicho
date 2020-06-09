package br.cademeubicho.model

data class Usuario (
    var nomeUsuario: String,
    var numeroCelular: String,
    var dddCelular: String,
    var emailUsuario: String,
    var distanciaFeed: Int,
    var uidFirebase: String,
    var idFacebook : String
)