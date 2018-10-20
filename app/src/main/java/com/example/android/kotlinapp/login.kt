package com.example.android.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login.*

class LoginActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
        loginbutton.setOnClickListener()
        {
            checkDetails()

        }


    }
private fun checkDetails()
{
    val email= login_email.text.toString()
    val password=login_password.text.toString()

    if(email.isEmpty() || password.isEmpty())
    {
        Toast.makeText(this,"Please enter Email and password", Toast.LENGTH_SHORT).show()
        return
    }
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(){
        if(!it.isSuccessful)
            return@addOnCompleteListener
        val intent= Intent(this,HomepageActivity::class.java)
        startActivity(intent)

    }
            .addOnFailureListener(){
                Toast.makeText(this, "INVALID LOGIN",Toast.LENGTH_SHORT).show()
            }




}
}
