package br.cademeubicho.ui.tutorial

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.viewpager.widget.ViewPager
import br.cademeubicho.R
import br.cademeubicho.ui.maps.MapsActivity
import com.heinrichreimersoftware.materialintro.app.IntroActivity
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide

class TutorialActivity : IntroActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isButtonBackVisible = false
        isFullscreen = true

        addSlide(
            SimpleSlide.Builder()
                .title(R.string.new_user_title)
                .description(R.string.new_user_description)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorAccent)
                .scrollable(false)
                .build()
        )

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            addSlide(
                SimpleSlide.Builder()
                    .title(R.string.permission_title)
                    .description(R.string.permission_description)
                    .background(R.color.colorAccent)
                    .backgroundDark(R.color.colorPrimary)
                    .scrollable(false)
                    .buttonCtaLabel(R.string.grant_permission)
                    .buttonCtaClickListener { verifyStoragePermissions(this) }
                    .build()
            )
            addOnNavigationBlockedListener { _, _ ->
                Toast.makeText(this, R.string.grant_permission_toast, Toast.LENGTH_LONG).show()
            }
        }
        addSlide(
            SimpleSlide.Builder()
                .title(R.string.finish_slide_title)
                .description(R.string.finish_slide_description)
                .background(R.color.colorPrimary)
                .backgroundDark(R.color.colorAccent)
                .scrollable(false)
                .build()
        )
        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                if (i > 0 && i != slides.size - 1) {
                    setNavigation(forward = false, backward = true)
                } else {
                    setNavigation(forward = true, backward = false)
                }
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })
    }

    override fun onBackPressed() {
        // não chame o super desse método
    }

    private fun setNavigation(forward: Boolean, backward: Boolean) {
        setNavigationPolicy(object : NavigationPolicy {
            override fun canGoForward(i: Int): Boolean {
                return forward
            }

            override fun canGoBackward(i: Int): Boolean {
                return backward
            }
        })
    }

    private fun verifyStoragePermissions(activity: Activity?) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // We don't have permission so prompt the user
            activity?.let {

                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                val alertDialog: AlertDialog = AlertDialog.Builder(this)
                    .setPositiveButton(R.string.dialog_permission_button) { _, _ ->
                        verifyStoragePermissions(this)
                    }.create()
                alertDialog.setTitle(R.string.dialog_permission_title)
                alertDialog.setMessage(getString(R.string.dialog_permission_description))
                alertDialog.show()
            } else {
                setNavigation(forward = true, backward = false)
                nextSlide()
            }
        }
    }

}