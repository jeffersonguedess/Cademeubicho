package br.cademeubicho.ui.meusbichosperdidos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.cademeubicho.BaseFragment
import br.cademeubicho.R
import br.cademeubicho.model.PostConsulta
import br.cademeubicho.ui.adapter.AnimaisAdapter
import br.cademeubicho.ui.cadastroanimal.CadastroAnimalActivity
import br.cademeubicho.webservice.controller.ConsultasController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_meus_bichos_perdidos.*

class MeusBichosPerdidosFragment : BaseFragment() {

    private lateinit var listaPosts: List<PostConsulta>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_meus_bichos_perdidos, container, false)
    }

    override fun onStart() {

        val uid: String
        if (FirebaseAuth.getInstance().currentUser != null) {
            uid = FirebaseAuth.getInstance().currentUser!!.uid
            listaPosts = ConsultasController().buscarMeusPosts(uid)!!

            val adapter = AnimaisAdapter(listaPosts)
            rec_meus_bichos_perdidos.adapter = adapter

            adapter.listener = object : AnimaisAdapter.Listener {
                override fun onCardClicked(postConsultas: PostConsulta) {
                    chamaDetalhes(postConsultas)
                }
            }
        }
        super.onStart()
    }

    private fun chamaDetalhes(postConsultas: PostConsulta) {
        val intent = Intent(context, CadastroAnimalActivity::class.java)
        intent.putExtra(CadastroAnimalActivity.EXTRA_POST, postConsultas)
        startActivity(intent)
    }
}
