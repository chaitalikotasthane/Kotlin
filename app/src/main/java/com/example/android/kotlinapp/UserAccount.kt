package com.example.android.kotlinapp

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_useracc.*
import android.util.Log
import android.widget.Toast
import android.content.Context
import android.view.View
import com.google.firebase.database.DatabaseError
import kotlinx.android.synthetic.main.adapter_layout.*
import kotlinx.android.synthetic.main.adapter_layout.view.*

 class UserAccount:AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_useracc)

        val adapter=GroupAdapter<ViewHolder>()
        recycler_view.adapter=adapter
        fetchData()



    }

    private fun fetchData()
    {
        val user = FirebaseAuth.getInstance().currentUser
        if(user!=null) {
            val uid = user.uid
            val ref = FirebaseDatabase.getInstance().getReference("/posts/$uid")

            ref.addListenerForSingleValueEvent( object : ValueEventListener {

                val adapter=GroupAdapter<ViewHolder>()

                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach{
                        val user =it.getValue(UserPost::class.java)

                        if(user!=null) {
                            adapter.add(UserItem(user))
                        }
                    }
                    recycler_view.adapter=adapter

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }
    }

    public fun post(intent: Intent)
    {
        startActivity(intent)
    }
}

/*class UserItem(val user: UserPost):Item<ViewHolder>()
{

    override fun bind(viewHolder: ViewHolder, position:Int){
        if(user.title!=null && user.description!=null) {
            viewHolder.itemView.title1.setText(user.title)
            viewHolder.itemView.describe1.setText(user.description)

        }
        viewHolder.itemView.editnow.setOnClickListener(View.OnClickListener {
            val intent=Intent(viewHolder.itemView.context,editActivity::class.java)
            val pretitle=viewHolder.itemView.title1.text.toString()
            val predesc=viewHolder.itemView.describe1.text.toString()
            intent.putExtra("pretitle",pretitle)
            intent.putExtra("predesc",predesc)
            viewHolder.itemView.context.startActivity(intent)
        })
    }

    override fun getLayout():Int {

        return R.layout.adapter_layout

    }


}*/