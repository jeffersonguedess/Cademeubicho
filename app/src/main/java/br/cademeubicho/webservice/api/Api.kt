package br.cademeubicho.webservice.api

import br.cademeubicho.webservice.model.Status
import br.cademeubicho.webservice.model.Usuario
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api{

    @FormUrlEncoded
    @POST("CadastrarUsuario")
    fun cadastrarUsuario(
        @Field("uidFirebase") uidFirebase:String,
        @Field("nomeUsuario") nomeUsuario:String,
        @Field("distanciaFeed") distanciaFeed:Int,
        @Field("emailUsuario") emailUsuario:String,
        @Field("numeroCelular") numeroCelular:String,
        @Field("dddCelular") dddCelular:String
    ):Call<Status>

    @FormUrlEncoded
    @POST("AtualizarUsuario")
    fun atualizaUsuario(
        @Field("uidFirebase") uidFirebase:String,
        @Field("nomeUsuario") nomeUsuario:String,
        @Field("distanciaFeed") distanciaFeed:Int,
        @Field("emailUsuario") emailUsuario:String,
        @Field("numeroCelular") numeroCelular:String,
        @Field("dddCelular") dddCelular:String
    ):Call<Status>



    @FormUrlEncoded
    @POST("Login")
    fun userLogin(
        @Field("uidFirebase") uidFirebase:String
    ):Call<Usuario>


}