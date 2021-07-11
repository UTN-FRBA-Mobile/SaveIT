package com.example.saveit.ui.movimientos.lista

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.saveit.R
import com.example.saveit.data.CategoriasGasto
import com.example.saveit.data.CategoriasIngreso
import com.example.saveit.data.Moneda
import com.example.saveit.model.Movimiento
import kotlinx.android.synthetic.main.custom_movimiento_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class ListaMovimientosAdapter: RecyclerView.Adapter<ListaMovimientosAdapter.MyViewHolder>() {
    private var movimientoList = emptyList<Movimiento>()
    private lateinit var context: Context

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context

        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.custom_movimiento_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = movimientoList[position]

        if (currentItem.tipoMovimiento == 0) {
            holder.itemView.findViewById<LinearLayout>(R.id.layout_movimiento).setBackground(ContextCompat.getDrawable(context, R.drawable.custom_layout_green_style))
        }
        else {
            holder.itemView.findViewById<LinearLayout>(R.id.layout_movimiento).setBackground(ContextCompat.getDrawable(context, R.drawable.custom_layout_red_style))
        }

        val fecha = formatDate(currentItem.fecha)

        holder.itemView.findViewById<TextView>(R.id.valor_movimiento).text = getTextValorMovimiento(currentItem)
        holder.itemView.findViewById<TextView>(R.id.fecha_movimiento).text = fecha + "      " + if(currentItem.tipoMovimiento == 0) CategoriasIngreso.getByValor(currentItem.categoria) else CategoriasGasto.getByValor(currentItem.categoria)

        holder.itemView.rowLayout.setOnClickListener {
//            val action = ListaMovimientosFragmentDirections.actionListaMovimientosFragmentToActualizarMovimientoFragment(currentItem)
            val action = ListaMovimientosFragmentDirections.actionListaMovimientosFragmentToAgregarMovimientosFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return movimientoList.size
    }

    fun setData(movimiento: List<Movimiento>) {
        this.movimientoList = movimiento
        notifyDataSetChanged()
    }

    private fun formatDate(fecha: Long): String {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val netDate = Date(fecha)

            return sdf.format(netDate).toString()
        } catch (e: Exception) {
            return e.toString()
        }
    }

    private fun getTextValorMovimiento(currentItem: Movimiento): String {
        if (currentItem.descripcion.length > 10) {
            if (currentItem.moneda == Moneda.PESO.valor) {
                return currentItem.descripcion.subSequence(0, 10).toString() + "      $" + currentItem.monto.toString()
            }
            else {
                return currentItem.descripcion.subSequence(0, 10).toString() + "      u\$s" + currentItem.monto.toString()
            }
        }
        else {
            if (currentItem.moneda == Moneda.PESO.valor) {
                return currentItem.descripcion + "      $" + currentItem.monto.toString()
            }
            else {
                return currentItem.descripcion + "      u\$s" + currentItem.monto.toString()
            }
        }
    }
}