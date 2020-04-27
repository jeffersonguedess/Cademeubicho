package br.cademeubicho

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.webservice.Sessao
import br.cademeubicho.webservice.controller.CadastrosController
import br.cademeubicho.webservice.controller.ConsultasController
import br.cademeubicho.webservice.model.Usuario
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    companion object {
        const val RC_SIGN_IN = 1
    }

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
        AuthUI.IdpConfig.FacebookBuilder().build()
    )



    override fun onResume() {
        super.onResume()
        initSignIn()
    }

    private fun initSignIn() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setIsSmartLockEnabled(false)
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.logo_cortado) // Set logo drawable
                .build(),
            RC_SIGN_IN
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {

            val response = IdpResponse.fromResultIntent(data)

            when {
                resultCode == Activity.RESULT_OK -> {
                    var user = Usuario(
                        FirebaseAuth.getInstance().currentUser?.displayName,
                        FirebaseAuth.getInstance().currentUser?.email,
                        FirebaseAuth.getInstance().currentUser?.uid)


                    println(user?.nomeUsuario)
                    println(user?.uidFirebase)
                    println(user?.emailUsuario)

                    var status = CadastrosController().cadastraUsuario(user)

                    if (status != null){
                       user =  ConsultasController().buscaUsuarios(user?.uidFirebase)
                       Sessao().initUser(user)
                    }




                    Toast.makeText(this, "Seja bem vindo ${user?.nomeUsuario}", Toast.LENGTH_LONG).show()

                    chamaMain()
                }
                response == null -> {
                    chamaMain()
                }
                else -> {
                    Toast.makeText(this, "Erro no login.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun chamaMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}
