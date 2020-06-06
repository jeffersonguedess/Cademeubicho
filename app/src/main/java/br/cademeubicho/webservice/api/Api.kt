package br.cademeubicho.webservice.api

import br.cademeubicho.model.Posts
import br.cademeubicho.model.Status
import br.cademeubicho.model.Usuario
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Api{



    /********************** CADASTRO DE USUARIOS **********************/
    @FormUrlEncoded
    @POST("CadastrarUsuario")
    fun cadastrarUsuario(
        @Field("uidFirebase") uidFirebase:String,
        @Field("nomeUsuario") nomeUsuario:String,
        @Field("distanciaFeed") distanciaFeed:Int,
        @Field("emailUsuario") emailUsuario:String,
        @Field("numeroCelular") numeroCelular:String,
        @Field("dddCelular") dddCelular:String,
        @Field("idFacebook") idFacebook:String
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


    /********************** CADASTRO DE POSTS **********************/
    @FormUrlEncoded
    @POST("CadastrarPost")
    fun cadastrarPost(
        @Field("uidFirebase") uidFirebase:String,
        @Field("porteAnimal") porteAnimal:String,
        @Field("tipoAnimal") tipoAnimal:String,
        @Field("nomeAnimal") nomeAnimal:String,
        @Field("racaAnimal") racaAnimal:String,
        @Field("idadeAnimal") idadeAnimal:String,
        @Field("corAnimal") corAnimal:String,
        @Field("recompensa") recompensa:String,
        @Field("longitude") longitude:String,
        @Field("latitude") latitude:String,
        @Field("imagens") imagens:String
    ):Call<Status>


    @FormUrlEncoded
    @POST("PostsProximos")
    fun getPosts(
        @Field("uidFirebase") uidFirebase:String,
        @Field("longitude") longitude:String,
        @Field("latitude") latitude:String
    ):Call<Posts>

    @FormUrlEncoded
    @POST("MeusPosts")
    fun getMeusPosts(
        @Field("uidFirebase") uidFirebase:String
    ):Call<Posts>



}


