package br.cademeubicho.webservice.model

import com.google.gson.annotations.SerializedName

class Status {

    @SerializedName("StatusMensagem")
    var statusMensagem: String? = null

    @SerializedName("Retorno")
    var retorno: String? = null

}