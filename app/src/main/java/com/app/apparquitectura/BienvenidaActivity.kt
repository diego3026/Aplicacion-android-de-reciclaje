package com.app.apparquitectura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class BienvenidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)
    }

    fun ingresar(view: View){
        var irLogin = Intent(this,LoginActivity::class.java)
        startActivity(irLogin)
    }

    fun registrarte(view: View){
        var irRegisterActivity = Intent(this,RegisterActivity::class.java)
        startActivity(irRegisterActivity)
    }
}