package com.example.calculadoraimc

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import kotlin.math.pow


class MainActivity : AppCompatActivity() {

    lateinit var heightSlider: Slider

    lateinit var weightTextView: TextView
    lateinit var weightMinusButton: Button
    lateinit var weightPlusButton: Button

    lateinit var calculateButton: Button

    lateinit var resultTextView: TextView
    lateinit var descriptionTextView: TextView
    lateinit var resultImageView: ImageView

    var weight: Float = 60f
    var height: Float = 170f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        heightSlider = findViewById(R.id.heightSlider)
        weightTextView = findViewById(R.id.weightTextView)
        weightMinusButton = findViewById(R.id.weightMinusButton)
        weightPlusButton = findViewById(R.id.weightPlusButton)
        calculateButton = findViewById(R.id.calculateButton)
        resultTextView = findViewById(R.id.resultTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)
        resultImageView = findViewById(R.id.resultImageView)

        heightSlider.value = height
        weightTextView.text = "$weight Kg"

        weightMinusButton.setOnClickListener {
            weight--
            if (weight < 20) {
                weight = 20.0f
            }
            weightTextView.text = "$weight Kg"
        }

        weightPlusButton.setOnClickListener {
            weight++
            if (weight > 300) {
                weight = 300.0f
            }
            weightTextView.text = "$weight Kg"
        }

        heightSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                height = value
            }
        }

        calculateButton.setOnClickListener {
            val result = weight / (height / 100).pow(2)

            resultTextView.text = String.format(Locale.getDefault(), "%.2f", result)

            when (result) {
                in 0.0..<18.5 -> { // Bajo peso
                    setColorAndTextAndImage(R.color.underWeight, R.string.underWeight, R.drawable.weight_under)
                }
                in 18.5..<25.0 -> { // Peso normal
                    setColorAndTextAndImage(R.color.normalWeight, R.string.normalWeight, R.drawable.weight_normal)
                }
                in 25.0..<30.0 -> { // Sobrepeso
                    setColorAndTextAndImage(R.color.overWeight, R.string.overWeight, R.drawable.weight_over)
                }
                in 30.0..<35.0 -> { // Obesidad
                    setColorAndTextAndImage(R.color.obesity, R.string.obesity, R.drawable.weight_obesity)
                }
                else -> { // Obesidad extrema
                    setColorAndTextAndImage(R.color.extremeObesity, R.string.extremeObesity, R.drawable.weight_extreme_obesity)
                }
            }
        }
    }

    fun setColorAndTextAndImage(colorRes: Int, textRes: Int, drawableRes: Int) {
        val color = getColor(colorRes)
        resultTextView.setTextColor(color)
        descriptionTextView.setTextColor(color)
        descriptionTextView.text = getString(textRes)
        resultImageView.setImageDrawable(getDrawable(drawableRes))
        resultImageView.setColorFilter(color)

        scaleImage()
    }

    fun scaleImage() {
        resultImageView.scaleX = 0.0f
        resultImageView.scaleY = 0.0f
        val scaleUpX = ObjectAnimator.ofFloat(resultImageView, "scaleX", 1f)
        val scaleUpY = ObjectAnimator.ofFloat(resultImageView, "scaleY", 1f)
        scaleUpX.setDuration(500)
        scaleUpY.setDuration(500)

        val scaleUp = AnimatorSet()
        scaleUp.play(scaleUpX).with(scaleUpY)

        scaleUp.start()
    }
}