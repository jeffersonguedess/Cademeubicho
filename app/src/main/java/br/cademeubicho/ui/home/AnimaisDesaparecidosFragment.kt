package br.cademeubicho.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.cademeubicho.LoginActivity
import br.cademeubicho.R
import br.cademeubicho.model.PostConsulta
import br.cademeubicho.model.Sessao
import br.cademeubicho.ui.adapter.AnimaisAdapter
import br.cademeubicho.ui.cadastroanimal.CadastroAnimalActivity
import br.cademeubicho.ui.detalhes.AnimaisDetalhesActivity
import br.cademeubicho.webservice.controller.ConsultasController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_animais_desaparecidos.*

class AnimaisDesaparecidosFragment : Fragment() {

    private lateinit var listaPosts: List<PostConsulta>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var uid = ""
        if (FirebaseAuth.getInstance().currentUser != null){
            uid = FirebaseAuth.getInstance().currentUser!!.uid
        }
        listaPosts = ConsultasController().buscarPosts(
            uid,
            "",
            ""
        )!!

        return inflater.inflate(R.layout.fragment_animais_desaparecidos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabCadastrar.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val intent: Intent?

            intent = if (user != null) {
                Intent(activity, CadastroAnimalActivity::class.java)
            } else {
                Intent(activity, LoginActivity::class.java)
            }
            startActivity(intent)
        }
    }

    override fun onStart() {
        validaLogin()

        val adapter = AnimaisAdapter(listaPosts)
        rec_desaparecidos.adapter = adapter

        adapter.listener = object : AnimaisAdapter.Listener {
            override fun onCardClicked(postConsultas: PostConsulta) {
                chamaDetalhes(postConsultas)
            }
        }

        super.onStart()
    }

    private fun chamaDetalhes(postConsultas: PostConsulta) {
        val intent = Intent(context, AnimaisDetalhesActivity::class.java)
        intent.putExtra(AnimaisDetalhesActivity.EXTRA_POST, postConsultas)
        startActivity(intent)
    }

    private fun validaLogin() {
        println("VALIDA LOGIN")
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null && Sessao.getUser().uidFirebase == "") {
            //CARA ENTÁ LOGADO (NÃO TEM COMO SABER SE ELE
            // VEIO DIRETO DA PAGINA DE LOGIN OU SE REABRIU O APP
            //POR VIA DAS DUVIDAS, RECARREGO A VARIAVEL DE SESSAO
            if (Sessao.loadSessao(user.uid)) {
                //conseguiu preencher a variavel de sessao
                if (Sessao.getUser().distanciaFeed == 0
                    || Sessao.getUser().nomeUsuario == ""
                    || Sessao.getUser().dddCelular.isEmpty()
                    || Sessao.getUser().numeroCelular.isEmpty()
                    || Sessao.getUser().emailUsuario == ""
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Por favor, finalize seu cadastro!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


}