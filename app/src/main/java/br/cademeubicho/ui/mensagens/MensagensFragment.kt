package br.cademeubicho.ui.mensagens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.cademeubicho.BaseFragment
import br.cademeubicho.R

class MensagensFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mensagens, container, false)

    }

}
