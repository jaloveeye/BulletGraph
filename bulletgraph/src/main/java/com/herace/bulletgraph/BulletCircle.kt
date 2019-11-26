package com.herace.bulletgraph

import android.util.AttributeSet
import android.view.View
import android.content.Context
import android.graphics.*
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.core.view.marginTop
import java.lang.Exception

/**
 * @author Herace(jaloveeye@gmail.com)
 * Class: BulletGraph
 * Created by Herace on 2019/11/26.
 * Description:
 */
class BulletCircle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : BulletGraph(context, attrs, defStyleAttr)
{
    private var paintCyan = Paint()
    private var paintLightRed = Paint()

    private var subTitle: String? = ""

    /**
     * make image to marker
     */
    private val markerRed = BitmapFactory.decodeResource(context.resources, R.drawable.marker_circle_red)
    private val markerBlue = BitmapFactory.decodeResource(context.resources, R.drawable.marker_circle_cyan)

    private var graphRect: RectF = RectF(0F, 0F, 0F, 0F)

    private val subTitleSize = 40f
    private var subTitlePaint: Paint

    init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletCircle, 0, 0)

        try {
            subTitle = attributeSet.getString(R.styleable.BulletCircle_subTitle)
            if (subTitle.isNullOrBlank()) subTitle = ""

            paintCyan.color = getResourceIdToColor(R.color.colorBulletCyan)
            paintLightRed.color = getResourceIdToColor(R.color.colorBulletLightRed)

            paints.remove(paintYellow)
            paints.remove(paintOrange)
            paints.remove(paintDarkOragne)
            paints.remove(paintRed)
            paints.remove(paintDarkRed)

            paints.add(paintCyan)
            paints.add(paintLightRed)

            var titleColor = getResourceIdToColor(R.color.colorBulletGray)
            if (isWarning) titleColor = getResourceIdToColor(R.color.colorBulletLightRed)

            titlePaint =
                Paint().apply {
                    isAntiAlias = true
                    color = titleColor
                    style = Paint.Style.FILL
                    textSize = titleSize
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.DEFAULT
                }

            subTitlePaint =
                Paint().apply {
                    isAntiAlias = true
                    color = titleColor
                    style = Paint.Style.FILL
                    textSize = subTitleSize
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.DEFAULT
                }
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
        val top = mHEIGHT.toFloat() * 0.5f
//        val bottom = mHEIGHT.toFloat() * 0.6f
        val bottom = top + 20f


        /**
         * Number of Fields to Graph
         */
        val number:Int = numberOfFields
        val ratio: Float = mWIDTH.toFloat() / (number + 1).toFloat()


        /**
         * Draw graph
         * 처음, 마지막 먼저 그리고 가운데를 그려야 함.
         */
        val cornerRadius = 25F

        graphRect.set(ratio / 2f, top, ratio + ratio / 2f + cornerRadius, bottom)
        canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[0])

        graphRect.set((number - 1) * ratio + ratio / 2f - cornerRadius, top, ( number ) * ratio + ratio / 2f, bottom)
        canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[number - 1])

        for (i in 1 until number.minus(1)) {
            graphRect.set(i * ratio + ratio / 2f, top, ( i + 1) * ratio + ratio / 2f, bottom)
            canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[i])
        }


        /**
         * Draw Title Text
         */
        val titleMarginTop = 20f
        canvas?.drawText(title!!, ratio / 2f, titleSize + titleMarginTop, titlePaint)
        canvas?.drawText(subTitle!!, ratio / 2f, titleSize + subTitleSize + titleMarginTop * 2, subTitlePaint)


        /**
         * Draw axis label
         */
        val labelMarginTop = 40f
        val LabelABottom = bottom + labelSize + labelMarginTop
        val LabelBBottom = bottom + labelSize * 2 + labelMarginTop

        for (i in 0 until number.plus(1)) {
            canvas?.drawText(labelA?.get(i).toString(), i * ratio + ratio / 2f, LabelABottom, labelPaint)
            canvas?.drawText(labelB?.get(i).toString(), i * ratio + ratio / 2f, LabelBBottom, labelPaint)
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
//        val widthTemp = mWIDTH / 15
        val widthTemp = 60
        setMarkerRect(widthTemp, markerX, target, ratio, top.toInt())
        if (isWarning) canvas?.drawBitmap(markerRed, null, markerRect, null)
        else canvas?.drawBitmap(markerBlue, null, markerRect, null)
    }


    override fun setMarkerRect(widthTemp:Int, markerX: Float, target: Int, ratio: Float, top: Int)  {
        val x = (markerX + target * ratio + ratio / 2f).toInt() - widthTemp/2
        val y = top - widthTemp / 2 + 10 // (bottom - top)/2 ???
        val w = widthTemp + x
        val h = widthTemp + y

        markerRect.set(x, y, w, h)
    }
}