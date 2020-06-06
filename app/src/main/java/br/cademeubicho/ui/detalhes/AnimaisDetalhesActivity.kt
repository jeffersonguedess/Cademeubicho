package br.cademeubicho.ui.detalhes

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import br.cademeubicho.model.PostConsulta
import kotlinx.android.synthetic.main.activity_animais_detalhes.*


class AnimaisDetalhesActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POST = "EXTRA_POST"
        const val EXTRA_MEU_BICHO = "EXTRA_MEU_BICHO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animais_detalhes)

        val meuBicho = intent.getBooleanExtra(EXTRA_MEU_BICHO, false)
        if (meuBicho) {
            println("teste")
        }

        val post = intent.getParcelableExtra<PostConsulta>(EXTRA_POST)
        txtViewAnimalPorte.text = post?.descricaoPorte
        textTipo.text = post?.descricaoTipo
        etNomeAnimal.setText(post?.nomeAnimal)
        editTextNumber.setText(post?.idadeAnimal)
        etcorAnimal.setText(post?.corAnimal)
        etracaAnimal.setText(post?.racaAnimal)
        etrecompensa.setText(post?.recompensa)

        if (post?.celularWhatsApp?.length == 0){

            btnWhatsApp.setVisibility(View.GONE);
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

        fabMaps.setOnClickListener{
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
}