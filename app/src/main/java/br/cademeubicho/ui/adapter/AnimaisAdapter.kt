package br.cademeubicho.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import br.cademeubicho.R
import br.cademeubicho.model.PostConsulta
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.adapter_animais.view.*
import java.lang.Exception


class AnimaisAdapter(
    private val animaisDesaparecidos: List<PostConsulta>
) :
    RecyclerView.Adapter<AnimaisAdapter.ViewHolder>() {

    var listener: Listener? = null

    interface Listener {
        fun onCardClicked(postConsultas: PostConsulta)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val containerView = layoutInflater.inflate(viewType, parent, false)
        return ViewHolder(containerView)
    }

    override fun getItemViewType(position: Int): Int = R.layout.adapter_animais

    override fun getItemCount(): Int = animaisDesaparecidos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = animaisDesaparecidos[position]

        holder.nomeAnimal.text = animal.nomeAnimal
        holder.racaAnimal.text = animal.racaAnimal

        if (animal.idadeAnimal.equals("1")) {
            holder.idadeAnimal.text = animal.idadeAnimal + " ano"
        } else {
            holder.idadeAnimal.text = animal.idadeAnimal + " anos"
        }
        holder.corAnimal.text = animal.corAnimal

        Picasso.get()
            .load(if (animal.imagens?.isEmpty()!!) null else animal.imagens?.get(0))
            .error(R.mipmap.logo_camera_round)
            .into(holder.ivGallery, object : Callback {

                override fun onSuccess() {
                    holder.progress.visibility = View.GONE
                }

                override fun onError(e: Exception?) {}

            })

    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivGallery = itemView.ivGallery
        val progress = itemView.progress
        val nomeAnimal = itemView.txtnomeAnimal
        val racaAnimal = itemView.txtracaAnimal
        val idadeAnimal = itemView.txtidadeAnimal
        val corAnimal = itemView.txtcorAnimal

        init {
            itemView.cardsPost.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    val post = animaisDesaparecidos[adapterPosition]
                    post.let { it1 -> listener?.onCardClicked(it1) }
                }
            }
        }

    }


}