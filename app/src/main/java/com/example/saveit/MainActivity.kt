package com.example.saveit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.saveit.ui.main.MainFragment
import com.example.saveit.databinding.ActivityMainBinding
import com.example.saveit.ui.ahorro.AhorroFragment
import com.example.saveit.ui.movimientos.agregar.AgregarMovimientosFragment
import com.example.saveit.ui.movimientos.agregar.AgregarUsuarioFragment
import com.example.saveit.ui.movimientos.lista.ListaMovimientosFragment
import com.example.saveit.ui.reportes.ReportesFragment

class MainActivity : AppCompatActivity(), NavegacionInterface {
//MainFragment.OnFragmentInteractionListener, AhorroFragment.OnFragmentInteractionListener, ListaMovimientosFragment.OnFragmentInteractionListener, AgregarMovimientosFragment.OnFragmentInteractionListener, AgregarUsuarioFragment.OnFragmentInteractionListener, ReportesFragment.OnFragmentInteractionListener {
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
    }
    override fun showFragment(fragment: Fragment, addToBackstack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragment)

        if (addToBackstack) {
            transaction.addToBackStack(null)
        }

        transaction.commit()
    }
}