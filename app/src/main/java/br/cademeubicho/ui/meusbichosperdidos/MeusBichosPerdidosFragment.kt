package br.cademeubicho.ui.meusbichosperdidos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.cademeubicho.BaseFragment
import br.cademeubicho.R
import br.cademeubicho.model.MeusBichosPerdidos
import br.cademeubicho.ui.meusbichosperdidos.adapter.MeusBischosPerdidosAdapter
import kotlinx.android.synthetic.main.fragment_meus_bichos_perdidos.*

class MeusBichosPerdidosFragment : BaseFragment() {

    val listAnimais = mutableListOf(
        MeusBichosPerdidos("snoop", "viralata", 12, "marelo"),
        MeusBichosPerdidos("snoop", "viralata", 12, "marelo"),
        MeusBichosPerdidos("snoop", "viralata", 12, "marelo"),
        MeusBichosPerdidos("snoop", "viralata", 12, "marelo"),
        MeusBichosPerdidos("snoop", "viralata", 12, "marelo"),
        MeusBichosPerdidos("snoop", "viralata", 12, "marelo"),
        MeusBichosPerdidos("snoop", "viralata", 12, "marelo")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_meus_bichos_perdidos, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rec_meus_bichos_perdidos.adapter = context?.let { MeusBischosPerdidosAdapter(listAnimais, it) }
        //Toast.makeText(activity, "foi", Toast.LENGTH_SHORT).show()
    }
}
