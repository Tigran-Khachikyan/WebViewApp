package com.example.webviewapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_1.setOnClickListener {
            openWebPage(it)
        }

        btn_2.setOnClickListener {
            openWebPage(it)
        }
    }

    private fun openWebPage(view: View) {

        val arg = when (view.id) {
            R.id.btn_1 -> ACCOUNT_1
            R.id.btn_2 -> ACCOUNT_2
            else -> null
        }
        val navController: NavController = view.findNavController()
        arg?.let {
            try {
                val action = HomeFragmentDirections.actionOpenWebPage(it)
                navController.navigate(action)
            } catch (ex: Exception) {
                Log.d(LOG, "EXCEPTON: ${ex.message}")
            }
        }
    }

}
