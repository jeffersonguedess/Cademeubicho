package br.cademeubicho.ui.cadastroanimal

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import br.cademeubicho.model.Sessao
import br.cademeubicho.webservice.controller.CadastrosController
import br.cademeubicho.model.PostCadastro
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_cadastro_animal.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


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

    var latitude = ""
    var longitude = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = setContentView(R.layout.activity_cadastro_animal)

        btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)

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

    override fun onResume() {
        super.onResume()
        fabMaps.setOnClickListener {
            val secondActivity = Intent(this, MapsActivity::class.java)
            startActivityForResult(secondActivity, PICK_LTG_LOG)
        }
        alteraSpinnerTipoAnimal()
        alteraSpinnerPorteAnimal()
        btnCadastroAnimais.setOnClickListener {

            val imagens = ArrayList<String>()

            var ajusteTecnico = "";
            for (i in imagens){
                ajusteTecnico += i + "***ROGER_LIMA_GAMBIARRA***"
            }

            if (!this::imagesEncodedList.isInitialized) {
                imagesEncodedList = ArrayList()
            }
            if (imagesEncodedList.size < 0) {
                Toast.makeText(this, "Selecione pelo menos uma foto", Toast.LENGTH_LONG).show()
            } else {
                if (
                    etNomeAnimal.toString().length == 0 ||
                    etracaAnimal.toString().length == 0 ||
                    etcorAnimal.toString().length == 0) {
                        Toast.makeText(this, "Insira todos os campos!", Toast.LENGTH_LONG).show()
                    }else if (longitude == "" || latitude == ""){
                        Toast.makeText(this, "Escolha uma localização!", Toast.LENGTH_LONG).show()

                    }else {
                    val post = PostCadastro(
                        Sessao.getUser().uidFirebase,
                        spinner_porte_animal.selectedItem.toString(),
                        spinner_tipo_animal.selectedItem.toString(),
                        etNomeAnimal.getText().toString(), etracaAnimal.getText().toString(),
                        editTextNumber.getText().toString(), etcorAnimal.getText().toString(),
                        etrecompensa.getText().toString(), longitude, latitude, ajusteTecnico
                    )
                    val status = CadastrosController().cadastrarPost(post);

                    println(status.statusMensagem)
                    if (status.retorno == "true") {
                        Toast.makeText(this, "Post cadastrado com sucesso", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Toast.makeText(this, status.statusMensagem, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

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

    /**
     * Método que verifica se o arquivo esta dentro do tamanho necessario para o envio, caso nao esteja
     * Ele diminui o arquivo até ficar no tamannho permitido
     * Com o arquivo do tamanho necessário ele é convertido em base64 e devolvido
     */
    fun convertImageFileToBase64(file: File, mContext: Context): Single<String> {
        return Single.create {
            if (file.exists() && file.length() > 0) {
                try {
                    val fileWithAllowedSize = generateFileWithAllowedSize(file, mContext)
                    val buffer = ByteArray(fileWithAllowedSize.length().toInt() + 100)
                    val length = FileInputStream(fileWithAllowedSize).read(buffer)
                    it.onSuccess(
                        android.util.Base64.encodeToString(
                            buffer, 0, length,
                            android.util.Base64.DEFAULT
                        )
                    )
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    it.onError(Exception(ex.message))
                }
            } else {
                it.onError(Exception())
            }
        }
    }

    /**
     * Método que verifica se a foto esta dentro do tamanho permitido pela requisição, enquanto nao estiver,
     * ele chama o método para diminuir a qualidade da foto gradativamente, até ficar do tamanho ideal
     */
    private fun generateFileWithAllowedSize(file: File, mContext: Context): File {
        var compressedAnImage: Boolean
        var finalFile = file

        do {
            if (getSizeInMbFromFile(finalFile) > SIZE_IN_MB_TO_COMPRESS) {
                compressedAnImage = true
                finalFile = compressFile(file, mContext)
            } else {
                compressedAnImage = false
            }
        } while (compressedAnImage)

        return finalFile
    }

    /**
     * Método alterado para diminuir a qualidade da foto em 10%, sem alterar o seu tamanho
     * Original: https://coderwall.com/p/wzinww/resize-an-image-in-android-to-a-given-width
     */
    private fun compressFile(file: File, mContext: Context): File {
        // we'll start with the original picture already open to a file
        val b = BitmapFactory.decodeFile(file.absolutePath)
        // original measurements
        val origWidth = b.width
        val origHeight = b.height

        // val destWidth = 600//or the width you need

        // we create an scaled bitmap so it reduces the image, not just trim it
        val b2 = Bitmap.createScaledBitmap(b, origWidth, origHeight, false)
        val outStream = ByteArrayOutputStream()
        // compress to the format you want, JPEG, PNG...
        // 70 is the 0-100 quality percentage
        b2.compress(Bitmap.CompressFormat.JPEG, 90, outStream)
        // generate a new name
        val lastName = file.name.toString().replace(".jpg", "")
        val newName = lastName + "2.jpg"
        // we save the file, at least until we have made use of it
        val finalFile = File(
            mContext.filesDir,
            newName
        )
        // if it is of the allowed size saves a new file
        if (getSizeInMbFromFile(finalFile) <= SIZE_IN_MB_TO_COMPRESS) {
            finalFile.createNewFile()
            //write the bytes in file
            val fo = FileOutputStream(finalFile)
            fo.write(outStream.toByteArray())
            // remember close de FileOutput
            fo.close()
        }
        return finalFile

    }

    /**
     * Método capas de pegar o tamanho do arquivo em mega byte, somando o tamanho aproximado da request (1mb)
     */
    private fun getSizeInMbFromFile(file: File): Double {
        // Get length of file in bytes
        val fileSizeInBytes = file.length()
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        val fileSizeInKB: Double = (fileSizeInBytes / 1024).toDouble()
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        var fileSizeInMB: Double = fileSizeInKB / 1024
        val approximateSizeOfRequest = 1
        fileSizeInMB += approximateSizeOfRequest
        return fileSizeInMB
    }

}
