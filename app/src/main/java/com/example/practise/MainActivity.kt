package com.example.practise

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    lateinit var faultType: TextView
    lateinit var siteName: TextView
    lateinit var reportedBy: TextView

    lateinit var frf_item_list : MutableList<FRFitem>
    lateinit var ref : DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        frf_item_list = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("FRF_list")

        faultType = findViewById(R.id.faultTypeEntry)
        siteName = findViewById(R.id.siteNameEntry)
        reportedBy = findViewById(R.id.reportedByEntry)

        switchToCamera.setOnClickListener {
            val cameraIntent = Intent(this, CameraUpload::class.java )
            startActivity(cameraIntent)
        }


        addBtn.setOnClickListener{
            saveFRF()
        }



        ref.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    frf_item_list.clear()
                    for(h in p0.children){
                        val FRFitem= h.getValue(FRFitem::class.java)
                        frf_item_list.add(FRFitem!!)
                    }

                    val layoutManager = LinearLayoutManager(applicationContext)
                    recyclerView.layoutManager = layoutManager
                    recyclerView.adapter = FRFAdapter(applicationContext, frf_item_list)
                }
            }

        })


    }

    private fun saveFRF(){
        val faultType = faultTypeEntry.text.toString().trim()
        val siteName = siteNameEntry.text.toString().trim()
        val reportedBy = reportedByEntry.text.toString().trim()

        if(faultType.isEmpty()){
            faultTypeEntry.error = "Please enter a fault type"
        }

        if(siteName.isEmpty()){
            siteNameEntry.error = "Please enter a site name."
        }

        if(reportedBy.isEmpty()){
            reportedByEntry.error = "Please enter who reported."
        }

        val FRFid = ref.push().key
        val frf = FRFitem(FRFid, faultType, siteName, reportedBy)

        ref.child(FRFid!!).setValue(frf).addOnCompleteListener{
            Toast.makeText(applicationContext,"FRF added successfully", Toast.LENGTH_LONG).show()
        }
    }
}
