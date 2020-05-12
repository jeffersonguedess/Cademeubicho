package br.cademeubicho.ui.minhaconta

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.cademeubicho.BaseFragment
import br.cademeubicho.R
import br.cademeubicho.webservice.controller.ConsultasController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_minha_conta.*
import kotlinx.android.synthetic.main.fragment_minha_conta.view.*


class MinhaContaFragment : BaseFragment() {

    val estados = arrayOf(
        "AC",
        "AL",
        "AM",
        "AP",
        "BA",
        "CE",
        "DF",
        "ES",
        "GO",
        "MA",
        "MG",
        "MS",
        "MT",
        "PA",
        "PB",
        "PE",
        "PI",
        "PR",
        "RJ",
        "RN",
        "RO",
        "RR",
        "RS",
        "SC",
        "SE",
        "SP",
        "TO"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_minha_conta, container, false)
        val user = FirebaseAuth.getInstance().currentUser

        root.tvNome.text = user?.displayName.toString()
        root.tvEmail.text = user?.email.toString()

        alteraSpinnerUF(root)
        return root
    }

    override fun onResume() {
        super.onResume()
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                result.text = "" + progress + "KM"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun alteraSpinnerUF(root: View?) {
        val spinner_uf = root?.findViewById<Spinner>(R.id.spinner_estado)

        context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                estados
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinner_uf?.adapter = adapter

            }
        }
        spinner_uf?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context!!, R.string.error_localidades, Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val uf = parent?.getItemAtPosition(position).toString()
                alteraSpinnerCidades(uf, root)
            }

        }
    }

    private fun alteraSpinnerCidades(uf: String, root: View?) {
        val spinnerCidade = root?.findViewById<Spinner>(R.id.spinner_cidades)

        val arrayListCidades = ConsultasController().localidadesServices(uf)
        spinnerCidade?.adapter =
            ArrayAdapter(
                this.requireContext(),
                R.layout.support_simple_spinner_dropdown_item,
                arrayListCidades
            )


        spinnerCidade?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val cidades = parent?.getItemAtPosition(position).toString()
                alteraSpinnerCidades(cidades, root)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(context!!, R.string.error_localidades, Toast.LENGTH_LONG).show()
            }
        }
    }


}
