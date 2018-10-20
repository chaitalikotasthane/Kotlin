package com.example.android.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_addpost.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase


class AddPost:AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addpost)

        postbutton.setOnClickListener{
            postToFirebaseDatabase()
        }
    }
    private fun postToFirebaseDatabase()
    {
        val t=titlename.text.toString()
        val descript=describe.text.toString()
        if(t.isEmpty() || descript.isEmpty())
        {
            Toast.makeText(this,"Please enter title and description",Toast.LENGTH_SHORT).show()
            return
        }


        val user = FirebaseAuth.getInstance().currentUser
        if(user!=null)
        {
            val uid = user.uid
            Log.d("Mainn","username ka uid hai $uid")
            val ref = FirebaseDatabase.getInstance().getReference("/posts")
            val userpost=UserPost(t,descript)
            ref.child(user.uid).push().setValue(userpost)

            Toast.makeText(this,"Your Post has been posted successfully",Toast.LENGTH_SHORT).show()
            val intent= Intent(this,HomepageActivity::class.java)
            startActivity(intent)
        }

    }
}
class UserPost(val title: String , val description: String)
{
    constructor(): this("", "")
}