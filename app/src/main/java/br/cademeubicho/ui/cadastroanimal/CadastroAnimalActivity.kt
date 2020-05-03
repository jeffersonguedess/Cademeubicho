package br.cademeubicho.ui.cadastroanimal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import br.cademeubicho.maps.MapsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_cadastro_animal.*

const val REQUEST_IMAGE_CAPTURE = 100

@Suppress("UNREACHABLE_CODE", "DEPRECATION")
class CadastroAnimalActivity : AppCompatActivity() {
    val porteAnimal = arrayOf(
        "Pequeno",
        "Médio",
        "Grande"
    )

    val tipoAnimal = arrayOf(
        "Cachorro",
        "Gato",
        "Pássaro",
        "Roedor",
        "Réptil"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = setContentView(R.layout.activity_cadastro_animal)
        val user = FirebaseAuth.getInstance().currentUser

        alteraSpinnerPorteAnimal(spinner_porte_animal)
        alteraSpinnerTipoAnimal(spinner_tipo_animal)

        return root
        fotoAnimaisButton.callOnClick()

    }

    override fun onResume() {
        super.onResume()
        fabMaps.setOnClickListener(View.OnClickListener {
            val secondActivity = Intent(this, MapsActivity::class.java)
            startActivity(secondActivity)

        })


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun alteraSpinnerPorteAnimal(root: View?) {
        val spinner_porte_animal = findViewById<Spinner>(R.id.spinner_porte_animal)

        let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                porteAnimal
            ).also { adapter ->
                // Especifique o layout a ser usado quando a lista de opções aparecer
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner_porte_animal?.adapter = adapter

            }
        }

    }

    private fun alteraSpinnerTipoAnimal(root: View?) {
        val spinner_tipo_animal = findViewById<Spinner>(R.id.spinner_tipo_animal)

        let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                tipoAnimal
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner_tipo_animal?.adapter = adapter

            }
        }

    }


}
