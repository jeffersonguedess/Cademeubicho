package br.cademeubicho.webservice.controller

import br.cademeubicho.webservice.WebService
import br.cademeubicho.webservice.model.Localidade
import br.cademeubicho.webservice.model.Status
import br.cademeubicho.webservice.model.Usuario
import br.cademeubicho.webservice.rotas.CadastrosServices
import br.cademeubicho.webservice.rotas.ConsultasServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CadastrosController {
    private val ws = WebService.instance
    private val service = ws?.create(CadastrosServices::class.java)


    fun cadastraUsuario(user : Usuario) : Status {
        println (user)
        println (user.nomeUsuario)
        println(user.emailUsuario)
        val call = service?.cadastraUsuario(user)
        val status =  Status()//mutableListOf<String>()

        call?.enqueue(object : Callback<Status> {
            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                if (response.code() == 200) {
                    response.let { result ->
                        result.body()?.let { status  }
                    }
                }
            }

            override fun onFailure(call: Call<Status>, t: Throwable) {
                println(t)
            }
        })
        return status
    }
}