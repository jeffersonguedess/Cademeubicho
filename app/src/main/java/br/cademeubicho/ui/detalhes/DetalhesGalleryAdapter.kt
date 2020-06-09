package br.cademeubicho.ui.detalhes

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import br.cademeubicho.R
import com.squareup.picasso.Picasso

class DetalhesGalleryAdapter(private val context: Context, private var listImgs: List<String>?) :
    BaseAdapter() {

    private var inflater: LayoutInflater? = null
    private var ivGallery: ImageView? = null

    override fun getCount(): Int {
        return listImgs?.size!!
    }

    override fun getItem(position: Int): String? {
        return listImgs?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View? {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View? = this.inflater!!.inflate(R.layout.gv_item, parent, false)
        ivGallery = itemView?.findViewById(R.id.ivGallery) as ImageView

        Picasso.get()
            .load(if(listImgs?.get(position)?.isEmpty()!!)null else listImgs?.get(position))
            .into(ivGallery)

        return itemView
    }

}