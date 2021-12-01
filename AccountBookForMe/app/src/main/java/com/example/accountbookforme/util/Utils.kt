package com.example.accountbookforme.util

import com.example.accountbookforme.model.Expense
import com.example.accountbookforme.database.entity.ItemEntity
import com.example.accountbookforme.model.Total
import java.math.BigDecimal

object Utils {

    /**
     * BigDecimal型を小数点第2位までの文字列に変換する
     */
    fun convertToStrDecimal(decimal: BigDecimal) = String.format("%.2f", decimal)

    /**
     * BigDecimal型を整数の文字列に変換する
     */
    private fun convertToStrInteger(decimal: BigDecimal) = String.format("%d", decimal.toInt())

    /**
     * 総額をパーセンテージ付き文字列で返す
     */
    fun totalWithPercentage(partialValue: BigDecimal, totalValue: BigDecimal): String =
        convertToStrDecimal(partialValue) + " (" + convertToStrInteger(
            calcPercentage(
                partialValue,
                totalValue
            )
        ) + "%)"

    /**
     * パーセンテージを計算
     * param: partialValue 分子
     * param: totalValue 分母
     * return パーセンテージ
     */
    private fun calcPercentage(partialValue: BigDecimal, totalValue: BigDecimal): BigDecimal =
        if (totalValue == BigDecimal.ZERO) {
            BigDecimal.ZERO
        } else {
            partialValue.divide(totalValue, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal(100))
        }

    /**
     * 総額を計算
     */
    fun calcTotal(totalList: List<Total>): BigDecimal? = totalList.fold(BigDecimal.ZERO) { acc, total ->
        acc + total.total
    }

    /**
     * 支出の総額を計算
     */
    fun calcExpenseTotal(expenseList: List<Expense>): BigDecimal? =
        expenseList.fold(BigDecimal.ZERO) { acc, expense ->
            acc + expense.total
        }

    /**
     * 品物の総額を計算
     */
    fun calcItemTotal(itemList: List<ItemEntity>): BigDecimal? = itemList.fold(BigDecimal.ZERO) { acc, item ->
        acc + item.price
    }
}