package br.cademeubicho.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.cademeubicho.LoginActivity
import br.cademeubicho.R
import br.cademeubicho.model.AnimaisDesaparecidos
import br.cademeubicho.ui.cadastroanimal.CadastroAnimalActivity
import br.cademeubicho.ui.home.adapter.AnimaisDesaparecidosAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_animais_desaparecidos.*

class AnimaisDesaparecidosFragment : Fragment() {

    val listAnimais = mutableListOf(
        AnimaisDesaparecidos("snoop", "viralata", 12, "marelo"),
        AnimaisDesaparecidos("snoop", "viralata", 12, "marelo"),
        AnimaisDesaparecidos("snoop", "viralata", 12, "marelo"),
        AnimaisDesaparecidos("snoop", "viralata", 12, "marelo"),
        AnimaisDesaparecidos("snoop", "viralata", 12, "marelo"),
        AnimaisDesaparecidos("snoop", "viralata", 12, "marelo"),
        AnimaisDesaparecidos("snoop", "viralata", 12, "marelo")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_animais_desaparecidos, container, false)



        return root
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
        rec_desaparecidos.adapter = context?.let { AnimaisDesaparecidosAdapter(listAnimais, it) }
        //Toast.makeText(activity, "foi", Toast.LENGTH_SHORT).show()
    }


}
