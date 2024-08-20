package com.app.apparquitectura

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Consejos : Fragment() {
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
        val rootView = inflater.inflate(R.layout.activity_consejos, container, false)

        val cardView1 = rootView.findViewById<CardView>(R.id.cardView2)
        cardView1.setOnClickListener { consejo1(it) }

        val cardView2 = rootView.findViewById<CardView>(R.id.cardView3)
        cardView2.setOnClickListener { consejo2(it) }

        val cardView3 = rootView.findViewById<CardView>(R.id.cardView5)
        cardView3.setOnClickListener { consejo3(it) }

        val cardView4 = rootView.findViewById<CardView>(R.id.cardView6)
        cardView4.setOnClickListener { consejo4(it) }

        val cardView5 = rootView.findViewById<CardView>(R.id.cardView7)
        cardView5.setOnClickListener { consejo5(it) }

        val cardView6 = rootView.findViewById<CardView>(R.id.cardView8)
        cardView6.setOnClickListener { consejo6(it) }

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
            Consejos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    @SuppressLint("ResourceType")
    fun consejo1(view: View) {
        val nuevoFragmento = primerConsejoActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @SuppressLint("ResourceType")
    fun consejo2(view: View) {
        val nuevoFragmento = SegundoConsejoActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @SuppressLint("ResourceType")
    fun consejo3(view: View) {
        val nuevoFragmento = TercerConsejoActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @SuppressLint("ResourceType")
    fun consejo4(view: View) {
        val nuevoFragmento = CuartoConsejoActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @SuppressLint("ResourceType")
    fun consejo5(view: View) {
        val nuevoFragmento =QuintoConsejoActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    @SuppressLint("ResourceType")
    fun consejo6(view: View) {
        val nuevoFragmento = SextoConsejoActivity()
        val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_navigation, nuevoFragmento)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

}