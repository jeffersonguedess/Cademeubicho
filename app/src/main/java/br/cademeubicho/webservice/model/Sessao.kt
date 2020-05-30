package br.cademeubicho.webservice

import android.os.StrictMode
import br.cademeubicho.webservice.controller.CadastrosController
import br.cademeubicho.webservice.controller.ConsultasController
import br.cademeubicho.webservice.model.Usuario
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseUserMetadata

object Sessao {

    private var userSessao = Usuario(
        "", "",
        "", "", 0, "", ""
    )

    private lateinit var latitude: String
    private lateinit var longitude: String

    fun initUser(user: Usuario?) {
        println("initUser")
        println(user)
        if (user != null) {
            userSessao = user
        }
        var l = createLocationRequest()
        print("LOCAAAAAAAAAAAATION")
        print(l.toString())
    }

    fun getUser(): Usuario {
        return userSessao
    }

    fun getLatitude(): String {
        return latitude
    }

    fun getLongitude(): String {
        return longitude
    }

    val instance: Usuario by lazy {
        userSessao
    }


    fun loadSessao(uid: String): Boolean {
        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val consulta = ConsultasController()
        val cadastro = CadastrosController()


        if (uid == "") {
            println("USUARIO SEM UID - NÃO AUTENTICADO")
            return false
        }

        var responseWS = consulta.buscaUsuario(uid)

        if (responseWS.uidFirebase != "") { //USUARIO CADASTRADO NO WS
            initUser(responseWS)
            return true
        } else {
            println("USUARIO NÃO ENCONTRADO NO WEBSERVICE. FAZER CADASTRO")
            val user = Usuario(
                FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                "", "",
                FirebaseAuth.getInstance().currentUser?.email.toString(),
                0, uid, ""
            )

            val statusCadastro = cadastro.cadastroUsuario(user)

            if (statusCadastro.retorno.toLowerCase() == "true") {
                println("USUARIO CADASTRADO COM SUSCESSO NO WEBSERVICE")

                responseWS = consulta.buscaUsuario(uid)
                initUser(responseWS)
                return true

            } else {
                println("ACORREU ALGUM ERRO AO CADASTRAR NO WS")
                println(statusCadastro)
                return false
            }
        }
    }

    private fun createLocationRequest(): LocationSettingsRequest.Builder? {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        return locationRequest?.let {
            LocationSettingsRequest.Builder()
                .addLocationRequest(it)
        }
    }

}
