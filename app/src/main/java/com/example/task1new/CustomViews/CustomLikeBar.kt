package com.example.task1new.CustomViews

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.task1new.R
import org.w3c.dom.Text
import java.lang.Exception

class CustomLikeBar : ConstraintLayout {

    private var mTvNumberOfLikes: TextView? = null
    private var mTvNumberOfComments: TextView? = null
    private var mTvNumberOfLikesTextId: Int? = null
    private var mTvNumberOfCommentsTextId: Int? = null
    private var mTvDistance: TextView? = null
    private var mTvDistanceTextId: Int? = null

    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val view = inflate(context, R.layout.custom_likebar, this)

        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)

        mTvNumberOfLikes = view.findViewById(R.id.tvNumberOfLikes)
        mTvNumberOfComments = view.findViewById(R.id.tvNumberOfComments)


        attrs?.let {
            val typedArray =
                context.theme.obtainStyledAttributes(attrs, R.styleable.CustomLikeBar, 0, 0)
            try {
                mTvNumberOfLikesTextId =
                    typedArray.getResourceId(R.styleable.CustomLikeBar_customLikebarLikesText, 1)
                mTvNumberOfLikesTextId?.let { mTvNumberOfLikes?.text = "777" }

                mTvNumberOfCommentsTextId =
                    typedArray.getResourceId(R.styleable.CustomLikeBar_customLikebarCommentsText, 1)
                mTvNumberOfCommentsTextId?.let { mTvNumberOfComments?.text = "150" }
            } catch (e: Exception) {

            } finally {
                typedArray.recycle()
            }
        }
    }
}