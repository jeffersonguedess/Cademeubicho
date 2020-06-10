package br.cademeubicho.webservice.controller

import android.os.StrictMode
import br.cademeubicho.model.PostConsulta
import br.cademeubicho.model.Posts
import br.cademeubicho.model.Usuario
import br.cademeubicho.webservice.api.RetrofitClient
import retrofit2.Call
import retrofit2.Response


class ConsultasController {
    private val userResponse = Usuario(
        "", "", "",
        "", 0, "", ""
    )

    fun buscaUsuario(uid: String): Usuario {


        val call: Call<Usuario> = RetrofitClient.instance.userLogin(uid)
        val response: Response<Usuario> = call.execute()

        if (response.isSuccessful) {
            userResponse.uidFirebase = response.body()!!.uidFirebase
            userResponse.nomeUsuario = response.body()!!.nomeUsuario
            userResponse.emailUsuario = response.body()!!.emailUsuario
            userResponse.numeroCelular = response.body()!!.numeroCelular
            userResponse.dddCelular = response.body()!!.dddCelular
            userResponse.distanciaFeed = response.body()!!.distanciaFeed
            userResponse.idFacebook = response.body()!!.idFacebook
        }
        return userResponse
    }


    fun buscarPosts(uid: String, longitude: String, latitude: String): List<PostConsulta>? {
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val response: Response<Posts> = RetrofitClient.instance.getPosts(
            uid, longitude, latitude
        ).execute()
        if (response.isSuccessful) {
            return response.body()!!.Posts
        }
        return null

    }

    fun buscarMeusPosts(uid: String): List<PostConsulta>? {

        val response: Response<Posts> = RetrofitClient.instance.getMeusPosts(uid).execute();
        if (response.isSuccessful) {
            return response.body()!!.Posts
        }
        return null

    }


}
