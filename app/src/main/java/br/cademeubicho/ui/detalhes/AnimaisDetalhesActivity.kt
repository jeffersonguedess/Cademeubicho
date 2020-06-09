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


//        if (post?.idFirebaseUsu == Sessao.getUser().uidFirebase){
//            /* SE FOR O CARA QUE TA ACESSANDO
//            * "SIMPLESMENTE" BLOQUEIA O CAMPO PARA OS OUTROS CAMPOS
//            * INTERLIGADOS A ELE NAO DESAPARECEREM (EDITAR E DESATIVAR) */
//            textView.setText("")
//            btnWhatsApp.isEnabled = false
//            btnWhatsApp.isClickable = false
//
//            btnChat.isEnabled = false
//            btnChat.isClickable = false
//            btnChat.setVisibility(View.GONE);
//        }
//
//        /* SE FOR UM TELEFONE INVALIDO, OU O USUARIO NAO ESTIVER LOGADO,
//        DESAPARECER COM OS BOTOES*/
//        if ( post?.idFirebaseUsu != Sessao.getUser().uidFirebase
//            ||  FirebaseAuth.getInstance().currentUser == null ){
//            textView.setText("")
//            btnWhatsApp.isEnabled = false
//            btnWhatsApp.isClickable = false
//
//            btnChat.setVisibility(View.GONE);
//        }
//
//        if (post?.celularWhatsApp?.length == 0){
//            textView.setText("")
//            btnWhatsApp.setVisibility(View.GONE);
//        }

//        /** MOSTRA BOTAO DE ATUALIZAR POST E DE INATIVAR POST,
//            * SOMENTE SE USUARIO LOGADO SEJA O  USUARIO QUE FEZ O POST
//         * E O POST ESTEJA COM STATUS ATIVO = 'S*/
//        btnEditaPost.setVisibility(View.GONE);
//        btnDesativaPost.setVisibility(View.GONE);
//        if ( ( post?.idFirebaseUsu == Sessao.getUser().uidFirebase
//            && post?.postAtivo == "S"
//        )){
//            btnEditaPost.setVisibility(View.VISIBLE);
//            btnDesativaPost.setVisibility(View.VISIBLE);
//        }
//


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