package br.cademeubicho.webservice

import br.cademeubicho.BuildConfig
import br.cademeubicho.webservice.model.Usuario
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Sessao {

    private var userSessao: Usuario? = null

    fun getUser(): Usuario? {
        return userSessao
    }

    fun initUser(user : Usuario?){
        userSessao = user
    }


    companion object {
        val instance = Sessao().getUser()
    }


}
