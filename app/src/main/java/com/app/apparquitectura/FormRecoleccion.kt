package com.app.apparquitectura

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FormRecoleccion : Fragment() {
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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_form_recoleccion, container, false)
        firestore = FirebaseFirestore.getInstance()

        val nombre = view.findViewById<EditText>(R.id.textView29)
        val direccion = view.findViewById<EditText>(R.id.textView32)
        val barrio = view.findViewById<EditText>(R.id.textView35)
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
            val valNombre = nombre.text.toString()
            val valDireccion = direccion.text.toString()
            val valFecha = fecha.text.toString()
            val valDescripcion = descripcion.text.toString()
            val valBarrio = barrio.text.toString()

            if (valNombre != "" && valFecha != "") {
                val datos = hashMapOf(
                    "nombreCompleto" to valNombre,
                    "descripcion" to valDescripcion,
                    "direccion" to  valDireccion,
                    "fecha" to valFecha,
                    "barrio" to valBarrio,
                    "estado" to "pendiente"
                )

                firestore.collection("recolecciones")
                    .document()
                    .set(datos)
                    .addOnSuccessListener {
                        nombre.setText("")
                        barrio.setText("")
                        direccion.setText("")
                        fecha.setText("")
                        descripcion.setText("")
                        Toast.makeText(activity, "GUARDADO", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(activity, "ERROR AL GUARDAR", Toast.LENGTH_SHORT).show()
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
            FormRecoleccion().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}