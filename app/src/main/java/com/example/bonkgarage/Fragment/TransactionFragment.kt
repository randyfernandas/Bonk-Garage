package com.example.bonkgarage.Adapter
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bonkgarage.Model.Transactions.listTransactions
import com.example.bonkgarage.R
import com.example.bonkgarage.TRANSACTION_ADDED

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TransactionFragment : Fragment() {
     val receiver = object : BroadcastReceiver() {
        @SuppressLint("NotifyDataSetChanged")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == TRANSACTION_ADDED) {
                // Transaction added, update adapter
                val adapter = requireView().findViewById<RecyclerView>(R.id.rvTransactionsList)?.adapter as TransactionListAdapter
                adapter.notifyDataSetChanged()
            }
        }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        var view = inflater.inflate(R.layout.fragment_transaction, container, false)
        lateinit var rvTransactionsList: RecyclerView

        rvTransactionsList = view.findViewById(R.id.rvTransactionsList)

        rvTransactionsList.layoutManager = LinearLayoutManager(context)

        rvTransactionsList.adapter = TransactionListAdapter(listTransactions)

            return view
        }
//
    override fun onResume() {
        super.onResume()
        context?.registerReceiver(receiver, IntentFilter(TRANSACTION_ADDED))
    }

    override fun onPause() {
        super.onPause()
        context?.unregisterReceiver(receiver)
    }
}
