package com.example.saveit.ui.ahorro

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.saveit.R

class AhorroAdapter (val ahorros:List<Ahorro>): RecyclerView.Adapter<AhorroAdapter.AhorroHolder>(){
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AhorroHolder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        return AhorroHolder(layoutInflater.inflate(R.layout.item_ahorro,parent,false))
    }

    override fun onBindViewHolder(holder: AhorroHolder, position: Int) {
        holder.render(ahorros[position],context)
    }

    override fun getItemCount(): Int = ahorros.size

    class AhorroHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMonto: TextView
        val tvFecha: TextView


        init {
            // Define click listener for the ViewHolder's View.
            tvMonto = view.findViewById(R.id.tvMonto)
            tvFecha = view.findViewById(R.id.tvFecha)
        }
        fun render(ahorro: Ahorro, context: Context){
            tvMonto.text = ahorro.monto.toString()
            tvFecha.text = ahorro.fecha.toString()
            if(!ahorro.ingreso){
                itemView.findViewById<LinearLayout>(R.id.itemAhorro).setBackground(
                    ContextCompat.getDrawable(context, R.drawable.custom_layout_red_style))

            }

        }
    }
}