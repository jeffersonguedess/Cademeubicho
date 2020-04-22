package br.cademeubicho.ui.configuracao

import android.R.attr.button
import android.R.attr.fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.cademeubicho.BaseFragment
import br.cademeubicho.R
import br.cademeubicho.maps.MapsActivity
import com.facebook.FacebookSdk.getApplicationContext
import kotlinx.android.synthetic.main.fragment_configuracao.*


class ConfiguracaoFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_configuracao, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabMaps.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, MapsActivity::class.java)


            startActivity(intent)
        })

    }


}
