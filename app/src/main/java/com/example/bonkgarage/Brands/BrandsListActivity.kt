package com.example.bonkgarage.Brands

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bonkgarage.Adapter.BrandsListAdapter
import com.example.bonkgarage.CarsDetailsActivity
import com.example.bonkgarage.MenuActivity
import com.example.bonkgarage.R

class BrandsListActivity : AppCompatActivity() {

    private lateinit var rvBrands: RecyclerView
    private lateinit var txtBrands: TextView
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brands_list)

        txtBrands = findViewById(R.id.txtBrands)
        rvBrands = findViewById(R.id.rvBrands)
        backButton = findViewById(R.id.back_button)



        
        val adapter = BrandsListAdapter() { brandName ->

        }

        rvBrands.layoutManager = LinearLayoutManager(this)
        rvBrands.adapter = adapter
        txtBrands.text = "Brands"

        backButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}

