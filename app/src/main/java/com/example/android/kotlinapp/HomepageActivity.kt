package com.example.android.kotlinapp
import android.content.Intent
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window

import com.example.android.kotlinapp.R.styleable.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_homepage.*
import kotlinx.android.synthetic.main.activity_useracc.*
import kotlinx.android.synthetic.main.adapter_layout.view.*
import kotlinx.android.synthetic.main.adapter_list.view.*

import com.google.firebase.database.DatabaseReference
import android.widget.CompoundButton





class HomepageActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        logout.setOnClickListener()
        {
            FirebaseAuth.getInstance().signOut()
            val intent= Intent(this,MainActivity:: class.java)
            startActivity(intent)
        }
        plusbutton.setOnClickListener(){
            val intent= Intent(this,AddPost:: class.java)
            startActivity(intent)
        }

        editText.setText("Posts")
        switch1.isChecked=false
        val adapter= GroupAdapter<ViewHolder>()
        if(adapter!=null) {
            recycler_list.adapter = adapter
            fetchList()
        }
        switch1.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            // do something, the isChecked will be
            // true if the switch is in the On position
            if(isChecked)
            {
                editText.setText("Your Pieces")
                val adapter= GroupAdapter<ViewHolder>()
                if(adapter!=null) {
                    recycler_list.adapter = adapter
                    fetchData()


                }
            }
            else
            {
                editText.setText("Posts")
                val adapter= GroupAdapter<ViewHolder>()
                if(adapter!=null) {
                    recycler_list.adapter = adapter
                    fetchList()
                }

            }

        })








    }



    private fun fetchList()
    {
            val ref = FirebaseDatabase.getInstance().getReference("users")

            ref.addListenerForSingleValueEvent( object : ValueEventListener {
                val adapter=GroupAdapter<ViewHolder>()
                override fun onDataChange(p0: DataSnapshot) {
                    p0.children.forEach{
                        val user=it.getValue(User::class.java)
                        if(user!=null) {
                            val uid=user.uid
                                val refToYourUserid = ref.parent?.child("posts")?.child(uid)
                                refToYourUserid?.addListenerForSingleValueEvent( object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot)
                                    {

                                        p0.children.forEach{
                                            val user1 =it.getValue(UserPost::class.java)
                                            if(user1!=null)
                                            {
                                                val arg2=user1?.title
                                                val arg3=user1?.description
                                                val result=ListData(user.email,arg2,arg3)
                                                adapter.add(UserItem2(result))
                                            }

                                        }

                                    }
                                    override fun onCancelled(p0: DatabaseError) {

                                    }

                                })

                        }
                    }

                    recycler_list.adapter = adapter

                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })

    }
    private fun fetchData()
    {
        val user2 = FirebaseAuth.getInstance().currentUser
        if(user2!=null) {
            val uid = user2.uid
            val ref = FirebaseDatabase.getInstance().getReference("/posts/$uid")

            ref.addListenerForSingleValueEvent( object : ValueEventListener {

                val adapter=GroupAdapter<ViewHolder>()

                override fun onDataChange(p0: DataSnapshot) {
                    if(p0.hasChildren())
                    {
                        p0.children.forEach{
                            val user =it.getValue(UserPost::class.java)

                            if(user!=null) {
                                adapter.add(UserItem(user))
                            }
                        }



                    }
                    else
                    {
                        val user1="Sorry"
                        val user2="No Posts Yet!"
                        adapter.add(Nothing(user1,user2))

                    }
                    recycler_list.adapter=adapter

                }


                override fun onCancelled(p0: DatabaseError) {

                }
            })

        }
    }

}
class ListData(val emailid: String, val title: String, val description :String)
class UserItem2(val user:ListData):Item<ViewHolder>()
{
    override fun bind(viewHolder: ViewHolder, position:Int){
            viewHolder.itemView.textView1.setText(user.emailid)
            viewHolder.itemView.textView2.setText(user.title)
            viewHolder.itemView.textView3.setText(user.description)
    }
    override fun getLayout():Int {
        return R.layout.adapter_list
    }
}
class UserItem(val user2: UserPost):Item<ViewHolder>()
{


    override fun bind(viewHolder: ViewHolder, position:Int){
        if(user2.title!=null && user2.description!=null) {
            viewHolder.itemView.title1.setText(user2.title)
            viewHolder.itemView.describe1.setText(user2.description)

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


}
class Nothing(val user1: String,val user2: String):Item<ViewHolder>()
{
    override fun bind(viewHolder: ViewHolder, position:Int){
            viewHolder.itemView.textView3.setText(user2)


    }

    override fun getLayout():Int {

        return R.layout.adapter_list

    }

}
