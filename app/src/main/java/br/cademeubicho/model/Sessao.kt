package br.cademeubicho.model

import android.os.StrictMode
import br.cademeubicho.webservice.controller.CadastrosController
import br.cademeubicho.webservice.controller.ConsultasController
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.firebase.auth.FirebaseAuth

object Sessao {

    private var userSessao = Usuario(
        "", "",
        "", "", 0, "", ""
    )

    fun initUser(user: Usuario?) {

        if (user != null) {
            userSessao = user
        }

        println(createLocationRequest().toString());

    }

    fun getUser(): Usuario {
        return userSessao
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
            return false
        }

        var responseWS = consulta.buscaUsuario(FirebaseAuth.getInstance().currentUser?.uid!!)

        if (responseWS.uidFirebase != "") { //USUARIO CADASTRADO NO WS
            initUser(responseWS)
            return true
        } else {
            val user = Usuario(
                FirebaseAuth.getInstance().currentUser?.displayName.toString(),
                "", "",
                FirebaseAuth.getInstance().currentUser?.email.toString(),
                0, uid, ""
            )

            val statusCadastro = cadastro.cadastroUsuario(user)
            if (statusCadastro.retorno.toLowerCase() == "true") {
                responseWS = consulta.buscaUsuario(uid)
                initUser(responseWS)
                return true

            } else {
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
