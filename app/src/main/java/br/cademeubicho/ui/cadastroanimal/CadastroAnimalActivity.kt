package br.cademeubicho.ui.cadastroanimal

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import br.cademeubicho.maps.MapsActivity
import kotlinx.android.synthetic.main.activity_cadastro_animal.*


class CadastroAnimalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_animal)

    }

    override fun onResume() {
        super.onResume()
        fabMaps.setOnClickListener(View.OnClickListener {
            val secondActivity = Intent(this, MapsActivity::class.java)
            startActivity(secondActivity)

        })


    }


}
