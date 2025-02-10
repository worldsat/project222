package com.uilover.project2142.Helper

import android.content.Context
import android.widget.Toast
import com.example.food2.Domain.FoodModel
import com.example.food2.Helper.TinyDB
import com.uilover.project2142.Model.ChangeNumberItemsListener


class ManagmentCart(val context: Context) {

    private val tinyDB = TinyDB(context)

    fun insertItem(item: FoodModel) {
        var listFood = getListCart()
        val existAlready = listFood.any { it.Title == item.Title }
        val index = listFood.indexOfFirst { it.Title == item.Title }

        if (existAlready) {
            listFood[index].numberInCart = item.numberInCart
        } else {
            listFood.add(item)
        }
        tinyDB.putListObject("CartList", listFood)
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show()
    }

    fun getListCart(): ArrayList<FoodModel> {
        return tinyDB.getListObject("CartList") ?: arrayListOf()
    }

    fun minusItem(listFood: ArrayList<FoodModel>, position: Int, listener: ChangeNumberItemsListener) {
        if (position < 0 || position >= listFood.size) return
        val currentCount = listFood[position].numberInCart
        if (currentCount <= 1) {
            listFood.removeAt(position)
        } else {
            listFood[position].numberInCart = currentCount - 1
        }
        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

    fun plusItem(listFood: ArrayList<FoodModel>, position: Int, listener: ChangeNumberItemsListener) {
        listFood[position].numberInCart++
        tinyDB.putListObject("CartList", listFood)
        listener.onChanged()
    }

    fun getTotalFee(): Double {
        val listFood = getListCart()
        var fee = 0.0
        for (item in listFood) {
            fee += item.Price * item.numberInCart
        }
        return fee
    }
}