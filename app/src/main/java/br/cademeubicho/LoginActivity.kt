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
import com.firebase.ui.auth.data.model.PhoneNumber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

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
                    val nome_usuario = FirebaseAuth.getInstance().currentUser?.displayName
                    val uid = FirebaseAuth.getInstance().currentUser?.uid!!

                    val usuarioLogado = ConsultasController().buscaUsuario(uid)
                    if (usuarioLogado.uidFirebase == ""){

                        usuarioLogado.uidFirebase = uid
                        usuarioLogado.nomeUsuario = FirebaseAuth.getInstance().currentUser?.displayName.toString()
                        usuarioLogado.emailUsuario = FirebaseAuth.getInstance().currentUser?.email.toString()


                        val status = CadastrosController().cadastroUsuario(usuarioLogado)

                        if (status.statusMensagem.toLowerCase().equals("true")){
                                loadUsuario(uid)
                        }else{
                            println (status.statusMensagem)
                            println (status.retorno)
                            Toast.makeText(this, "Erro no login.", Toast.LENGTH_SHORT).show()
                        }
                    }else {
                          loadUsuario(uid)
                    }

                    Toast.makeText(this, "Seja bem vindo $nome_usuario", Toast.LENGTH_LONG).show()

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

    private fun loadUsuario(uid:String){
        val usuario = ConsultasController().buscaUsuario(uid)
        Sessao().initUser(usuario)


        println (Sessao().getUser()?.uidFirebase)

        if (usuario.emailUsuario == ""
            || usuario.dddCelular == ""
            || usuario.numeroCelular == ""
            ||usuario.nomeUsuario == ""
            || usuario.distanciaFeed == 0
        ){
            Toast.makeText(this, "Por favor, finalize seu cadastro.", Toast.LENGTH_SHORT).show()
//            chama tela de cadastro
        }


    }

    private fun chamaMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }


}
