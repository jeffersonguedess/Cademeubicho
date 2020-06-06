package br.cademeubicho.ui.meusbichosperdidos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.cademeubicho.BaseFragment
import br.cademeubicho.R
import br.cademeubicho.ui.adapter.AnimaisAdapter
import br.cademeubicho.ui.detalhes.AnimaisDetalhesActivity
import br.cademeubicho.model.Sessao
import br.cademeubicho.webservice.controller.ConsultasController
import br.cademeubicho.model.PostConsulta
import kotlinx.android.synthetic.main.fragment_meus_bichos_perdidos.*

class MeusBichosPerdidosFragment : BaseFragment() {

    private lateinit var listaPosts: List<PostConsulta>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_meus_bichos_perdidos, container, false)

        return root
    }

    override fun onStart() {

        listaPosts = ConsultasController().buscarMeusPosts(Sessao.getUser().uidFirebase)!!

        val adapter = AnimaisAdapter(listaPosts)
        rec_meus_bichos_perdidos.adapter = adapter

        adapter.listener = object : AnimaisAdapter.Listener {
            override fun onCardClicked(postConsultas: PostConsulta) {
                chamaDetalhes(postConsultas)
            }
        }

        super.onStart()
    }

    //TODO mudar activity
    private fun chamaDetalhes(postConsultas: PostConsulta) {
        val intent = Intent(context, AnimaisDetalhesActivity::class.java)
        intent.putExtra(AnimaisDetalhesActivity.EXTRA_POST, postConsultas)
        startActivity(intent)
    }
}
