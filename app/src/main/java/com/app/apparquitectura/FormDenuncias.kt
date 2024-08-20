package com.app.apparquitectura

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FormDenuncias : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_form_denuncias, container, false)

        firestore = FirebaseFirestore.getInstance()

        val contenedorLleno = view.findViewById<CheckBox>(R.id.checkBox3)
        val puntoRecoleccion = view.findViewById<CheckBox>(R.id.checkBox4)
        val nombre = view.findViewById<EditText>(R.id.textView29)
        val direccion = view.findViewById<EditText>(R.id.textView32)
        val fecha = view.findViewById<EditText>(R.id.editTextDate)
        val descripcion = view.findViewById<EditText>(R.id.textView37)
        val buttonGuardar = view.findViewById<Button>(R.id.button16)
        val buttonCancelar = view.findViewById<Button>(R.id.button17)

        buttonCancelar.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Configurar el OnClickListener del botón
        buttonGuardar.setOnClickListener {
            // Obtener los valores de los EditTexts
            val valContenedorLleno = contenedorLleno.isChecked
            val valPuntoRecoleccion = puntoRecoleccion.isChecked
            val valNombre = nombre.text.toString()
            val valDireccion = direccion.text.toString()
            val valFecha = fecha.text.toString()
            val valDescripcion = descripcion.text.toString()


            if (valNombre.isNotEmpty() && valFecha.isNotEmpty()) {
                val datos = hashMapOf(
                    "nombreCompleto" to valNombre,
                    "descripcion" to valDescripcion,
                    "contenedorLleno" to valContenedorLleno,
                    "puntoRecoleccion" to valPuntoRecoleccion,
                    "direccion" to  valDireccion,
                    "fecha" to valFecha,
                    "estado" to "pendiente"
                )

                firestore.collection("denuncias")
                    .document()
                    .set(datos)
                    .addOnSuccessListener {
                        contenedorLleno.isChecked = false
                        puntoRecoleccion.isChecked = false
                        nombre.setText("")
                        direccion.setText("")
                        fecha.setText("")
                        descripcion.setText("")
                        Toast.makeText(activity, "Guardado", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(activity, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
            }

        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FormDenuncias().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}