package br.cademeubicho.ui.cadastroanimal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import br.cademeubicho.maps.MapsActivity
import br.cademeubicho.webservice.Sessao
import br.cademeubicho.webservice.controller.CadastrosController
import br.cademeubicho.webservice.model.PostCadastro
import kotlinx.android.synthetic.main.activity_cadastro_animal.*
import kotlinx.android.synthetic.main.fragment_animais_desaparecidos.*

const val PICK_IMAGE_MULTIPLE = 1

class CadastroAnimalActivity : AppCompatActivity() {

    private val porteAnimal = arrayOf(
        "Pequeno",
        "Médio",
        "Grande"
    )

    private val tipoAnimal = arrayOf(
        "Cachorro",
        "Gato",
        "Pássaro",
        "Roedor",
        "Réptil",
        "Outros"
    )

    private lateinit var imageEncoded: String
    private lateinit var imagesEncodedList: MutableList<String>
    private var galleryAdapter: GalleryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = setContentView(R.layout.activity_cadastro_animal)
        //val user = FirebaseAuth.getInstance().currentUser

        btn.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                intent.action = Intent.ACTION_GET_CONTENT
                startActivityForResult(
                    Intent.createChooser(intent, "Selecionar imagem"),
                    PICK_IMAGE_MULTIPLE
                )

                Toast.makeText(this, "selecione no maximo 3 fotos ", Toast.LENGTH_LONG).show()


        }
        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try {
            //  Quando uma imagem é selecionada
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == Activity.RESULT_OK
                && null != data
            ) {
                //  Obtém a imagem dos dados

                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                imagesEncodedList = ArrayList(3)
                if (data.data != null) {

                    val mImageUri = data.data

                    // Pega o cursor
                    val cursor = mImageUri?.let {
                        contentResolver.query(
                            it,
                            filePathColumn, null, null, null
                        )
                    }
                    // Move para o cursor da primeira linha
                    cursor?.moveToFirst()

                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                    imageEncoded = columnIndex?.let { cursor.getString(it) }.toString()
                    cursor?.close()

                    val mArrayUri = ArrayList<Uri>()
                    mImageUri?.let { mArrayUri.add(it) }
                    galleryAdapter = GalleryAdapter(applicationContext, mArrayUri)
                    gv.adapter = galleryAdapter


                } else {
                    if (data.clipData != null) {
                        val mClipData = data.clipData
                        val mArrayUri = ArrayList<Uri>(3)
                        for (i in 0 until mClipData?.itemCount!!) {

                            val item = mClipData.getItemAt(i)
                            val uri = item?.uri
                            uri?.let { mArrayUri.add(it) }
                            // Pega o cursor
                            val cursor =
                                uri?.let {
                                    contentResolver.query(
                                        it,
                                        filePathColumn,
                                        null,
                                        null,
                                        null
                                    )
                                }
                            // // Move para o cursor da primeira linha !!
                            cursor?.moveToFirst()

                            val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                            imageEncoded = columnIndex?.let { cursor.getString(it) }.toString()
                            imagesEncodedList.add(imageEncoded)
                            cursor?.close()

                            galleryAdapter = GalleryAdapter(applicationContext, mArrayUri)
                            gv.adapter = galleryAdapter


                        }
                        Log.v("LOG_TAG", "Imagens selecionadas" + mArrayUri.size)
                    }
                }
            } else {
                Toast.makeText(
                    this, "Você não escolheu a imagem",
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG)
                .show()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        fabMaps.setOnClickListener {
            var secondActivity = Intent(this, MapsActivity::class.java)
            startActivity(secondActivity)
        }


        btnCadastroAnimais.setOnClickListener{

            val lat = "-18.910680"
            val log = "-50.653200"


//            val post = PostCadastro(
//                Sessao.getUser().uidFirebase,
//                spinner_porte_animal.selectedItem.toString(),
//                spinner_tipo_animal.selectedItem.toString(),
//                etNomeAnimal.toString(), etracaAnimal.toString(),
//                etIdadeAnimal.toString(), etcorAnimal.toString(),
//                etrecompensa.toString(), "",""
//            )
//            val status = CadastrosController().cadastrarPost(post);

            var insere = true
            if (imagesEncodedList.size < 1){
                Toast.makeText(this, "Selecione pelo menos uma foto", Toast.LENGTH_LONG).show()
                insere = false
            }

        }


        alteraSpinnerPorteAnimal()
        alteraSpinnerTipoAnimal()
    }

    private fun alteraSpinnerPorteAnimal() {
        val spinnerPorteAnimal = findViewById<Spinner>(R.id.spinner_porte_animal)

        let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                porteAnimal
            ).also { adapter ->
                // Especifique o layout a ser usado quando a lista de opções aparecer
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerPorteAnimal?.adapter = adapter

            }
        }

    }

    private fun alteraSpinnerTipoAnimal() {
        val spinnerTipoAnimal = findViewById<Spinner>(R.id.spinner_tipo_animal)

        let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                tipoAnimal
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerTipoAnimal?.adapter = adapter

            }
        }

    }
}
