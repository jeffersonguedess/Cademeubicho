package br.cademeubicho.webservice.controller

import br.cademeubicho.webservice.WebService
import br.cademeubicho.webservice.model.Localidade
import br.cademeubicho.webservice.model.Usuario
import br.cademeubicho.webservice.rotas.ConsultasServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultasController {
    private val ws = WebService.instance
    private val service = ws?.create(ConsultasServices::class.java)

    fun localidadesServices(uf: String): MutableList<String> {
        val call = service?.getCidadesIbge(uf)
        val cidadesList = mutableListOf<String>()

        call?.enqueue(object : Callback<Localidade> {
            override fun onResponse(call: Call<Localidade>, response: Response<Localidade>) {
                if (response.code() == 200) {
                    response.let { result ->
                        result.body()?.cidades?.let { cidades -> cidadesList.addAll(cidades) }
                    }
                }
            }

            override fun onFailure(call: Call<Localidade>, t: Throwable) {
                println(t)
            }
        })
        return cidadesList
    }


    fun buscaUsuarios(uid: String?): Usuario {
        val call = uid?.let { service?.getUsuario(it) }
        val usuario = Usuario()//mutableListOf<String>()

        mutableListOf<Usuario>()

        call?.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.code() == 200) {
                    response.let { result ->
                        result.body()?.let {  usuario }
                        println(result.body()?.nomeUsuario)
                    }
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                println(t)
            }
        })
        return usuario
    }
}