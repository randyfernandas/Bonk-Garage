package com.example.bonkgarage

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.bonkgarage.Adapter.TransactionFragment
import com.example.bonkgarage.Fragment.HomeFragment
import com.example.bonkgarage.Fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //set fragments
        val homeFragment = HomeFragment()
        val transactionFragment = TransactionFragment()
        val profileFragment = ProfileFragment()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        makeCurrentFragment(homeFragment)
        //bottom navbar
        bottomNavigationView.setOnItemReselectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.baseline_person_24 -> {
                    val profileFragment = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_wrapper, profileFragment).commit()
                    true
                }
                R.id.baseline_home_24 -> {
                    val homeFragment = HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.fl_wrapper, homeFragment)
                        .commit()
                    true
                }
                R.id.baseline_list_alt_24 -> {
                    val transactionFragment = TransactionFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fl_wrapper, transactionFragment).commit()
                    true
                }
                else -> false
            }
        }
    }

    //set default fragments to show
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}