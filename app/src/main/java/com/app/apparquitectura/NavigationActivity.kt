package com.app.apparquitectura

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.app.apparquitectura.databinding.ActivityNavigationBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class NavigationActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityNavigationBinding
    private val PICK_IMAGE_REQUEST = 1
    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarNavigation.toolbar)

        binding.appBarNavigation.fab.setOnClickListener { view ->
            Snackbar.make(view, "No tiene novedades", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        auth = FirebaseAuth.getInstance()

        val hView: View = navView.getHeaderView(0)
        val textHeader: TextView = hView.findViewById(R.id.textCabecera)


        val imageChoose: ImageView = hView.findViewById(R.id.imageView)
        imageChoose.setOnClickListener {
            if (checkPermission()) {
                chooseImage()
            } else {
                requestPermission()
            }
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.mainActivity -> {
                    val irInicioFragment = MainActivity.newInstance("param1", "param2")
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_navigation, irInicioFragment).commit()
                    menuItem.setChecked(true)
                }
                R.id.consejos -> {
                    val irConsejosFragment = Consejos()
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_navigation, irConsejosFragment).commit()
                    menuItem.setChecked(true)
                }
                R.id.mapFragment->{
                    val mapsFragment = MapsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_navigation, mapsFragment).commit()
                    menuItem.setChecked(true)
                }
                R.id.solicitudRecoleccionActivity ->{
                    val recoleccionFragment = SolicitudRecoleccionActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_navigation, recoleccionFragment).commit()
                    menuItem.setChecked(true)
                }
                R.id.solicitudDenunciaActivity ->{
                    val denunciaFragment = SolicitudDenunciaActivity()
                    supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_navigation, denunciaFragment).commit()
                    menuItem.setChecked(true)
                }
            }

            // Cierra el drawer después de seleccionar un item
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            val userName = currentUser.displayName
            if (userName != null) {
                textHeader.text = userName.uppercase()
            }
        }

        val navController = findNavController(R.id.nav_host_fragment_content_navigation)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainActivity,R.id.mapsFragment, R.id.solicitudDenunciaActivity, R.id.solicitudRecoleccionActivity,
                R.id.consejos, R.id.programasActivity, R.id.power
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun updateSelectedNavItem(itemId: Int) {
        binding.navView.menu.findItem(itemId).setChecked(true)
    }

    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            val imageView: ImageView = binding.navView.getHeaderView(0).findViewById<ImageView>(R.id.imageView)

            imageView.setImageURI(uri)
        }
    }

    private fun checkPermission(): Boolean {
        Log.d("MyApp", "Checking permission status")
        val result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                chooseImage()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_navigation)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}