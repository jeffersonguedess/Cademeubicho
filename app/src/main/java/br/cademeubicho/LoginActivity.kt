package br.cademeubicho

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.webservice.Sessao
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.adapter_animais_desaparecidos.*
import kotlinx.android.synthetic.main.content_main.*

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
                .setLogo(R.mipmap.logo_procuranimal_gif_1) // Set logo drawable
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
                    val r = Sessao.loadSessao(FirebaseAuth.getInstance().currentUser?.uid!!)
                    if (r){
                        println(Sessao.instance.nomeUsuario)
                        Toast.makeText(this, "Seja bem vindo ${Sessao.instance.nomeUsuario}", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this, "Erro no login.", Toast.LENGTH_SHORT).show()
                    }

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
