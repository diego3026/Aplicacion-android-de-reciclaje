package com.app.apparquitectura

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ConsultarRecoleccion : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var tableLayout: TableLayout
    private lateinit var firestore: FirebaseFirestore
    private var matrix: MutableList<MutableList<String>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_consultar_recoleccion, container, false)

        firestore = FirebaseFirestore.getInstance()

        tableLayout = view.findViewById(R.id.tableLayoutRecoleccion)
        tableLayout?.removeAllViews()

        var registro = LayoutInflater.from(requireActivity()).inflate(R.layout.table_row,null,false)
        var nombre = registro.findViewById<TextView>(R.id.textNombre)
        var descripcion = registro.findViewById<TextView>(R.id.textDescripcion)
        var estado = registro.findViewById<TextView>(R.id.textEstado)
        var verMas = registro.findViewById<TextView>(R.id.textVerMas)

        nombre.text = "Nombre"
        nombre.setBackgroundColor(Color.BLACK)
        nombre.setTextColor(Color.WHITE)

        descripcion.text = "Descripcion"
        descripcion.setBackgroundColor(Color.BLACK)
        descripcion.setTextColor(Color.WHITE)

        estado.text = "Estado"
        estado.setBackgroundColor(Color.BLACK)
        estado.setTextColor(Color.WHITE)

        verMas.text = "Ver mas"
        verMas.setBackgroundColor(Color.BLACK)
        verMas.setTextColor(Color.WHITE)

        tableLayout.addView(registro)

        obtenerDatosDeFirestore()

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
            ConsultarRecoleccion().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun obtenerDatosDeFirestore(){
        firestore.collection("recolecciones")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    var i = 0
                    var registro = LayoutInflater.from(requireActivity()).inflate(R.layout.table_row,null,false)
                    var nombre = registro.findViewById<TextView>(R.id.textNombre)
                    var descripcion = registro.findViewById<TextView>(R.id.textDescripcion)
                    var estado = registro.findViewById<TextView>(R.id.textEstado)
                    var verMas = registro.findViewById<TextView>(R.id.textVerMas)

                    matrix.add(mutableListOf())
                    matrix.add(mutableListOf())
                    matrix.add(mutableListOf())
                    matrix.add(mutableListOf())

                    for ((campo, valor) in document.data) {
                        val textViewCampo = TextView(requireContext())
                        if(campo == "estado" || campo == "descripcion" || campo == "nombreCompleto") {
                            textViewCampo.text = "$valor"

                            if(campo == "nombreCompleto"){
                                nombre.text = valor.toString()
                            }else if(campo == "estado"){
                                estado.text = valor.toString()
                            }else if(campo == "descripcion"){
                                descripcion.text = valor.toString()
                            }
                        }
                    }
                    verMas.text = "Ver mas"
                    matrix[i++].add(nombre.text.toString())
                    matrix[i++].add(descripcion.text.toString())
                    matrix[i++].add(estado.text.toString())
                    matrix[i++].add(verMas.text.toString())

                    tableLayout.addView(registro)
                }
            }
            .addOnFailureListener { e ->
                // Manejar errores según tus necesidades
            }
    }

}