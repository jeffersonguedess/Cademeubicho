package br.cademeubicho.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PostConsulta (
    var idUsuario: Int?,
    var idFirebaseUsu : String?,
    var nomeAnimal : String?,
    var descricaoTipo : String?,
    var descricaoPorte : String?,
    var racaAnimal : String?,
    var corAnimal : String?,
    var recompensa : String?,
    var longitude : String?,
    var latitude : String?,
    var postAtivo : String?,
    var horaCadastro : String?,
    var nomeUsuario : String?,
    var celularWhatsApp : String?,
    var idFacebook : String?,
    var distanciaKM : String?,
    var idadeAnimal : String?,
    var imagens : List<String>?
    ) : Parcelable