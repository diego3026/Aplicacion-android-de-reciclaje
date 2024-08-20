package com.app.apparquitectura

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SolicitudRecoleccionActivity : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

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
        val rootView = inflater.inflate(R.layout.activity_solicitud_recoleccion, container, false)


        val formuRecoleccion = rootView.findViewById<Button>(R.id.button10)
        formuRecoleccion.setOnClickListener { solicitudRecoleccion(it) }

        val recolecciones = rootView.findViewById<Button>(R.id.button11)
        recolecciones.setOnClickListener { consultarSolicitudes(it) }

        return rootView
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
            SolicitudRecoleccionActivity().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("ResourceType")
    fun solicitudRecoleccion(view: View) {
        val nuevoFragmento = FormRecoleccion()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @SuppressLint("ResourceType")
    fun consultarSolicitudes(view: View) {
        val nuevoFragmento = ConsultarRecoleccion()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}