package com.grupo9.clubdeportivo.admin

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.grupo9.clubdeportivo.databinding.ItemSocioNosocioBinding

class SocioNoSocioAdapter(private var lista: MutableList<SocioItem>) :
    RecyclerView.Adapter<SocioNoSocioAdapter.SocioViewHolder>() {

    private var listaCompleta = lista.toMutableList()

    inner class SocioViewHolder(val binding: ItemSocioNosocioBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SocioViewHolder {
        val binding = ItemSocioNosocioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SocioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SocioViewHolder, position: Int) {
        val socio = lista[position]
        holder.binding.tvNombre.text = socio.nombre
        holder.binding.tvDni.text = "DNI: ${socio.dni}"

        if (socio.estado != null) {
            // Es SOCIO → mostrar badge con color
            holder.binding.tvBadge.text = socio.estado
            if (socio.estado == "Al día") {
                holder.binding.tvBadge.setTextColor(Color.parseColor("#2E7D32"))
                holder.binding.tvBadge.setBackgroundColor(Color.parseColor("#E8F5E9"))
            } else {
                holder.binding.tvBadge.setTextColor(Color.parseColor("#C62828"))
                holder.binding.tvBadge.setBackgroundColor(Color.parseColor("#FFEBEE"))
            }
        } else {
            // Es NO SOCIO → mostrar actividad sin color
            holder.binding.tvBadge.text = socio.actividad
            holder.binding.tvBadge.setTextColor(Color.parseColor("#1A3A5C"))
            holder.binding.tvBadge.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    override fun getItemCount() = lista.size

    fun filtrar(texto: String) {
        lista = if (texto.isEmpty()) {
            listaCompleta.toMutableList()
        } else {
            listaCompleta.filter {
                it.nombre.contains(texto, ignoreCase = true) ||
                        it.dni.contains(texto, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}