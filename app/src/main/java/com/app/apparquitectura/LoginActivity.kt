package com.app.apparquitectura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var btnSignInWithGoogle: View
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnSignInWithGoogle = findViewById(R.id.btnSignInWithGoogle)
        auth = FirebaseAuth.getInstance()
        configureGoogleSignIn()
    }

    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    // Triggered when the Google Sign-In button is clicked
    fun signInWithGoogle(view: View) {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Handle the result of Google Sign-In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    // Handle the Google Sign-In result
    private fun handleGoogleSignInResult(completedTask: com.google.android.gms.tasks.Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            account?.idToken?.let { firebaseAuthWithGoogle(it) }
        } catch (e: ApiException) {
            Log.w("GoogleSignIn", "signInResult:failed code=" + e.statusCode)
        }
    }

    // Authenticate with Firebase using the obtained Google ID token
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = FirebaseAuth.getInstance().currentUser
                    // Call the function to handle successful login
                    ingresoGoogle()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("FirebaseAuth", "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun ingresoGoogle() {
        val irApp = Intent(this,NavigationActivity::class.java)
        startActivity(irApp)
    }

    fun login(view: View) {
        val email = findViewById<EditText>(R.id.campo_texto).text.toString().trim()
        val password = findViewById<EditText>(R.id.campo_texto3).text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Ingrese su email y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        // Iniciar sesión con Firebase Auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    Log.d("FirebaseAuth", "signInWithEmail:success")
                    val user = auth.currentUser
                    ingresoApp()
                } else {
                    // Si el inicio de sesión falla, mostrar un mensaje al usuario.
                    Log.w("FirebaseAuth", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Error en el inicio de sesión. Verifique sus credenciales.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun forgotPassword(view: View) {
        val emailEditText = findViewById<EditText>(R.id.campo_texto)
        val email = emailEditText.text.toString().trim()

        if (email.isEmpty()) {
            Toast.makeText(this, "Ingresa tu dirección de correo electrónico", Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("ForgotPassword", "Email enviado para restablecer la contraseña.")
                    Toast.makeText(this, "Se ha enviado un correo para restablecer la contraseña.", Toast.LENGTH_SHORT).show()
                } else {
                    Log.w("ForgotPassword", "Error al enviar el correo de restablecimiento de contraseña.", task.exception)
                    Toast.makeText(this, "Error al enviar el correo. Verifica la dirección de correo electrónico.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun registrarte(view: View) {
        val irRegisterActivity = Intent(this, RegisterActivity::class.java)
        startActivity(irRegisterActivity)
    }

    private fun ingresoApp() {
        val irApp = Intent(this, NavigationActivity::class.java)
        startActivity(irApp)
        finish() // Cierra la actividad actual para evitar que el usuario regrese al inicio de sesión presionando el botón "Atrás"
    }
}