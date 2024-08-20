package com.app.apparquitectura

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainActivity : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth

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
        val rootView = inflater.inflate(R.layout.activity_main, container, false)
        auth = FirebaseAuth.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            val userName = currentUser.displayName
            val textBienvendio = rootView.findViewById<TextView>(R.id.textView7)
            if (userName != null) {
                textBienvendio.text = userName.uppercase()
            }
        }

        val text23 = rootView.findViewById<TextView>(R.id.textView23)
        text23.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)

        val text15 = rootView.findViewById<TextView>(R.id.textView15)
        text15.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)

        val cardView1 = rootView.findViewById<CardView>(R.id.cardView2)
        cardView1.setOnClickListener { consejo1(it) }

        val cardView3 = rootView.findViewById<CardView>(R.id.cardView)
        cardView3.setOnClickListener { consejo3(it) }

        val contenedor = rootView.findViewById<Button>(R.id.button7)
        contenedor.setOnClickListener { irPunto(it) }

        val denuncias = rootView.findViewById<Button>(R.id.button13)
        denuncias.setOnClickListener { realizar_denuncia(it) }

        val recogida = rootView.findViewById<Button>(R.id.button14)
        recogida.setOnClickListener { realizar_recogida(it) }

        val consejos = rootView.findViewById<TextView>(R.id.textView15)
        consejos.setOnClickListener { irConsejos(it) }

        val programas = rootView.findViewById<TextView>(R.id.textView23)
        programas.setOnClickListener { irProgramas(it) }

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
            MainActivity().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun consejo1(view: View) {
        val nuevoFragmento = primerConsejoActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        (activity as NavigationActivity).updateSelectedNavItem(R.id.consejos)
    }

    @SuppressLint("ResourceType")
    fun consejo3(view: View) {
        val irTercerConsejoFragment = TercerConsejoActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, irTercerConsejoFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        (activity as NavigationActivity).updateSelectedNavItem(R.id.consejos)
    }

    @SuppressLint("ResourceType")
    fun realizar_denuncia(view: View) {
        val realizarDenunciaFragment = SolicitudDenunciaActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, realizarDenunciaFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        (activity as NavigationActivity).updateSelectedNavItem(R.id.solicitudDenunciaActivity)
    }

    @SuppressLint("ResourceType")
    fun realizar_recogida(view: View) {
        val realizarRecogidaFragment = SolicitudRecoleccionActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, realizarRecogidaFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        (activity as NavigationActivity).updateSelectedNavItem(R.id.solicitudRecoleccionActivity)
    }

    @SuppressLint("ResourceType")
    fun irPunto(view: View) {
        val irPuntoDeRecoleccionFragment = MapsFragment()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, irPuntoDeRecoleccionFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        (activity as NavigationActivity).updateSelectedNavItem(R.id.mapsFragment)
    }

    @SuppressLint("ResourceType")
    fun irConsejos(view: View) {
        val irConsejosFragment = Consejos()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, irConsejosFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        (activity as NavigationActivity).updateSelectedNavItem(R.id.consejos)
    }

    @SuppressLint("ResourceType")
    fun irProgramas(view: View) {
        val irProgramasFragment = ProgramasActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, irProgramasFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
        (activity as NavigationActivity).updateSelectedNavItem(R.id.programasActivity)
    }

}
