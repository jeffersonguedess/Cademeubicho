package br.cademeubicho.webservice.model

import com.google.gson.annotations.SerializedName

class Localidade {
    @SerializedName( "Cidades" )
    var cidades: List<String>? =  null

}