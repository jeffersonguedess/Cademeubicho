package br.cademeubicho.webservice.rotas
import br.cademeubicho.webservice.model.*
import retrofit2.Call
import retrofit2.http.*

interface CadastrosServices {

    @FormUrlEncoded
    @POST("CadastrarUsuario")
    fun cadastraUsuario(@FieldMap user : Usuario): Call<Status>
}