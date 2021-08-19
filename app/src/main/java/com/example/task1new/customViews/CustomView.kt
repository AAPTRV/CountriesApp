package com.example.task1new.customViews

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.View
import android.widget.TextView
import android.view.MotionEvent

import android.view.MotionEvent.INVALID_POINTER_ID


class CustomView : View {
    var mPaint: Paint
    var otherPaint: Paint
    var outerPaint: Paint
    var mTextPaint: Paint
    var mRectF: RectF
    var mPadding: Int
    var arcLeft: Float
    var arcTop: Float
    var arcRight: Float
    var arcBottom: Float
    var mPath: Path

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    }


    init {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.BLUE
        mPaint.strokeWidth = 5f

        mTextPaint = Paint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = pxFromDp(context, 24f)

        otherPaint = Paint()
        outerPaint = Paint()
        outerPaint.style = Paint.Style.FILL
        outerPaint.color = Color.YELLOW
        mPadding = 100

        val displayMetrics = DisplayMetrics()
        (getContext() as Activity).windowManager
            .defaultDisplay
            .getMetrics(displayMetrics)
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        arcLeft = pxFromDp(context, 20f)
        arcTop = pxFromDp(context, 20f)
        arcRight = pxFromDp(context, 100f)
        arcBottom = pxFromDp(context, 100f)

        val p1 = Point(
            pxFromDp(context, 80f).toInt() + screenWidth / 2,
            pxFromDp(context, 40f).toInt()
        )
        val p2 = Point(
            pxFromDp(context, 40f).toInt() + screenWidth / 2,
            pxFromDp(context, 80f).toInt()
        )
        val p3 = Point(
            pxFromDp(context, 120f).toInt() + screenWidth / 2,
            pxFromDp(context, 80f).toInt()
        )
        mPath = Path()
        mPath.moveTo(p1.x.toFloat(), p1.y.toFloat())
        mPath.lineTo(p2.x.toFloat(), p2.y.toFloat())
        mPath.lineTo(p3.x.toFloat(), p3.y.toFloat())
        mPath.close()
        mRectF = RectF(
            (screenWidth / 4).toFloat(), (screenHeight / 3).toFloat(), (screenWidth / 6).toFloat(),
            (screenHeight / 2).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(mRectF, 10f, 10f, otherPaint)
        canvas.clipRect(mRectF, Region.Op.DIFFERENCE)
        canvas.drawPaint(outerPaint)
        canvas.drawLine(250f, 250f, 400f, 400f, mPaint)
        canvas.drawRect(
            mPadding.toFloat(),
            mPadding.toFloat(),
            (width - mPadding).toFloat(),
            (height - mPadding).toFloat(),
            mPaint
        )
        canvas.drawArc(arcLeft, arcTop, arcRight, arcBottom, 75f, 45f, true, mPaint)

        otherPaint.color = Color.GREEN
        otherPaint.style = Paint.Style.FILL
        canvas.drawRect(
            (left + (right - left) / 3).toFloat(),
            (top + (bottom - top) / 3).toFloat(),
            (right - (right - left) / 3).toFloat(),
            (bottom - (bottom - top) / 3).toFloat(), otherPaint
        )

        canvas.drawPath(mPath, mPaint)

        otherPaint.color = Color.BLACK
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), arcLeft, otherPaint)
        canvas.drawText("Canvas basics", (width * 0.3).toFloat(), (height * 0.8).toFloat(), mTextPaint)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = ev.findPointerIndex(0)
                val x = ev.getX(pointerIndex)
                val y = ev.getY(pointerIndex)
                //val pramougonik = newState()
                invalidate()
            }
            MotionEvent.ACTION_UP -> {

            }
            MotionEvent.ACTION_CANCEL -> {

            }
            MotionEvent.ACTION_POINTER_UP -> {
            }
        }
        return true
    }

    companion object {
        fun pxFromDp(context: Context, dp: Float): Float {
            return dp * context.resources.displayMetrics.density
        }
    }

}