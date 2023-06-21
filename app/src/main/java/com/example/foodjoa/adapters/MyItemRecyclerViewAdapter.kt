//Item_Content_Fragment에서 사용하는 recyclerview 용 adapter 데이터 중 일부 보여줍니다.

package com.example.foodjoa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodjoa.database.JANG
import com.example.foodjoa.databinding.ItemRowBinding

class MyItemRecyclerViewAdapter(var items: ArrayList<JANG>)
    : RecyclerView.Adapter<MyItemRecyclerViewAdapter.MyViewHolder>(){


    inner class MyViewHolder(val binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemRowBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply{
            productname.text = items[position].productname
            if (items[position].category == "외식"){
                productcount.visibility = View.GONE
                productcountunit.visibility = View.GONE
            }
            else
                productcount.text = items[position].count.toString()
            when(items[position].countunit) {
                0 -> productcountunit.text = "개"
                1 -> productcountunit.text = "마리"
                2 -> productcountunit.text = "그램"
                else -> productcountunit.text = ""
            }
            productprice.text = items[position].price.toString()

        }
    }

    override fun getItemCount(): Int {

        return items.size
    }

    fun addItem(data : JANG) {
        items.add(data)
        notifyItemInserted(itemCount-1)
    }

}