//여기는 ItemCalendarViewFragment에서 사용하는 recyclerview용 adapter
// 아직 데이터베이스를 못받아와서 못쓰는중

package com.example.foodjoa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodjoa.database.JANG
import com.example.foodjoa.databinding.ItemCalenderRowBinding

class ItemExpandableAdapter(var items : ArrayList<JANG>) : RecyclerView.Adapter<ItemExpandableAdapter.MyViewHolder>(){

    interface OnItemClickListener {
        fun OnItemClick(data: JANG, position: Int)
    }

    var itemClickListener: OnItemClickListener? = null

    inner class MyViewHolder(
        val binding : ItemCalenderRowBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.layoutExpand.setOnClickListener {
                itemClickListener?.OnItemClick(items[bindingAdapterPosition] ,bindingAdapterPosition)
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = ItemCalenderRowBinding.inflate(
            LayoutInflater.from(parent.context)
            ,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.apply {
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

    fun remove(pos: Int) {
        notifyItemRemoved(pos)
    }

}