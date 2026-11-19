package br.unisanta.analarissa.minhaloja.view

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.unisanta.analarissa.minhaloja.R
import br.unisanta.analarissa.minhaloja.adapter.FavoritoAdapter
import br.unisanta.analarissa.minhaloja.model.Favorito
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class FavoritosActivity : AppCompatActivity(R.layout.activity_favoritos) {

    private val adapter = FavoritoAdapter { favorito -> removerFavorito(favorito) }

    private fun favoritosRef() = Firebase.firestore
        .collection("usuarios").document(Firebase.auth.currentUser!!.uid)
        .collection("favoritos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rv_favoritos = findViewById<RecyclerView>(R.id.rv_favoritos)
        val fab_voltar = findViewById<FloatingActionButton>(R.id.fab_voltar_produtos)
        rv_favoritos.layoutManager = LinearLayoutManager(this)
        rv_favoritos.adapter = adapter
        carregarFavoritos()

        fab_voltar.setOnClickListener{
            finish()
        }
    }

    private fun carregarFavoritos() {
        val pb = findViewById<ProgressBar>(R.id.pb_favoritos)
        val tvVazio = findViewById<TextView>(R.id.tv_vazio)
        pb.visibility = View.VISIBLE
        favoritosRef()
            .orderBy("salvoEm", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { resultado ->
                pb.visibility = View.GONE
                val lista = resultado.documents.map { doc ->
                    Favorito(
                        id = (doc.getLong("id") ?: 0L).toInt(),
                        titulo = doc.getString("titulo") ?: "",
                        preco = doc.getDouble("preco") ?: 0.0,
                        categoria = doc.getString("categoria") ?: ""
                    )
                }
                adapter.atualizarLista(lista)
                tvVazio.visibility = if (lista.isEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener { e ->
                pb.visibility = View.GONE
                Toast.makeText(this, "Erro ao carregar favoritos: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun removerFavorito(favorito: Favorito) {
        favoritosRef().document(favorito.id.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Removido dos favoritos", Toast.LENGTH_SHORT).show()
                carregarFavoritos()   // recarrega a lista
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao remover: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}