

package com.example.bonkgarage.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bonkgarage.Adapter.CarsListAdapter
import com.example.bonkgarage.Brands.BrandsListActivity
import com.example.bonkgarage.CarDataManager
import com.example.bonkgarage.R
import com.example.bonkgarage.BestSeller.BestSellerActivity
import com.example.mcs_lab_assignment.fragments.ARG_PARAM1
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {
    private var param1: String? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CarsListAdapter
    private lateinit var ivTamiya: ImageView
    private lateinit var ivMinigt: ImageView
    private lateinit var ivTarmac: ImageView
    private lateinit var ivKaido: ImageView
    private lateinit var ivBestSeller: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.rvCars)
        ivTamiya = view.findViewById(R.id.ivTamiya)
        ivMinigt = view.findViewById(R.id.ivMinigt)
        ivTarmac = view.findViewById(R.id.ivTarmac)
        ivKaido = view.findViewById(R.id.ivKaido)
        ivBestSeller = view.findViewById(R.id.ivBestSeller)

        // Set layout manager for the RecyclerView (horizontal scrolling)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Create the CarsListAdapter with listener
        adapter = CarsListAdapter { carData ->
        }

        recyclerView.adapter = adapter

        // Set click listeners for brand ImageViews
        ivTamiya.setOnClickListener { startActivity(Intent(context, BrandsListActivity::class.java)) }
        ivMinigt.setOnClickListener { startActivity(Intent(context, BrandsListActivity::class.java)) }
        ivTarmac.setOnClickListener { startActivity(Intent(context, BrandsListActivity::class.java)) }
        ivKaido.setOnClickListener { startActivity(Intent(context, BrandsListActivity::class.java)) }

        // Set click listener for best seller image
        ivBestSeller.setOnClickListener { startActivity(Intent(context, BestSellerActivity::class.java)) }


        return view
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String?) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}
