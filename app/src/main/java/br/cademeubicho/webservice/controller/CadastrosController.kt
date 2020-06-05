package br.cademeubicho.webservice.controller

import br.cademeubicho.webservice.api.RetrofitClient
import br.cademeubicho.webservice.model.*
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field


class CadastrosController {
    val statusResponse = Status("", "")

    fun cadastroUsuario(user: Usuario) : Status {
        var response : Response<Status>
        response = RetrofitClient.instance.cadastrarUsuario(
            user.uidFirebase, user.nomeUsuario,
            user.distanciaFeed, user.emailUsuario,
            user.numeroCelular, user.dddCelular,
            user.idFacebook
        ).execute();

        return returnStatusResponse(response)

    }

    fun atualizaUsuario(user : Usuario) : Status{
        var response : Response<Status>
        response = RetrofitClient.instance.atualizaUsuario(
            user.uidFirebase, user.nomeUsuario,
            user.distanciaFeed, user.emailUsuario,
            user.numeroCelular, user.dddCelular
        ).execute();

        return returnStatusResponse(response)
    }


    fun cadastrarPost(post : PostCadastro) : Status{
        var response : Response<Status>
        response = RetrofitClient.instance.cadastrarPost(
            post.uidFirebase, post.porteAnimal,
            post.tipoAnimal, post.nomeAnimal,
            post.racaAnimal, post.idadeAnimal,
            post.corAnimal, post.recompensa,
            post.longitude, post.latitude,
            post.imagens
        ).execute();

        return returnStatusResponse(response)

    }

    private fun returnStatusResponse(response : Response<Status>) : Status{
        if (response.isSuccessful()) {
            statusResponse.statusMensagem = response.body()!!.statusMensagem
            statusResponse.retorno = response.body()!!.retorno
        }
        return statusResponse

    }
}





