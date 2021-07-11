package com.example.saveit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.saveit.databinding.ActivityMainBinding
import com.example.saveit.ui.ahorro.AhorroFragment
import com.example.saveit.ui.main.MainFragment
import com.example.saveit.ui.movimientos.agregar.AgregarMovimientosFragment
import com.example.saveit.ui.movimientos.lista.ListaMovimientosFragment
import com.example.saveit.ui.reportes.ReportesFragment

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener, AhorroFragment.OnFragmentInteractionListener, ListaMovimientosFragment.OnFragmentInteractionListener, AgregarMovimientosFragment.OnFragmentInteractionListener, ReportesFragment.OnFragmentInteractionListener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            // Solo la primera vez que corre el activity
            // Las demás el propio manager restaura todo como estaba
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.fragmentContainer,
                    MainFragment.newInstance(getString(R.string.titulo_principal))
                )
                .commit()
        }

        setupActionBarWithNavController(findNavController(R.id.fragment))
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            //.setCustomAnimations(R.anim.fragment_push_enter, R.anim.fragment_push_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exit)
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}