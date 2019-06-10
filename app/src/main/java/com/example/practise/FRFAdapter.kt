package com.example.practise

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class FRFAdapter(val context: Context, val FRFitem:List<FRFitem>):RecyclerView.Adapter<FRFAdapter.Holder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        val v : View = LayoutInflater.from(p0.context).inflate(R.layout.frf_list_request, p0, false)
        return Holder(v)
    }

    override fun getItemCount(): Int {
        return FRFitem.count()
    }

    override fun onBindViewHolder(p0: Holder, p1: Int) {
        p0.bindCategory(FRFitem[p1], context)
    }

    inner class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val faultType = itemView?.findViewById<TextView>(R.id.faultType)
        val siteName = itemView?.findViewById<TextView>(R.id.siteName)
        val reportedBy = itemView?.findViewById<TextView>(R.id.reportedBy)

        fun bindCategory(FRFitem: FRFitem, context: Context){
            faultType?.text = FRFitem.faultType
            siteName?.text = FRFitem.siteName
            reportedBy?.text = FRFitem.reportedBy
        }
    }
}