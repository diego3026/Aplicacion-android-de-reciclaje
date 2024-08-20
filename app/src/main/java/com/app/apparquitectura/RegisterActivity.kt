package com.app.apparquitectura

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
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
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 123
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var btnSignInWithGoogle: View
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        btnSignInWithGoogle = findViewById(R.id.btnSignInWithGoogle)
        auth = FirebaseAuth.getInstance()

        configureGoogleSignIn()
    }

    fun register(view: View) {
        val name = findViewById<EditText>(R.id.campo_texto2).text.toString().trim()
        val email = findViewById<EditText>(R.id.campo_texto5).text.toString().trim()
        val username = findViewById<EditText>(R.id.campo_texto4).text.toString().trim()
        val password = findViewById<EditText>(R.id.campo_texto6).text.toString().trim()
        val checkBoxTerms = findViewById<CheckBox>(R.id.checkBox)

        if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || !checkBoxTerms.isChecked) {
            Toast.makeText(this, "Complete todos los campos y acepte los términos y condiciones", Toast.LENGTH_SHORT).show()
            return
        }

        // Registrar usuario en Firebase
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registro exitoso

                    // Actualizar el perfil del usuario con el nombre
                    val user = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileUpdateTask ->
                            if (profileUpdateTask.isSuccessful) {
                                // Perfil actualizado con éxito
                                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, NavigationActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Error al actualizar el perfil
                                Toast.makeText(this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Si el registro falla, muestra un mensaje al usuario.
                    Log.e("FirebaseAuth", "Error en el registro", task.exception)
                    Toast.makeText(baseContext, "Error en el registro. Inténtalo nuevamente.",
                        Toast.LENGTH_SHORT).show()
                }
            }
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

    fun login(view: View){
        var irLoginActivity = Intent(this,LoginActivity::class.java)
        startActivity(irLoginActivity)
    }
}