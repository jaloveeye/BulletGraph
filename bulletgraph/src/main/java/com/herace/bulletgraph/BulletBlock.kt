package com.herace.bulletgraph

import android.util.AttributeSet
import android.content.Context
import android.graphics.*

/**
 * @author Herace(jaloveeye@gmail.com)
 * Class: BulletBlock
 * Created by Herace on 2019/11/26.
 * Description:
 */
class BulletBlock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : BulletGraph(context, attrs, defStyleAttr)
{
    /**
     * make image to marker
     */
    private val markerRed = BitmapFactory.decodeResource(context.resources, R.drawable.mark_red)
    private val markerBlue = BitmapFactory.decodeResource(context.resources, R.drawable.mark_blue)
   init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletGraph, 0, 0)

        try {
        } finally {
            attributeSet.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        /**
         * Set min width & min height
         */
        var mWIDTH = minWidth
        var mHEIGHT = minHeight
        if (width > minWidth) mWIDTH = width
        if (height > minHeight) mHEIGHT = height


        /**
         * Draw Background Color
         */
        canvas?.drawRect(0.0F, 0.0f, mWIDTH.toFloat(), mHEIGHT.toFloat(), bgPaint)


        /**
         * Set Top & Bottom to Graph
         */
        val top = mHEIGHT.toFloat() * 0.4f
        val bottom = mHEIGHT.toFloat() * 0.6f


        /**
         * Number of Fields to Graph
         */
        val graphMargin = 80f
        val number:Int = numberOfFields
        val ratio: Float = (mWIDTH.toFloat() -  graphMargin * 2) / number.toFloat()


        /**
         * Draw block graph
         */
        for (i in 0 until number) canvas?.drawRect(i * ratio + graphMargin , top, ( i + 1) * ratio + graphMargin, bottom, paints[i])


        /**
         * Draw Title Text
         */
        canvas?.drawText(title!!, graphMargin, titleSize + 20f, titlePaint)


        /**
         * Draw axis label
         */
        val LabelABottom = bottom + labelSize + 10f
        val LabelBBottom = bottom + labelSize * 2 + 10f

        for (i in 0 until number.plus(1)) {
            canvas?.drawText(
                labelA?.get(i).toString(),
                i * ratio + graphMargin - getTextWidth(labelPaint, labelA?.get(i).toString(), boundRect) / 2,
                LabelABottom,
                labelPaint)

            canvas?.drawText(
                labelB?.get(i).toString(),
                i * ratio + graphMargin - getTextWidth(labelPaint, labelB?.get(i).toString(), boundRect) / 2,
                LabelBBottom,
                labelPaint)
        }

        /**
         * Calculation to x axis coordinates to marker
         */
        var target = 0
        var ratioValue = 0f
        for (i in 0 until range?.size?.minus(1)!!) {
            if (isASC) {
                if (value >= Integer.parseInt(range?.get(i).toString()) && value <  Integer.parseInt(range?.get(i + 1).toString())) {
                    target = i
                    val startVal =  Integer.parseInt(range?.get(i + 1).toString()).toFloat()
                    val endVal = Integer.parseInt(range?.get(i).toString()).toFloat()
                    ratioValue = (value.toFloat() - endVal) / (startVal - endVal)
                    break
                }
            } else {
                if (value < Integer.parseInt(range?.get(i).toString()) && value >=  Integer.parseInt(range?.get(i + 1).toString())) {
                    target = i
                    val startVal =  Integer.parseInt(range?.get(i).toString()).toFloat()
                    val endVal = Integer.parseInt(range?.get(i + 1).toString()).toFloat()
                    ratioValue = 1F - (value.toFloat() - endVal) / (startVal - endVal)
                    break
                }
            }
        }

        val markerX = ratio * ratioValue
        val widthTemp = mWIDTH / 22
        setMarkerRect(widthTemp, markerX, target, ratio, top.toInt(), graphMargin.toInt())
        if (isWarning) canvas?.drawBitmap(markerRed, null, markerRect, null)
        else canvas?.drawBitmap(markerBlue, null, markerRect, null)
    }
}