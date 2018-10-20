package com.example.android.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_editacc.*
import kotlinx.android.synthetic.main.activity_useracc.*
import kotlinx.android.synthetic.main.adapter_layout.*

class editActivity: AppCompatActivity()
{
    var previoustitle: String? = null
    var previousdescription: String? = null
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editacc)
             val myintent=intent
            previoustitle= myintent.getStringExtra("pretitle")
            previousdescription=myintent.getStringExtra("predesc")

        edittitlename.setText(previoustitle)
        editdescribe.setText(previousdescription)

        editnowbutton.setOnClickListener(){
            changeNow()
            Toast.makeText(this,"Your Post has been edited successfully",Toast.LENGTH_SHORT).show()
            val intent= Intent(this,HomepageActivity::class.java)
            startActivity(intent)
            }
        }
    private fun changeNow()
    {
        val user = FirebaseAuth.getInstance().currentUser
        if(user!=null) {
            val uid = user.uid
            val ref = FirebaseDatabase.getInstance().getReference("/posts/$uid")
            ref.addListenerForSingleValueEvent( object : ValueEventListener {

                val adapter= GroupAdapter<ViewHolder>()
                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach {
                        val user =it.getValue(UserPost::class.java)
                        if(user!=null)
                        {
                            if(user.title==previoustitle && user.description==previousdescription)
                            {
                                try
                                {
                                    it.ref.child("title").setValue(edittitlename.text.toString())
                                    it.ref.child("description").setValue(editdescribe.text.toString())
                                }
                                catch( e: Exception)
                                {
                                    e.printStackTrace()
                                }


                            }
                        }


                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

        }
    }
}
