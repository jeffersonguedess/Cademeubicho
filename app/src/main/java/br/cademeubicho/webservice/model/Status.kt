package br.cademeubicho.webservice.model

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

class Status (data: LinkedTreeMap<String, String>){

    @SerializedName("StatusMensagem")
    var statusMensagem: String? = null

    @SerializedName("Retorno")
    var retorno: String? = null

    init {
        this.statusMensagem = data["StatusMensagem"]
        this.retorno = data["Retorno"]
    }

}
