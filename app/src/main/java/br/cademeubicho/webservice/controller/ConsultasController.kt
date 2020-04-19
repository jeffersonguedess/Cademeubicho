package br.cademeubicho.webservice.controller

import br.cademeubicho.webservice.WebServiceInitializer
import br.cademeubicho.webservice.model.Localidade
import br.cademeubicho.webservice.model.Usuario
import br.cademeubicho.webservice.rotas.ConsultasServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultasController {
    private val ws = WebServiceInitializer().getWebService()
    private val service = ws?.create(ConsultasServices::class.java)

    fun localidadesServices(uf: String): MutableList<String> {
        val call = service?.getCidadesIbge(uf)
        val resul = mutableListOf<String>()
        
        call?.enqueue(object : Callback<Localidade> {
            override fun onResponse(call: Call<Localidade>, response: Response<Localidade>) {
                if (response.code() == 200) {

                    response.let {result->
                        result.body()?.cidades?.let { cidades -> resul.addAll(cidades) }
                    }
                }
            }

            override fun onFailure(call: Call<Localidade>, t: Throwable) {
                println(t)
            }
        })
        return resul
    }


    fun autorizaAcesso(uid: String): Array<String> {
        val call = service?.getUsuario(uid)
        val resul = arrayOf("")

        call?.enqueue(object : Callback<Usuario> {
            override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                if (response.code() == 200) {
                    response.body()!!.cidadeUsuario
                }
            }

            override fun onFailure(call: Call<Usuario>, t: Throwable) {
                println(t)
            }
        })
        return resul
    }
}