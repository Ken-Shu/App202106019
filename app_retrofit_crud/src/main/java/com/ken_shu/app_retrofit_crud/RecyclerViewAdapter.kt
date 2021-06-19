package com.ken_shu.app_retrofit_crud


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ken_shu.app_retrofit_crud.model.Employee
import kotlinx.android.synthetic.main.row.view.*


class RecyclerViewAdapter(val listener : RowClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>(){
    var items : List<Employee> =ArrayList<Employee>()
    fun setListData(items : List<Employee>){
        this.items = items
    }
    class MyViewHolder (view : View) : RecyclerView.ViewHolder(view){
        val name = view.tv_name
        val basic = view.tv_basic

        fun bind(employee: Employee ){
            name.text = employee.name.toString()
            basic.text = employee.salary.basic.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row , parent , false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(items[position])
        }
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface RowClickListener{
        fun onItemClickListener(employee: Employee)
    }
}