package br.cademeubicho.ui.minhaconta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.cademeubicho.BaseFragment
import br.cademeubicho.R
import br.cademeubicho.webservice.Sessao
import br.cademeubicho.webservice.controller.CadastrosController
import br.cademeubicho.webservice.model.Usuario
import kotlinx.android.synthetic.main.fragment_minha_conta.*
import kotlinx.android.synthetic.main.fragment_minha_conta.view.*


class MinhaContaFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_minha_conta, container, false)

        alteraDadosTela(root)

        return root
    }

    private fun alteraDadosTela( view : View) {


        view.tvNome.text =  Sessao.getUser().nomeUsuario
        view.tvEmail.text = Sessao.getUser().emailUsuario
       if (Sessao.getUser().dddCelular.isNotEmpty()) {
           view.num_ddd.setText(Sessao.getUser().dddCelular)
           view.num_telefone.setText(Sessao.getUser().numeroCelular)
        }
        view.result.setText(Sessao.getUser().distanciaFeed.toString())
        view.seekBar.setProgress(Sessao.getUser().distanciaFeed)
    }

    override fun onResume() {
        super.onResume()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                result.text = " $progress KM"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSalva.setOnClickListener {


            val usuario =  Usuario(
                tvNome.text as String,
                num_telefone.getText().toString(),
                num_ddd.getText().toString(),
                tvEmail.text.toString(),
                seekBar.progress,
                Sessao.getUser().uidFirebase )

           val response = CadastrosController().atualizaUsuario(usuario)

            Toast.makeText(activity, response.statusMensagem, Toast.LENGTH_LONG).show()

            if (response.retorno.toLowerCase() == "true"){
                println("recarrega sessao")

                var uid = Sessao.getUser().uidFirebase
                Sessao.loadSessao(uid)
                alteraDadosTela(view )
                //recarrega a sessao com os dados atuais que acabaram de ser cadastrados
            }


        }

    }


}


