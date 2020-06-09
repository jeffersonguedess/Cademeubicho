package br.cademeubicho.ui.detalhes

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import br.cademeubicho.model.PostConsulta
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_animais_detalhes.*


class AnimaisDetalhesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POST = "EXTRA_POST"
    }

    private var post: PostConsulta? = null
    private var galleryAdapter: DetalhesGalleryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animais_detalhes)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        post = intent.getParcelableExtra(EXTRA_POST)

        if (post != null) {
            galleryAdapter = DetalhesGalleryAdapter(this, post?.imagens)
            gv.adapter = galleryAdapter

            txtViewAnimalPorte.text = post?.descricaoPorte
            textTipo.text = post?.descricaoTipo
            etNomeAnimal.setText(post?.nomeAnimal)
            if (post?.idadeAnimal.equals("1")) {
                etIdadeAnimal.setText(post?.idadeAnimal + " ano")
            } else {
                etIdadeAnimal.setText(post?.idadeAnimal + " anos")
            }
            etcorAnimal.setText(post?.corAnimal)
            etRacaAnimal.setText(post?.racaAnimal)
            etrecompensa.text = post?.recompensa
        }

        /*  OCULTAR BOTAO DE CONTATO CASO O CARA NAO ESTEJA LOGADO
        *   OU SEJA O MESMO USUARIO QUE FEZ O POST
        * */
        if (FirebaseAuth.getInstance().currentUser == null
            || FirebaseAuth.getInstance().currentUser?.uid == post?.idFirebaseUsu
        ){
            textView.setText("")
            textView2.setText("")
            textView3.setText("")
            btnWhatsApp.visibility = View.GONE
            btnChat.visibility = View.GONE
        }




        /*OCULTAR O BOTAO DO WHATSAPP CASO NAO TENHA VINDO UM NUMERO VALIDO*/

        if (post?.celularWhatsApp == ""){
            textView3.setText("")
            btnWhatsApp.visibility = View.GONE
        }

        btnWhatsApp.setOnClickListener {
            val packageManager: PackageManager = packageManager
            val i = Intent(Intent.ACTION_VIEW)

            try {
                val url = post?.celularWhatsApp
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
            .error(R.mipmap.logo_camera_round)
            .into(imgMostraLocal, object : Callback {

                override fun onSuccess() {
                    progress.visibility = View.GONE
                }

                override fun onError(e: java.lang.Exception?) {}

            })

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
        val mapUrlProperties = "&zoom=15&size=400x400"
        val mapUrlMapAPI = "&key=AIzaSyBx-3_O9kRnk67rhwWq7Zf0JAi4eAvQFIc"
        val mapMarker = "&markers=color:red%7C"

        return mapUrlInitial + latLong + mapUrlProperties + mapMarker + latLong + mapUrlMapAPI
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}