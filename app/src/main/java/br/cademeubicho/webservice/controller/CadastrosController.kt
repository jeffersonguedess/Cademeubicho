package br.cademeubicho.webservice.controller

import br.cademeubicho.webservice.api.RetrofitClient
import br.cademeubicho.webservice.model.Status
import br.cademeubicho.webservice.model.Usuario
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CadastrosController {
    val statusResponse = Status("", "")

    fun cadastroUsuario(user: Usuario) : Status {
        var response : Response<Status>
        response = RetrofitClient.instance.cadastrarUsuario(
            user.uidFirebase, user.nomeUsuario,
            user.distanciaFeed, user.emailUsuario,
            user.numeroCelular, user.dddCelular
        ).execute();

        if (response.isSuccessful()) {
            statusResponse.statusMensagem = response.body()!!.statusMensagem
            statusResponse.retorno = response.body()!!.retorno
        }
        return statusResponse
    }


}