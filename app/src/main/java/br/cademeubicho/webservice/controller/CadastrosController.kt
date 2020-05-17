package br.cademeubicho.webservice.controller

import br.cademeubicho.webservice.api.RetrofitClient
import br.cademeubicho.webservice.model.Status
import br.cademeubicho.webservice.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CadastrosController {
    val statusResponse = Status("", "")

    fun cadastroUsuario(user: Usuario) : Status{
        RetrofitClient.instance.cadastrarUsuario(
                user.uidFirebase, user.nomeUsuario,
                user.distanciaFeed, user.emailUsuario,
                user.numeroCelular, user.dddCelular
        ).enqueue(object: Callback<Status>{
            override fun onFailure(call: Call<Status>, t: Throwable) {
                return ; //Toast.makeText(applicationContext, t.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                statusResponse.statusMensagem = response.body()!!.statusMensagem
                statusResponse.retorno = response.body()!!.retorno

            }
        })

        return statusResponse
    }


}