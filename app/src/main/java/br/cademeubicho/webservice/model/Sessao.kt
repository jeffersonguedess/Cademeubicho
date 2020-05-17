package br.cademeubicho.webservice

import android.os.StrictMode
import android.widget.Toast
import br.cademeubicho.BuildConfig
import br.cademeubicho.webservice.controller.CadastrosController
import br.cademeubicho.webservice.controller.ConsultasController
import br.cademeubicho.webservice.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Sessao {

    private var userSessao = Usuario ("","",
        "", "", 0, "")

    fun initUser(user : Usuario?){
        println("initUser")
        println(user)
        if (user != null) {
            userSessao = user
        }
    }

    fun getUser(): Usuario {
        return userSessao
    }

    val instance : Usuario by lazy{
        userSessao
    }



    fun loadSessao(uid : String) : Boolean{
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val consulta = ConsultasController()
        val cadastro = CadastrosController()


        if (uid == ""){
            println ("USUARIO SEM UID - NÃO AUTENTICADO")
            return false
        }

        var responseWS = consulta.buscaUsuario(uid)

        if (responseWS.uidFirebase != ""){ //USUARIO CADASTRADO NO WS
            initUser(responseWS)
            return true
        }else {
            println ("USUARIO NÃO ENCONTRADO NO WEBSERVICE. FAZER CADASTRO")
            val user = Usuario(
                FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                "", "",
                FirebaseAuth.getInstance().currentUser?.email.toString(),
                0, uid
            )

            val statusCadastro = cadastro.cadastroUsuario(user)

            if (statusCadastro.retorno.toLowerCase() == "true"){
                println("USUARIO CADASTRADO COM SUSCESSO NO WEBSERVICE")

                responseWS = consulta.buscaUsuario(uid)
                initUser(responseWS)
                return true

            }else{
                println ("ACORREU ALGUM ERRO AO CADASTRAR NO WS")
                println (statusCadastro)
                return false
            }
        }
    }

}
