package br.cademeubicho.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.cademeubicho.R
import br.cademeubicho.model.PostConsulta
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
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

    val storageReference = FirebaseStorage.getInstance("gs://cade-meu-bicho.appspot.com")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animal = animaisDesaparecidos[position]

        holder.nomeAnimal.text = animal.nomeAnimal
        holder.racaAnimal.text = animal.racaAnimal
        if (animal.idadeAnimal.equals("1")) {
            holder.idadeAnimal.text = animal.idadeAnimal + " ano"
        }else{
            holder.idadeAnimal.text = animal.idadeAnimal + " anos"
        }
        holder.corAnimal.text = animal.corAnimal

        println(animal.imagens)
        println(animal.imagens?.get(0))

        try {
            //var url = animal.imagens?.get(0)
            var url = "https://firebasestorage.googleapis.com/v0/b/cade-meu-bicho.appspot.com/o/file-20200309-118956-1cqvm6j.jpg?alt=media&token=a895c217-7ac5-4325-baa6-e7e38c73abde"

            Glide. with(holder.imageView  ).load(storageReference.getReferenceFromUrl(url.toString()))
                .into(holder.imageView )
        } catch(e : Exception){println( e)}
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeAnimal = itemView.txtnomeAnimal
        val racaAnimal = itemView.txtracaAnimal
        val idadeAnimal = itemView.txtidadeAnimal
        val corAnimal = itemView.txtcorAnimal
        val imageView = itemView.ivGallery
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