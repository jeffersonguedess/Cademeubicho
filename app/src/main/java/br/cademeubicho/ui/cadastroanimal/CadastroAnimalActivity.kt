package br.cademeubicho.ui.cadastroanimal

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.cademeubicho.R
import br.cademeubicho.maps.MapsActivity
import br.cademeubicho.model.PostCadastro
import br.cademeubicho.model.Sessao
import br.cademeubicho.model.Status
import br.cademeubicho.webservice.controller.CadastrosController
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_animais_detalhes.*
import kotlinx.android.synthetic.main.activity_cadastro_animal.*
import kotlinx.android.synthetic.main.activity_cadastro_animal.editTextNumber
import kotlinx.android.synthetic.main.activity_cadastro_animal.etNomeAnimal
import kotlinx.android.synthetic.main.activity_cadastro_animal.etcorAnimal
import kotlinx.android.synthetic.main.activity_cadastro_animal.etracaAnimal
import kotlinx.android.synthetic.main.activity_cadastro_animal.etrecompensa
import kotlinx.android.synthetic.main.activity_cadastro_animal.gv
import java.util.*
import kotlin.collections.ArrayList


const val PICK_IMAGE_MULTIPLE = 1000
const val PICK_LTG_LOG = 350
private const val SIZE_IN_MB_TO_COMPRESS = 3.7

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
    private lateinit var minhasImagens : ArrayList<Uri>


    var latitude = ""
    var longitude = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = setContentView(R.layout.activity_cadastro_animal)

        btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            minhasImagens = ArrayList<Uri>()
            imagesEncodedList = ArrayList()
            imagesEncodedList.clear()
            minhasImagens.clear()


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
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATE_ADDED)
                imagesEncodedList = ArrayList(3)
                if (data.data != null) {
                    minhasImagens.add(data.data!!)

                    val mImageUri = data.data

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
                            minhasImagens.add(item.uri)


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
                    } else {
                        Toast.makeText(
                            this, "Você não escolheu a imagem",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Algo deu errado", Toast.LENGTH_LONG)
                .show()
        }

        if (requestCode == PICK_LTG_LOG && resultCode == Activity.RESULT_OK
            && null != data
        ) {
            //  Obtém a localização
            latitude = data.extras?.get("latitude").toString()
            longitude = data.extras?.get("longitude").toString()

        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    lateinit var storageReference:StorageReference

    override fun onResume() {
        super.onResume()
        fabMaps.setOnClickListener {
            val secondActivity = Intent(this, MapsActivity::class.java)
            startActivityForResult(secondActivity, PICK_LTG_LOG)
        }
        alteraSpinnerTipoAnimal()
        alteraSpinnerPorteAnimal()


        btnDesativaPost.setOnClickListener {
            val status = CadastrosController().desativaPost(Sessao.getUser().uidFirebase);
            Toast.makeText(this, status.statusMensagem, Toast.LENGTH_LONG).show()
            if (status.retorno.toLowerCase() == "true"){
                btnEditaPost.setVisibility(View.GONE);
                btnDesativaPost.setVisibility(View.GONE);
            }
        }

        btnCadastroAnimais.setOnClickListener {
            alteraCadastra("CADASTRAR")
        }


        btnEditaPost.setOnClickListener {
            alteraCadastra("EDITAR")
        }

    } // END CLASS



    private fun alteraCadastra(tipoInteracao : String){
        var linksImagens : String = ""
        val storage = FirebaseStorage.getInstance("gs://cade-meu-bicho.appspot.com")
        var controle = 0
        var cadastro = true

        if (minhasImagens.size-1 <= 0) {
            Toast.makeText(this, "Selecione pelo menos uma foto", Toast.LENGTH_LONG).show()
            cadastro = false
        }else if ( etNomeAnimal.toString().length == 0 ||
            etracaAnimal.toString().length == 0 ||
            etcorAnimal.toString().length == 0
        ) {
            Toast.makeText(this, "Insira todos os campos!", Toast.LENGTH_LONG).show()
            cadastro = false
        } else if (longitude == "" || latitude == ""){
            Toast.makeText(this, "Escolha uma localização!", Toast.LENGTH_LONG).show()
            cadastro = false
        }
        if (cadastro) {
            for (i in 0 until minhasImagens.size) {
                val url = UUID.randomUUID().toString()
                storageReference = storage.getReference(url)
                val uploadTask = storageReference.putFile(minhasImagens.get(i))
                val task = uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {}
                    storageReference.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        val url = downloadUri!!.toString()
                        linksImagens += url + "***ROGER_LIMA_GAMBIARRA***"
                        if (controle == minhasImagens.size-1) {

                            //FAZER O POST QUANDO TODAS AS FOTOS SUBIREM PARA O GOOGLE CLOUD
                            //VARIAVEL CONTROLE == TAMANHO DO ARRAY DE FOTOS
                            post(linksImagens, tipoInteracao)

                        }
                        controle++;
                    } // imagem inserida com sucesso
                }
            }  // ENF FOR
        }
    }

    private fun post(imagens: String, tipoAlteracao : String) {
        val post = PostCadastro(
            Sessao.getUser().uidFirebase,
            spinner_porte_animal.selectedItem.toString(),
            spinner_tipo_animal.selectedItem.toString(),
            etNomeAnimal.getText().toString(), etracaAnimal.getText().toString(),
            editTextNumber.getText().toString(), etcorAnimal.getText().toString(),
            etrecompensa.getText().toString(), longitude, latitude, imagens
        )
        val status : Status

        if (tipoAlteracao == "CADASTRAR"){
            status = CadastrosController().cadastrarPost(post);
        }else{
            status = CadastrosController().atualizarPost(post);
        }
        Toast.makeText(this, status.statusMensagem, Toast.LENGTH_LONG).show()

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
