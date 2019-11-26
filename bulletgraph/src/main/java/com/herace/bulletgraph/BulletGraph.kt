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
open class BulletGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr)
{
    private var paints: MutableList<Paint> = mutableListOf()
    private var paintWhite = Paint()
    private var paintGray = Paint()
    private var paintYellow = Paint()
    private var paintOrange = Paint()
    private var paintDarkOragne = Paint()
    private var paintRed = Paint()
    private var paintDarkRed = Paint()
    private var paintBlue = Paint()
    private var bgPaint = Paint()

    private var minWidth = 200
    private var minHeight = 100

    private var numberOfFields = 6
        set(value) {
            field = value
            invalidate()
        }

    private var bgColor: String? = null
    private var isWarning: Boolean = false
    private var title: String? = ""
    private var value: Int = 0
    private var isASC: Boolean = false
    private var labelA: Array<CharSequence>? = null
    private var labelB: Array<CharSequence>? = null
    private var range: Array<CharSequence>? = null

    /**
     * make image to marker
     */
    private val markerRed = BitmapFactory.decodeResource(context.resources, R.drawable.mark_red)
    private val markerBlue = BitmapFactory.decodeResource(context.resources, R.drawable.mark_blue)

    private var markerRect: Rect = Rect(0, 0, 0, 0)

    private val labelSize = 40f
    private val labelPaint: Paint

    private val titleSize = 40f
    private var titlePaint: Paint


    init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletGraph, 0, 0)

        try {
            numberOfFields = attributeSet.getInt(R.styleable.BulletGraph_noOfFields, numberOfFields)
            bgColor = attributeSet.getString(R.styleable.BulletGraph_bgColor)
            isWarning = attributeSet.getBoolean(R.styleable.BulletGraph_isWarning, isWarning)
            title = attributeSet.getString(R.styleable.BulletGraph_title)
            if (title.isNullOrBlank()) title = ""
            value = attributeSet.getInt(R.styleable.BulletGraph_value, value)
            isASC = attributeSet.getBoolean(R.styleable.BulletGraph_isASC, isASC)

            labelA = attributeSet.getTextArray(R.styleable.BulletGraph_label_1)
            labelB = attributeSet.getTextArray(R.styleable.BulletGraph_label_2)
            range = attributeSet.getTextArray(R.styleable.BulletGraph_range)

            /**
             * Setting Paint
             */
            paintWhite.color = getResourceIdToColor(R.color.colorBulletWhite)
            paintGray.color = getResourceIdToColor(R.color.colorBulletGray)
            paintYellow.color = getResourceIdToColor(R.color.colorBulletYellow)
            paintOrange.color = getResourceIdToColor(R.color.colorBulletOrange)
            paintDarkOragne.color = getResourceIdToColor(R.color.colorBulletDarkOrage)
            paintRed.color = getResourceIdToColor(R.color.colorBulletRed)
            paintDarkRed.color = getResourceIdToColor(R.color.colorBulletDarkRed)
            paintBlue.color = getResourceIdToColor(R.color.colorBulletBlue)

            paints.add(paintGray)
            paints.add(paintYellow)
            paints.add(paintOrange)
            paints.add(paintDarkOragne)
            paints.add(paintRed)
            paints.add(paintDarkRed)

            var titleColor = getResourceIdToColor(R.color.colorBulletBlack)
            if (isWarning) titleColor = getResourceIdToColor(R.color.colorBulletRed)

            titlePaint =
                Paint().apply {
                    isAntiAlias = true
                    color = titleColor
                    style = Paint.Style.FILL
                    textSize = titleSize
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.DEFAULT
                }

            labelPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = labelSize
                    textAlign = Paint.Align.CENTER
                    typeface = Typeface.DEFAULT
                }

            bgPaint.color = Color.parseColor(bgColor)

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
        val number:Int = numberOfFields
        val ratio: Float = mWIDTH.toFloat() / (number + 1).toFloat()


        /**
         * Draw block graph
         */
        for (i in 0 until number) canvas?.drawRect(i * ratio + ratio / 2f , top, ( i + 1) * ratio + ratio / 2f, bottom, paints[i])


        /**
         * Draw Title Text
         */
        canvas?.drawText(title!!, ratio / 2f, titleSize + 20f, titlePaint)


        /**
         * Draw axis label
         */
        val LabelABottom = bottom + labelSize + 10f
        val LabelBBottom = bottom + labelSize * 2 + 10f

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
        val widthTemp = mWIDTH / 22
        setMarkerRect(widthTemp, markerX, target, ratio, top.toInt())
        if (isWarning) canvas?.drawBitmap(markerRed, null, markerRect, null)
        else canvas?.drawBitmap(markerBlue, null, markerRect, null)
    }


    private fun getResourceIdToColor(resourceId: Int): Int {
        if (SDK_INT >= 23) return resources.getColor(resourceId, null)
        else return resources.getColor(resourceId)
    }


    private fun setMarkerRect(widthTemp:Int, markerX: Float, target: Int, ratio: Float, top: Int)  {
        val x = (markerX + target * ratio + ratio / 2f).toInt() - widthTemp/2
        val y = top - widthTemp
        val w = widthTemp + x
        val h = widthTemp + y

        markerRect.set(x, y, w, h)
    }
}