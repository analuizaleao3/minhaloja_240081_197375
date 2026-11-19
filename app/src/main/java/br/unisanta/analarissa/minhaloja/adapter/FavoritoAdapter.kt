package br.unisanta.analarissa.minhaloja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.unisanta.analarissa.minhaloja.R
import br.unisanta.analarissa.minhaloja.model.Favorito

class FavoritoAdapter(
    private val aoRemover: (Favorito) -> Unit
) : RecyclerView.Adapter<FavoritoAdapter.ViewHolder>() {

    private val lista = mutableListOf<Favorito>()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tv_fav_titulo)
        val preco: TextView = view.findViewById(R.id.tv_fav_preco)
        val categoria: TextView = view.findViewById(R.id.tv_fav_categoria)
        val btnRemover: Button = view.findViewById(R.id.btn_remover)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorito, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val f = lista[position]
        holder.titulo.text = f.titulo
        holder.preco.text = "US$ %.2f".format(f.preco)
        holder.categoria.text = f.categoria
        holder.btnRemover.setOnClickListener { aoRemover(f) }
    }

    fun atualizarLista(novos: List<Favorito>) {
        lista.clear()
        lista.addAll(novos)
        notifyDataSetChanged()
    }
}