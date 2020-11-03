package org.wit.expense.console.controllers

import com.github.mm.coloredconsole.colored
import mu.KotlinLogging
import org.wit.expense.console.models.ExpenseJSONStore
import org.wit.expense.console.models.ExpenseMemStore
import org.wit.expense.console.models.ExpenseModel
import org.wit.expense.console.views.ExpenseView

class ExpenseController {

    // val expenses = ExpenseMemStore()
    val expenses = ExpenseJSONStore()
    val expenseView = ExpenseView()
    val logger = KotlinLogging.logger {}

    init {
        logger.info { "Launching Expense Console App" }

    colored {
        println("Expense Kotlin App Version 4.0".cyan.bold)
    }
    }

    fun start() {
        var input: Int

        do {
            input = menu()
            when (input) {
                1 -> add()
                2 -> update()
                3 -> list()
                4 -> search()
                5 -> delete()
                -99 -> dummyData()
                -1 -> println("Exiting App")
                else -> println("Invalid Option")
            }
            println()
        } while (input != -1)
        logger.info { "Shutting Down Expense Console App" }
    }

    fun menu() :Int { return expenseView.menu() }

    fun add(){
        var aExpense = ExpenseModel()

        if (expenseView.addExpenseData(aExpense))
            expenses.create(aExpense)
        else
            logger.info("Expense Not Added")
    }

    fun list() {
        expenseView.listExpenses(expenses)
    }

    fun update() {

        expenseView.listExpenses(expenses)
        var searchId = expenseView.getId()
        val aExpense = search(searchId)

        if(aExpense != null) {
            if(expenseView.updateExpenseData(aExpense)) {
                expenses.update(aExpense)
                expenseView.showExpense(aExpense)
                logger.info("Expense Updated : [ $aExpense ]")
            }
            else
                logger.info("Expense Not Updated")
        }
        else
            println("Expense Not Updated...")
    }

    fun delete() {
        expenseView.listExpenses(expenses)
        var searchId = expenseView.getId()
        val aExpense = search(searchId)

        if(aExpense != null) {
            expenses.delete(aExpense)
            println("Expense Deleted...")
            expenseView.listExpenses(expenses)
        }
        else
            println("Expense Not Deleted...")
    }

    fun search() {
        val aExpense = search(expenseView.getId())!!
        expenseView.showExpense(aExpense)
    }


    fun search(id: Long) : ExpenseModel? {
        var foundExpense = expenses.findOne(id)
        return foundExpense
    }

    fun dummyData() {
        expenses.create(ExpenseModel(amount = "New York New York", description = "So Good They Named It Twice"))
        expenses.create(ExpenseModel(amount= "Ring of Kerry", description = "Some place in the Kingdom"))
        expenses.create(ExpenseModel(amount = "Waterford City", description = "You get great Blaas Here!!"))
    }
}