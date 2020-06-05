package br.cademeubicho.ui.meusbichosperdidos.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.cademeubicho.R
import br.cademeubicho.webservice.model.PostConsulta
import kotlinx.android.synthetic.main.adapter_animais.view.*


class MeusBischosPerdidosAdapter(private val meusBichosPerdidos: List<PostConsulta>, private val context: Context) :
    RecyclerView.Adapter<MeusBischosPerdidosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.adapter_animais, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int = meusBichosPerdidos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = meusBichosPerdidos[position]
        holder.nomeAnimal.text = animal.nomeAnimal
        holder.racaAnimal.text = animal.racaAnimal
        holder.idadeAnimal.text = animal.idadeAnimal.toString()
        holder.corAnimal.text = animal.corAnimal

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeAnimal = itemView.txtnomeAnimal
        val racaAnimal = itemView.txtracaAnimal
        val idadeAnimal = itemView.txtidadeAnimal
        val corAnimal = itemView.txtcorAnimal

    }


}