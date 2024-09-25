package com.example.bonkgarage.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bonkgarage.MenuActivity
import com.example.bonkgarage.Model.Transaction
import com.example.bonkgarage.Model.Transactions.listTransactions
import com.example.bonkgarage.R
import com.google.firebase.firestore.FirebaseFirestore


class TransactionListAdapter(var dataset: ArrayList<Transaction>) : RecyclerView.Adapter<TransactionListAdapter.Holder>(){
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var transactions: Transaction
        lateinit var db: FirebaseFirestore

        //        bind view
        var imgCarBuy : ImageView = itemView.findViewById(R.id.imgCarBuy)
        var txtCarBuy : TextView = itemView.findViewById(R.id.txtCarBuy)
        var txtCarBuyQty: TextView = itemView.findViewById(R.id.txtCarBuyQty)
        var txtCarBuyPrice: TextView = itemView.findViewById(R.id.txtCarBuyPrice)

        fun bind(transaction: Transaction)
        {
            this.transactions = transaction


            val resourceId = itemView.context.resources.getIdentifier(transactions.carImg.toString(), "drawable", itemView.context.packageName)
            imgCarBuy.setImageResource(resourceId)
            txtCarBuy.text = if (transaction.carName.length > 20) {
                transaction.carName.substring(0, 20) + "..."
            } else {
                transaction.carName
            }
            txtCarBuyQty.text = transactions.transactionQty.toString()
            txtCarBuyPrice.text = transactions.transactionPrice.toString()



            val updateButton = itemView.findViewById<Button>(R.id.UpdateCarBtn)
            updateButton.setOnClickListener {
                navigateToCarDetails(transaction.carName, transaction.transactionQty)
            }

            val deleteButton = itemView.findViewById<Button>(R.id.DeleteCarBtn)
            deleteButton.setOnClickListener {
                removeTransaction(transaction)

            }

        }
        fun navigateToCarDetails(carName: String, transactionQty: Int) {
            val intent = Intent(itemView.context, MenuActivity::class.java)
            itemView.context.startActivity(intent)
        }

        fun removeTransaction(transaction: Transaction) {
            db = FirebaseFirestore.getInstance()
            Log.d("docId", "docId :  ${transaction.documentId}")
            db.collection("transactions").document(transaction.documentId).delete()
            listTransactions.remove(transaction)
            val intent = Intent(itemView.context, MenuActivity::class.java)
            itemView.context.startActivity(intent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_transaction_cars,parent,false)
        return Holder(view)
    }

    override fun getItemCount(): Int {
        return listTransactions.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        var transactions = dataset.get(position)
        holder.bind(transactions)
    }
}

