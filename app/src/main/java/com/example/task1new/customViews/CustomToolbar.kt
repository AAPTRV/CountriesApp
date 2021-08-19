package com.example.task1new.customViews

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.example.task1new.R
import java.lang.Exception

class CustomToolbar : LinearLayout {

    private var mBackClick: AppCompatImageView? = null
    private var mTvTitle: TextView? = null
    private var mTitleTextId: Int? = null

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val view = inflate(context, R.layout.custom_toolbar, this)

        layoutParams = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        layoutParams.height = context.resources.getDimension(R.dimen.size_56dp).toInt()
        setBackgroundColor(ContextCompat.getColor(context, R.color.purple_700))
        (layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER

        mTvTitle = view.findViewById(R.id.mTitle)
        mBackClick = view.findViewById(R.id.mIvBackButton)
        attrs?.let {
            val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomToolbar, 0, 0)
            try {
                mTitleTextId = typedArray.getResourceId(R.styleable.CustomToolbar_customToolbarTitleText, -1)
                mTitleTextId?.let { mTvTitle?.text = context.getString(it) }
            } catch (e: Exception) {

            } finally {
                typedArray.recycle()
            }
        }

    }

    fun setBackClickListener(click: () -> Unit) {
        mBackClick?.setOnClickListener {
            click.invoke()
        }
    }

}
