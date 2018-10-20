package com.example.android.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.signup.*

class SignUpActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)
       signupbutton.setOnClickListener(){
         performRegistration()
        }
    }

    private fun performRegistration(){
        val email= Signup_Email.text.toString()
        val password=Signup_Password.text.toString()
        if(email.isEmpty() || password.isEmpty())
        {
            Toast.makeText(this,"Please enter Email and password",Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            if (!it.isSuccessful)
                return@addOnCompleteListener
            else
            {

                Log.d("Mainsignup", "Yayy sccessful: ${it.result.user.uid}")

                saveUsertoFirebaseDatabase(it.result.user.uid)

            }
        }
                .addOnFailureListener{
                    Toast.makeText(this,"${it.message}",Toast.LENGTH_SHORT).show()
                }


    }
    private fun saveUsertoFirebaseDatabase( uid: String)
    {
        val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user=User(uid,Signup_Email.text.toString())
        ref.setValue(user).addOnSuccessListener {
            Toast.makeText(this,"Welcome,Writer!",Toast.LENGTH_SHORT).show()
            Log.d("Mainnn","dONEEEEE")
            val intent= Intent(this,HomepageActivity::class.java)
            startActivity(intent)
        }

    }

}
class User(val uid: String, val email: String)
{
    constructor(): this("", "")
}


