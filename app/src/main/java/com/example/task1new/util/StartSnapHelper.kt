package com.example.task1new.util

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

class StartSnapHelper : LinearSnapHelper() {

    private var mVerticalHelper: OrientationHelper? = null
    private var mHorizontalHelper: OrientationHelper? = null

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray {
        val out = IntArray(2)

        if(layoutManager.canScrollHorizontally()){
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }

        if(layoutManager.canScrollVertically()){
            out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }
        return out
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        if(layoutManager is LinearLayoutManager){
            if(layoutManager.canScrollHorizontally()){
                return getStartView(layoutManager, getHorizontalHelper(layoutManager))
            } else {
                return getStartView(layoutManager, getVerticalHelper(layoutManager))
            }
        }
        return super.findSnapView(layoutManager)
    }

    private fun getStartView(layoutManager: RecyclerView.LayoutManager, helper:OrientationHelper): View?{
        if (layoutManager is LinearLayoutManager){
            val firstChild = layoutManager.findFirstCompletelyVisibleItemPosition()
            val isLastItem = layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1
            if(firstChild == RecyclerView.NO_POSITION || isLastItem){
                return null
            }

            val child = layoutManager.findViewByPosition(firstChild)

            if(helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2 && helper.getDecoratedEnd(child) > 0){
                return child
            } else {
                if(layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.itemCount - 1){
                    return null
                } else {
                    return layoutManager.findViewByPosition(firstChild + 1)
                }
            }
        }
        return super.findSnapView(layoutManager)
    }

    private fun getHorizontalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper{
        mHorizontalHelper?.let { return it }
        mHorizontalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        // TODO: 31.08.2021 Dodge (!!)
        return mHorizontalHelper!!
    }

    private fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper{
        mVerticalHelper?.let { return it }
        mVerticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        // TODO: 31.08.2021 Dodge (!!)
        return mVerticalHelper!!
    }

    private fun distanceToStart(targetView: View, helper: OrientationHelper): Int{
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding
    }
}