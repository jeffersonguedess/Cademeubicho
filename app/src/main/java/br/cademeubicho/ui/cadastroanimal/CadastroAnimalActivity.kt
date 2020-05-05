package br.cademeubicho.ui.cadastroanimal

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import br.cademeubicho.maps.MapsActivity
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_cadastro_animal.*

const val REQUEST_IMAGE_CAPTURE = 100

@Suppress("UNREACHABLE_CODE", "DEPRECATION", "DEPRECATED_IDENTITY_EQUALS")
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

        iv_camera_primera.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)

        }

        iv_camera_segunda.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)

        }

        iv_camera_terceira.setOnClickListener {
            CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this)

        }
        return root

    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var resultPrimeriaImagem = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                iv_camera_primera.setImageURI(resultPrimeriaImagem.uri)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error: Exception = resultPrimeriaImagem.error
            }

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var resultSegundaImagem = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                iv_camera_segunda.setImageURI(resultSegundaImagem.uri)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error: Exception = resultSegundaImagem.error
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var resultTerceiraImagem = CropImage.getActivityResult(data)
            if (resultCode === Activity.RESULT_OK) {
                iv_camera_terceira.setImageURI(resultTerceiraImagem.uri)
            } else if (resultCode === CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error: Exception = resultTerceiraImagem.error
            }
        }

    }

    override fun onResume() {
        super.onResume()
        fabMaps.setOnClickListener(View.OnClickListener {
            val secondActivity = Intent(this, MapsActivity::class.java)
            startActivity(secondActivity)
        })
        alteraSpinnerPorteAnimal(spinner_porte_animal)
        alteraSpinnerTipoAnimal(spinner_tipo_animal)
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
