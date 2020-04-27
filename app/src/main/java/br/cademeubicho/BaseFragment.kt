package br.cademeubicho

import android.content.Intent
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

abstract class BaseFragment : Fragment() {

    private val fbAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var hasInitCalled = false

    private var authListener: FirebaseAuth.AuthStateListener =
        FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                if (!hasInitCalled) {
                    hasInitCalled = true
                }
            } else {
                activity?.finish()
                val intent = Intent(activity, LoginActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                startActivity(intent)
            }

        }


    override fun onStart() {
        super.onStart()
        fbAuth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        fbAuth.removeAuthStateListener(authListener)
    }
}