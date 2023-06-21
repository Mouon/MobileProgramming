// Item_insert_fragment2에서 사용하는 recyclerview
// 선택한 카테고리에 따라 값이 달라지게 하기 위해서 recyclerview사용

package com.example.foodjoa.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.example.foodjoa.databinding.ItemInsertRowBinding


class MyInsertItemRecyclerViewAdapter(var items:ArrayList<String>)
    : RecyclerView.Adapter<MyInsertItemRecyclerViewAdapter.MyViewHolder>(){

    interface OnItemClickListener {
        fun OnItemClick(position: Int, items: ArrayList<String>)
    }

    var itemClickListener : OnItemClickListener?= null

    inner class MyViewHolder(val binding: ItemInsertRowBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.itemNumber.setOnClickListener {
                itemClickListener?.OnItemClick(bindingAdapterPosition, items)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = ItemInsertRowBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply{
            itemNumber.text = items[position]
        }
    }

    override fun getItemCount(): Int {

        return items.size
    }

}