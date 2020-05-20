package br.cademeubicho.webservice

import android.os.StrictMode
import br.cademeubicho.webservice.controller.CadastrosController
import br.cademeubicho.webservice.controller.ConsultasController
import br.cademeubicho.webservice.model.Usuario
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth


object Sessao {

    private var userSessao = Usuario(
        "", "",
        "", "", 0, ""
    )


    private lateinit var latitude: String
    private lateinit var longitude: String

    fun initUser(user: Usuario?) {
        println("initUser")
        println(user)
        if (user != null) {
            userSessao = user
        }
        //createLocationRequest()


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
                0, uid
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

    private fun createLocationRequest() {
       val mLocationRequest = LocationRequest()
        mLocationRequest.interval = 10000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
    }
   /* fun createLocationRequest(): LocationSettingsRequest.Builder? {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        return locationRequest?.let {
            LocationSettingsRequest.Builder()
                .addLocationRequest(it)
        }*/
}


