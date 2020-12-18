package com.udacity

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0
    private var loading = false
    private var complete = false
    private var valueAnimator = ValueAnimator()
    private val pointPosition: PointF = PointF(0.0f, 0.0f)
    private lateinit var textButton: String
    private var txtColor = 0
    private var colorLoading: Int = 0
    private var floatProgress: Float = 0f
    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (buttonState) {
            ButtonState.Clicked -> Log.e("Test", ".Clicked")

            ButtonState.Loading -> loading.apply {
                loading = true
                textButton = context.getString(R.string.button_loading)
                startLoadingAnimator()


            }

            ButtonState.Completed -> loading.apply {
                loading = false
                textButton = context.getString(R.string.button_text)
                paint.color = context.getColor(R.color.colorPrimary)
                valueAnimator.cancel()
                floatProgress = 0f
                invalidate()
            }

        }
    }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 45.0f

        typeface = Typeface.create("", Typeface.BOLD)

    }
    private val paintArc = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.YELLOW
    }
    private val rect = RectF(
        740f,
        50f,
        810f,
        110f
    )

    init {
        textButton = context.getString(R.string.button_text)
        isClickable = true
        context.withStyledAttributes(
            attrs,
            R.styleable.LoadingButton
        ) {
            paint.color = getColor(R.styleable.LoadingButton_bgColor, 0)
            txtColor = getColor(R.styleable.LoadingButton_textColor,0)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

    }

    /* override fun performClick(): Boolean {
         if (super.performClick()) return true
         invalidate()
         return true
     }*/

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawRect(pointPosition.x, pointPosition.y, width.toFloat(), height.toFloat(), paint)
        paint.color = txtColor
        canvas?.drawText(textButton, (width / 2).toFloat(), ((height + 20) / 2).toFloat(), paint)
        if (loading) {
            paint.color = context.getColor(R.color.colorPrimaryDark)

            canvas?.drawRect(
                0f, 0f,
                (width * (floatProgress / 100)).toFloat(), height.toFloat(), paint
            )
            paint.color = Color.WHITE
            canvas?.drawText(
                textButton,
                (width / 2).toFloat(),
                ((height + 20) / 2).toFloat(),
                paint
            )
            canvas?.drawArc(rect, 0f, (360 * (floatProgress / 100)), true, paintArc)

        }

    }

    fun startLoadingAnimator() {

        valueAnimator = ValueAnimator.ofFloat(0f, 100f)
        valueAnimator.addUpdateListener {
            floatProgress = it.animatedValue as Float
            paint.color = context.getColor(R.color.colorPrimary)
            invalidate()
        }
        /*valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {

            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })*/
        valueAnimator.duration = 2000
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.repeatCount = ValueAnimator.INFINITE

        valueAnimator.start()
    }

    fun setState(state: ButtonState) {
        buttonState = state
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}