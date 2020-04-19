package br.cademeubicho.webservice.rotas
import br.cademeubicho.webservice.model.*
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ConsultasServices {

    @FormUrlEncoded
    @POST("ConsultaCidadesIbge")
    fun getCidadesIbge(@Field("Estado") uf: String): Call<Localidade>

    @FormUrlEncoded
    @POST("Login")
    fun getUsuario(@Field("uidFirebase") uid: String): Call<Usuario>


}