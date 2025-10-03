package com.example.calculatorandroidd

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var calcDisplay: TextView

    private lateinit var historyDisplay: TextView

    private var firstNumber: Double? = null

    private var currentOp: Char?=null

    private var newInput: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        calcDisplay = findViewById<TextView>(R.id.CalculatorText)
        historyDisplay = findViewById<TextView>(R.id.history)

        val digitButtons = listOf(
            R.id.button0,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
        )


        for (id in digitButtons){
            findViewById<Button>(id).setOnClickListener{
                val digit = (it as Button).text.toString()
                if(calcDisplay.text == "0" || newInput || calcDisplay.text == "Calculator..." || calcDisplay.text == "" || calcDisplay.text == "ERROR"){
                    calcDisplay.text = digit
                    newInput = false
                } else{
                    calcDisplay.append(digit)
                }
            }
        }

        findViewById<Button>(R.id.buttonPrc).setOnClickListener { setOperation('/') }
        findViewById<Button>(R.id.buttonMn).setOnClickListener { setOperation('*') }
        findViewById<Button>(R.id.buttonPlus).setOnClickListener { setOperation('+') }
        findViewById<Button>(R.id.buttonMinus).setOnClickListener { setOperation('-') }


        findViewById<Button>(R.id.buttonClearHistory).setOnClickListener{
            historyDisplay.text = ""
        }

        findViewById<Button>(R.id.buttonRowne).setOnClickListener { calculate() }
        findViewById<Button>(R.id.buttonClear).setOnClickListener { clear() }

        findViewById<Button>(R.id.buttonDel).setOnClickListener { del() }


        findViewById<Button>(R.id.buttonKropka).setOnClickListener {
            if (!calcDisplay.text.contains(".")) {
                calcDisplay.append(".")
            }

        }

    }

    private fun del(){
        if(calcDisplay.text == "Calculator..."){
            clear()
        }
        if (calcDisplay.text.endsWith(".")) {
            calcDisplay.text = calcDisplay.text.toString().dropLast(2)
        }

        else {
            calcDisplay.text = calcDisplay.text.toString().dropLast(1)
            if (calcDisplay.text.isEmpty()) {
                calcDisplay.text = "0"
            }
        }
    }

    private fun setOperation(op: Char){
        firstNumber = calcDisplay.text.toString().toDoubleOrNull()
        currentOp = op
        newInput = true
    }

    private fun clear(){
        calcDisplay.text = "0"
        firstNumber = null
        currentOp = null
        newInput = true
    }



    private fun calculate(){

        val secondNumber = calcDisplay.text.toString().toDoubleOrNull()

        if(firstNumber != null && secondNumber != null && currentOp != null){
            val result = when(currentOp){
                '+' -> firstNumber!! + secondNumber
                '-' -> firstNumber!! - secondNumber
                '*' -> firstNumber!! * secondNumber
                '/' -> {
                    if(secondNumber == 0.0){
                        calcDisplay.text = "0"
                        historyDisplay.append("$firstNumber / 0 ERROR \n")
                        return
                    }
                    else{
                        firstNumber !! / secondNumber
                    }
                }
                else -> 0
            }
            historyDisplay.append("${firstNumber} $currentOp $secondNumber = $result \n")

            calcDisplay.text = result.toString()
            firstNumber = result.toDouble()
            newInput = true
        }

    }
}
