package com.ingeniarius.minhashabilidades

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        entrar_button_login.setOnClickListener {
            val email = email_editText_login.text.toString()
            val senha = senha_editText_login.text.toString()

            Log.d("Login", "Tentativa de login com email/senha: $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha)
//                .addOnCompleteListener()
//                .add
        }

        voltarAoRegistro_textView_login.setOnClickListener {
            finish()
        }
    }
}