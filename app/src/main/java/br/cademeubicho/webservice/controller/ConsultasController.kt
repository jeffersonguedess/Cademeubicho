package br.cademeubicho.webservice.controller

import br.cademeubicho.webservice.api.RetrofitClient
import br.cademeubicho.webservice.model.Localidade
import br.cademeubicho.webservice.model.Status
import br.cademeubicho.webservice.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultasController {
    val statusResponse = Status("", "")
    val userResponse = Usuario("","","",
                                "",0,"")
    fun buscaUsuario(uid: String) : Usuario {
        RetrofitClient.instance.userLogin( uid ).enqueue(object: Callback<Usuario>{
            override fun onFailure(call: Call<Usuario>, t: Throwable) {

            }

            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                userResponse.uidFirebase = response.body()!!.uidFirebase
                userResponse.nomeUsuario = response.body()!!.nomeUsuario
                userResponse.emailUsuario = response.body()!!.emailUsuario
                userResponse.numeroCelular = response.body()!!.numeroCelular
                userResponse.dddCelular = response.body()!!.dddCelular
                userResponse.distanciaFeed = response.body()!!.distanciaFeed
            }
        })

        return userResponse
    }





}