package br.unisanta.analarissa.minhaloja.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.unisanta.analarissa.minhaloja.R
import br.unisanta.analarissa.minhaloja.model.Product

class ProdutoAdapter(private val aoClicar: (Product) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ViewHolder>() {

    private val lista = mutableListOf<Product>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo = itemView.findViewById<TextView>(R.id.tv_titulo_produto)
        val categoria = itemView.findViewById<TextView>(R.id.tv_categoria_produto)
        val preco = itemView.findViewById<TextView>(R.id.tv_preco_produto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val p = lista[position]
        holder.titulo.text = p.title
        holder.preco.text = "US$ %.2f".format(p.price)
        holder.categoria.text = p.category
        holder.itemView.setOnClickListener { aoClicar(p) }
    }

    fun atualizarLista(novos: List<Product>) {
        lista.clear()
        lista.addAll(novos)
        notifyDataSetChanged()
    }
}