package com.example.saveit.ui.movimientos.lista

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.saveit.R
import com.example.saveit.data.Movimiento
import java.text.SimpleDateFormat
import java.util.*

class ListaMovimientosAdapter: RecyclerView.Adapter<ListaMovimientosAdapter.MyViewHolder>() {
    private var movimientoList = emptyList<Movimiento>()
    private lateinit var context: Context

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaMovimientosAdapter.MyViewHolder {
        context = parent.context

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_movimiento_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = movimientoList[position]

        if (position == 1) {
            holder.itemView.findViewById<LinearLayout>(R.id.layout_movimiento).setBackground(ContextCompat.getDrawable(context, R.drawable.custom_layout_red_style))
        }

        val fecha = formatDate(currentItem.fecha)

        holder.itemView.findViewById<TextView>(R.id.valor_movimiento).text = currentItem.id.toString() + " - " + currentItem.monto.toString() + " " + currentItem.categoria + " - " + fecha
    }

    override fun getItemCount(): Int {
        return movimientoList.size
    }

    fun setData(movimiento: List<Movimiento>) {
        this.movimientoList = movimiento
        notifyDataSetChanged()
    }

    fun formatDate(fecha: Long): String {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(fecha)

            return sdf.format(netDate).toString()
        } catch (e: Exception) {
            return e.toString()
        }
    }
}