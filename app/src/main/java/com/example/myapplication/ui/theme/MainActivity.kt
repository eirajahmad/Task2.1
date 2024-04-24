package com.example.myapplication.ui.theme

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerSource = findViewById<Spinner>(R.id.spinnerSourceUnit)
        val spinnerDest = findViewById<Spinner>(R.id.spinnerDestUnit)
        val editTextValue = findViewById<EditText>(R.id.editTextValue)
        val buttonConvert = findViewById<Button>(R.id.buttonConvert)
        val textViewResult = findViewById<TextView>(R.id.textViewResult)

        // Initialize source spinner with all unit options (consider starting with one category)
        setupSpinner(spinnerSource, R.array.length_units) // Example: Starting with length units

        spinnerSource.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Update destination spinner based on source selection
//                val selectedUnit = parent.getItemAtPosition(position).toString()
//                updateDestinationUnits(selectedUnit, spinnerDest)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        buttonConvert.setOnClickListener {
            val sourceUnit = spinnerSource.selectedItem.toString()
            val destUnit = spinnerDest.selectedItem.toString()
            val value = editTextValue.text.toString().toDoubleOrNull()

            if (value != null) {
                val result = convertUnits(sourceUnit, destUnit, value)
                if(result == -1.0){
                    Toast.makeText(this, "Conversion value selected is different type", Toast.LENGTH_SHORT).show()
                } else {
                    textViewResult.text = "Result: $result"
                }
            }else if(!isValidForConversion(sourceUnit, value)){
                editTextValue.error = "Invalid input for $sourceUnit"
                return@setOnClickListener
            } else {
                textViewResult.text = "Please enter a valid number"
            }
        }
    }

    private fun isValidForConversion(unitType: String, value: Double?): Boolean {
        // Kelvin should not have a negative value
        if (unitType == "Kelvin" && value!! < 0.0) {
            return false
        }
        return true
    }

    private fun setupSpinner(spinner: Spinner, arrayId: Int) {
        ArrayAdapter.createFromResource(
            this,
            arrayId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    // Include your convertUnits function here
    private fun convertUnits(sourceUnit: String, destUnit: String, value: Double): Double {
        if (sourceUnit == destUnit) {
            return -1.0 // Indicating that conversion between the same units is not needed
        }

        return when (sourceUnit to destUnit) {
            // Length Conversions
            "Inches" to "Centimeters" -> value * 2.54
            "Feet" to "Centimeters" -> value * 30.48
            "Yards" to "Centimeters" -> value * 91.44
            "Miles" to "Kilometers" -> value * 1.60934
            "Centimeters" to "Inches" -> value / 2.54
            "Centimeters" to "Feet" -> value / 30.48
            "Centimeters" to "Yards" -> value / 91.44
            "Kilometers" to "Miles" -> value / 1.60934

            // Weight Conversions
            "Pounds" to "Kilograms" -> value * 0.453592
            "Ounces" to "Grams" -> value * 28.3495
            "Tons" to "Kilograms" -> value * 907.185
            "Kilograms" to "Pounds" -> value / 0.453592
            "Grams" to "Ounces" -> value / 28.3495
            "Kilograms" to "Tons" -> value / 907.185

            // Temperature Conversions

            "Celsius" to "Fahrenheit" -> (value * 9/5) + 32
            "Fahrenheit" to "Celsius" -> (value - 32) * 5/9
            "Celsius" to "Kelvin" -> value + 273.15
            "Kelvin" to "Celsius" -> value - 273.15
            "Fahrenheit" to "Kelvin" -> (value - 32) * 5/9 + 273.15
            "Kelvin" to "Fahrenheit" -> (value - 273.15) * 9/5 + 32

            else -> -1.0 // For unsupported conversions
        }
    }

}
