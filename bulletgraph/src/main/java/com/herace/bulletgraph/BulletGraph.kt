package com.herace.bulletgraph

import android.util.AttributeSet
import android.view.View
import android.content.Context
import android.graphics.*
import android.os.Build.VERSION.SDK_INT

/**
 * @author Herace(jaloveeye@gmail.com)
 * Class: BulletGraph
 * Created by Herace on 2019/11/25.
 * Description:
 */

open class BulletGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr)
{
    var paints: MutableList<Paint> = mutableListOf()
    var paintWhite = Paint()
    var paintGray = Paint()
    var paintYellow = Paint()
    var paintOrange = Paint()
    var paintDarkOragne = Paint()
    var paintRed = Paint()
    var paintDarkRed = Paint()
    var paintBlue = Paint()

    var bgPaint = Paint()

    var minWidth = 200
    var minHeight = 100

    var numberOfFields = 6
        set(value) {
            field = value
            invalidate()
        }

    var bgColor: String? = null
    var isWarning: Boolean = false
    var title: String? = ""
    var value: Int = 0
    var isASC: Boolean = false
    var labelA: Array<CharSequence>? = null
    var labelB: Array<CharSequence>? = null
    var range: Array<CharSequence>? = null

    var markerRect: Rect = Rect(0, 0, 0, 0)

    val labelSize = 40f
    val labelPaint: Paint

    val titleSize = 40f
    var titlePaint: Paint

    var boundRect = Rect(0, 0, 0, 0)

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
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.DEFAULT
                }

            bgPaint.color = Color.parseColor(bgColor)

        } finally {
            attributeSet.recycle()
        }
    }

    fun getResourceIdToColor(resourceId: Int): Int {
        if (SDK_INT >= 23) return resources.getColor(resourceId, null)
        else return resources.getColor(resourceId)
    }

    open fun setMarkerRect(widthTemp:Int, markerX: Float, target: Int, ratio: Float, top: Int, graphMargin: Int)  {
        val x = (markerX + target * ratio + graphMargin).toInt() - widthTemp/2
        val y = top - widthTemp
        val w = widthTemp + x
        val h = widthTemp + y

        markerRect.set(x, y, w, h)
    }

    open fun getTextWidth(paint: Paint, text: String, rect: Rect) : Int {
        paint.getTextBounds(text, 0, text.length, rect)

        return rect.width()
    }
}