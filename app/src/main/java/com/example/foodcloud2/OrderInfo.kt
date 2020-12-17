package com.example.foodcloud2


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class OrderInfo : AppCompatActivity() {
    lateinit var  id:TextView
    lateinit var date:TextView
    lateinit var time:TextView
    lateinit var amount:TextView
    lateinit var result:String
    lateinit var phonenumber:String
    lateinit var saved:String
    lateinit var recyclerview:RecyclerView
    lateinit var list:ArrayList<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_info)
        id=findViewById(R.id.order_id)
        date=findViewById(R.id.order_date)
        time=findViewById(R.id.order_time)
        amount=findViewById(R.id.order_amount)
        recyclerview = findViewById(R.id.rv)
        // result= intent.getStringExtra("SCAN_RESULT").toString()
       result="c209fa8e-2475-4a60-9681-2385d7ab3fd1+96176675755"
        phonenumber=result.takeLast(12)

        val database = FirebaseDatabase.getInstance()
        val myRef = database.reference.child("Orders").child(phonenumber)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(postSnapshot in dataSnapshot.children){
                    val order:Order= postSnapshot.getValue(Order::class.java)!!
                    if(order.key==result.take(36)){
                        date.text=order.date
                        time.text=order.time
                        amount.text = order.totalAmount
                        saved=order.itemID
                       // id.text=order.itemID
                        //arrayItems= arrayOf(order.itemID)
                       // id.text=arrayItems[1]
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val itemRef= database.reference.child("Cart List").child("Admin View").child(phonenumber).child("Item")
        itemRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists()){
                    list = arrayListOf()
                    for(postSnapshot in dataSnapshot.children){
                        val item: Cart = postSnapshot.getValue(Cart::class.java)!!
                        if(saved.contains(item.iid)){
                            list.add(item)
                        }}
                    val adapterClass = AdapterClass(list)
                    recyclerview.adapter = adapterClass

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })


    }

}