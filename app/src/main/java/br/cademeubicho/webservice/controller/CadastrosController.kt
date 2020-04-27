package br.cademeubicho.webservice.controller

import br.cademeubicho.webservice.WebService
import br.cademeubicho.webservice.model.Status
import br.cademeubicho.webservice.rotas.CadastrosServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import br.cademeubicho.webservice.model.Usuario
import com.google.gson.internal.LinkedTreeMap

class CadastrosController {
    private val ws = WebService.instance
    private val service = ws?.create(CadastrosServices::class.java)


    fun cadastraUsuario(user: Usuario) : Status? {
        println (user)
        println (user?.nomeUsuario)
        println(user?.emailUsuario)
        val call = service?.cadastraUsuario(user!!)
        var status :Status?=null
        call?.enqueue(object : Callback<Status> {
            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                if (response.code() == 200) {
                    response.let { result ->
                        status = Status(result.body() as LinkedTreeMap<String, String>)
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