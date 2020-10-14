package com.ingeniarius.minhashabilidades

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.util.*

class CadastroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        cadastro_button_cadastro.setOnClickListener {
            performRegister()
        }

        ja_tem_conta_textView.setOnClickListener {
            Log.d("CadastroActivity", "Tente mostrar a activity de login")

            // Lançar a activity de login de alguma forma
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selecionarFoto_button_cadastro.setOnClickListener {
            Log.d("CadastroActivity", "Tente mostrar o seletor de foto")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var uriFotoSelecionada: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // continuar e checar qual foi a imagem selecionada...
            Log.d("CadastroActivity", "Foto foi selecionada")
        }

        uriFotoSelecionada = data?.data

        val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriFotoSelecionada)

        val bitmapDrawable = BitmapDrawable(bitmap)
        selecionarFoto_button_cadastro.setBackgroundDrawable(bitmapDrawable)
    }

    private fun performRegister() {
        val email = email_edittext_cadastro.text.toString()
        val senha = senha_edittext_cadastro.text.toString()

        if (email.isEmpty() || senha.isEmpty()){
            Toast.makeText(this, "Por favor, insira seu email e senha.", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "E-mail é: "+email)
        Log.d("MainActivity", "Senha: $senha")

        // Autenticação Firebase para criar um usuário com login e senha
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if successful
                Log.d("CadastroActivity", "Usuário criado com sucesso com a uid: ${it.result?.user?.uid}")

                uploadImagemParaArmazenamentoFirebase()
            }
            .addOnFailureListener {
                Log.d("CadastroActivity", "Falha ao criar usuário: ${it.message}")
                Toast.makeText(this, "Falha ao criar usuário: ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }

    private fun uploadImagemParaArmazenamentoFirebase() {
        if (uriFotoSelecionada == null){
            Log.d("CadastroActivity", "uriFotoSelecionada é null")
            return
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(uriFotoSelecionada!!)
            .addOnSuccessListener {
                Log.d("CadastroActivity", "Imagem enviada com sucesso: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("CadastroActivity", "Local do arquivo: $it")
                }
            }
    }
}