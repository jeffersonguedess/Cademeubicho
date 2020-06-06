package br.cademeubicho.ui.detalhes

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import br.cademeubicho.webservice.model.PostConsulta
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_animais_detalhes.*
import kotlinx.android.synthetic.main.activity_maps.*
import java.net.URLEncoder


class AnimaisDetalhesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POST = "EXTRA_POST"
    }

    val phone = String()
    val message = String()
    private var post: PostConsulta? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animais_detalhes)

        post = intent.getParcelableExtra(EXTRA_POST)

        txtViewAnimalPorte.text = post?.descricaoPorte
        textTipo.text = post?.descricaoTipo
        etNomeAnimal.setText(post?.nomeAnimal)
        editTextNumber.setText(post?.idadeAnimal)
        etcorAnimal.setText(post?.corAnimal)
        etracaAnimal.setText(post?.racaAnimal)
        etrecompensa.setText(post?.recompensa)
        btnWhatsApp.setOnClickListener {
            val packageManager: PackageManager = packageManager
            val i = Intent(Intent.ACTION_VIEW)

            try {
                val url = post?.celularWhatsApp + "&text=" + URLEncoder.encode(
                    message,
                    "UTF-8"
                )
                i.setPackage("com.whatsapp")
                i.data = Uri.parse(url)
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        Picasso.get()
            .load(loadMap())
            .placeholder(R.mipmap.logo_camera_round)
            .into(imgMostraLocal)

        imgMostraLocal.setOnClickListener {
            try {

                val myLatitude = post?.latitude
                val myLongitude = post?.longitude
                val labelLocation = "Local Perda!"
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("geo:<$myLatitude>,<$myLongitude>?q=<$myLatitude>,<$myLongitude>($labelLocation)")
                )
                startActivity(intent)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun loadMap(): String {
        val mapUrlInitial = "https://maps.googleapis.com/maps/api/staticmap?center="
        val latLong: String = post?.latitude + "," + post?.longitude
        val mapUrlProperties = "&zoom=20&size=800x400"
        val mapUrlMapAPI = "&key=AIzaSyBx-3_O9kRnk67rhwWq7Zf0JAi4eAvQFIc"
        val mapMarker = "&markers=color:red%7C"

        return mapUrlInitial + latLong + mapUrlProperties + mapMarker + latLong + mapUrlMapAPI
    }

}