package com.example.foodcloud2


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
    lateinit var infobtn:Button
    lateinit var ID:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_info)

        date=findViewById(R.id.order_date)
        time=findViewById(R.id.order_time)
        amount=findViewById(R.id.order_amount)
        infobtn=findViewById(R.id.info_btn)
         result= intent.getStringExtra("RESULT").toString()
        ID=result.take(36)
       //result="c209fa8e-2475-4a60-9681-2385d7ab3fd1+96176675755"
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

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
infobtn.setOnClickListener {
    val intent = Intent(this, ItemDetails::class.java)
    intent.putExtra("ID",ID)
    intent.putExtra("SAVED", saved)
    intent.putExtra("PHONE",phonenumber)
    startActivity(intent)
}
    }

}