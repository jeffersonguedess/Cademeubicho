package br.cademeubicho.ui.cadastroanimal

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import br.cademeubicho.R
import java.io.ByteArrayOutputStream

class GalleryAdapter(private val context: Context, private var mArrayUris: ArrayList<Uri>) :
    BaseAdapter() {

    private var pos: Int = 0
    private var inflater: LayoutInflater? = null
    private var ivGallery: ImageView? = null

    override fun getCount(): Int {
        return mArrayUris.size
    }

    override fun getItem(position: Int): Any {
        return mArrayUris[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, p1: View?, parent: ViewGroup?): View? {

        pos = position
        inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView: View? = this.inflater!!.inflate(R.layout.gv_item, parent, false)

        ivGallery = itemView?.findViewById(R.id.ivGallery) as ImageView

        ivGallery!!.setImageURI(mArrayUris[position])

        return itemView
    }

}