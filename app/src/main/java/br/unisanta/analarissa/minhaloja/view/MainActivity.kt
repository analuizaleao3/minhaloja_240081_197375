package br.unisanta.analarissa.minhaloja.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unisanta.analarissa.minhaloja.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val edt_email = findViewById<EditText>(R.id.edt_email)
        val edt_senha = findViewById<EditText>(R.id.edt_senha)
        val btn_entrar = findViewById<Button>(R.id.btn_entrar)
        val btn_cadastrar = findViewById<Button>(R.id.btn_cadastrar)

        auth = Firebase.auth

        if (auth.currentUser != null){
            val intent = Intent(this, ProdutoActivity::class.java)
            startActivity(intent)
        }

        btn_entrar.setOnClickListener{
            auth.signInWithEmailAndPassword(edt_email.text.toString(), edt_senha.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, ProdutoActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                }
        }

    }
}