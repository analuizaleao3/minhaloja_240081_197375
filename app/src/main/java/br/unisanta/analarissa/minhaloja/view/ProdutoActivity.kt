package br.unisanta.analarissa.minhaloja.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.unisanta.analarissa.minhaloja.R
import br.unisanta.analarissa.minhaloja.adapter.ProdutoAdapter
import br.unisanta.analarissa.minhaloja.api.RetrofitClient
import br.unisanta.analarissa.minhaloja.model.Product
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

class ProdutoActivity : AppCompatActivity(R.layout.activity_produto) {

    private val adapter = ProdutoAdapter { produto -> salvarFavorito(produto) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rv_produtos = findViewById<RecyclerView>(R.id.rv_produtos)
        val btn_favoritos = findViewById<Button>(R.id.btn_favoritos)
        val btn_sair = findViewById<Button>(R.id.btn_sair)
        rv_produtos.layoutManager = LinearLayoutManager(this)
        rv_produtos.adapter = adapter
        carregarProdutos()

        btn_favoritos.setOnClickListener{
            val intent = Intent(this, FavoritosActivity::class.java)
            startActivity(intent)
        }

        btn_sair.setOnClickListener{
            Firebase.auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun carregarProdutos() {
        val pb_carregando = findViewById<ProgressBar>(R.id.pb_carregando)

        pb_carregando.visibility = View.VISIBLE

        lifecycleScope.launch {
            try {
                val produtos: List<Product> = RetrofitClient.service.buscarProdutos()
                adapter.atualizarLista(produtos)
            }
            catch (e: Exception) {
                Toast.makeText(
                    this@ProdutoActivity,
                    "Erro ao carregar produtos: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            } finally {
                pb_carregando.visibility = View.GONE
            }
        }
    }
    private fun salvarFavorito(produto: Product) {
        val uid = Firebase.auth.currentUser?.uid

        if (uid == null) {
            Toast.makeText(this, "Faça login novamente", Toast.LENGTH_SHORT).show()
            return
        }

        val dados = hashMapOf(
            "id" to produto.id,
            "titulo" to produto.title,
            "preco" to produto.price,
            "categoria" to produto.category,
            "salvoEm" to FieldValue.serverTimestamp()
        )

        Firebase.firestore
            .collection("usuarios").document(uid)
            .collection("favoritos").document(produto.id.toString())
            .set(dados)
            .addOnSuccessListener {
                Toast.makeText(this, "${produto.title} salvo nos favoritos!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Erro ao salvar: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

}