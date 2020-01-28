package com.herace.bulletgraph

import android.util.AttributeSet
import android.view.View
import android.content.Context
import android.graphics.*
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import java.lang.Exception

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
    var colors: MutableList<String> = mutableListOf()

    var paints: MutableList<Paint> = mutableListOf()
    var paintWhite = Paint()
    var paintGray = Paint()
    var paintYellow = Paint()
    var paintOrange = Paint()
    var paintDarkOragne = Paint()
    var paintRed = Paint()
    var paintDarkRed = Paint()
    var paintBlue = Paint()

    var paintColor = Paint()

    var bgPaint = Paint()

    var minWidth = 200
    var minHeight = 80

    var numberOfFields = 0

    /**
     * 변화 가능한  attrs
     */
    var title: String? = ""
        set(value) {
            field = value
            invalidate()
        }

    var label_1_text: String? = ""
        set(value) {
            field = value
            invalidate()
        }
    var label_1_textArray: MutableList<String> = mutableListOf()

    var label_2_text: String? = ""
        set(value) {
            field = value
            invalidate()
        }
    var label_2_textArray: MutableList<String> = mutableListOf()

    var label_3_text: String? = ""
        set(value) {
            field = value
            invalidate()
        }
    var label_3_textArray: MutableList<String> = mutableListOf()

    var text_color: String? = ""
        set(value) {
            field = value
            invalidate()
        }
    var text_color_Array: MutableList<String> = mutableListOf()

    var graph_color: String? = ""
        set(value) {
            field = value
            invalidate()
        }
    var graph_color_Array: MutableList<String> = mutableListOf()

    var graph_range: String? = ""
        set(value) {
            field = value
            invalidate()
        }
    var graph_range_Array: MutableList<String> = mutableListOf()

    var value = 0
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }

//    var isWarning: Boolean = false
//        set(value) {
//            field = value
//            invalidate()
//        }

    var warning: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }

    var isASC: Boolean = true
        set(value) {
            field = value
            invalidate()
        }


    var bgColor: String? = null
    var labelA: Array<CharSequence>? = null
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }
    var labelB: Array<CharSequence>? = null


    var range: Array<CharSequence>? = null
    var colorArray : Array<CharSequence>? = null

    val titleSize = resources.getDimension(R.dimen.title_text_size)
    val labelSize = resources.getDimension(R.dimen.label_text_size)
    val lineHeight = resources.getDimension(R.dimen.label_text_line_height)

    var markerRect: Rect = Rect(0, 0, 0, 0)
    val labelPaint: Paint
    var titlePaint: Paint
    var boundRect = Rect(0, 0, 0, 0)

    val graphMargin = resources.getDimension(R.dimen.graph_margin)

    init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletGraph, 0, 0)

        try {

            // TODO : attributeSet 의 null 처리
            numberOfFields = attributeSet.getInt(R.styleable.BulletGraph_noOfFields, numberOfFields)

            bgColor = attributeSet.getString(R.styleable.BulletGraph_bgColor)
            if (bgColor.isNullOrBlank()) bgColor = "#FFFFFF"

            warning = attributeSet.getString(R.styleable.BulletGraph_warning)
            if (warning.isNullOrBlank()) warning = "false"

            title = attributeSet.getString(R.styleable.BulletGraph_title)
            if (title.isNullOrBlank()) title = ""

            label_1_text = attributeSet.getString(R.styleable.BulletGraph_label_1_text)
            if (label_1_text.isNullOrBlank()) label_1_text = ""
            label_1_textArray = label_1_text?.split("|")!!.toMutableList()

            label_2_text = attributeSet.getString(R.styleable.BulletGraph_label_2_text)
            if (label_2_text.isNullOrBlank()) label_2_text = ""
            label_2_textArray = label_2_text?.split("|")!!.toMutableList()

            label_3_text = attributeSet.getString(R.styleable.BulletGraph_label_3_text)
            if (label_3_text.isNullOrBlank()) label_3_text = ""
            label_3_textArray = label_3_text?.split("|")!!.toMutableList()

            text_color = attributeSet.getString(R.styleable.BulletGraph_text_color)
            if (text_color.isNullOrBlank()) text_color = ""
            text_color_Array = text_color?.split("|")!!.toMutableList()

            graph_color = attributeSet.getString(R.styleable.BulletGraph_graph_color)
            if (graph_color.isNullOrBlank()) graph_color = ""
            graph_color_Array = graph_color?.split("|")!!.toMutableList()

            graph_range = attributeSet.getString(R.styleable.BulletGraph_graph_range)
            if (graph_range.isNullOrBlank()) graph_range = ""
            graph_range_Array = graph_range?.split("|")!!.toMutableList()


            value = attributeSet.getInt(R.styleable.BulletGraph_value, value)
            isASC = attributeSet.getBoolean(R.styleable.BulletGraph_isASC, isASC)

            labelA = attributeSet.getTextArray(R.styleable.BulletGraph_label_1)
            labelB = attributeSet.getTextArray(R.styleable.BulletGraph_label_2)

            range = attributeSet.getTextArray(R.styleable.BulletGraph_range)
            colorArray = attributeSet.getTextArray(R.styleable.BulletGraph_colorArray)

            println("range ${range?.get(0)}")
            println("colorArray $colorArray")
            /**
             * Setting Paint
             */
            if(colorArray == null) {
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
            }else {
                for (i in 0 until numberOfFields) {
                    val paint = Paint()
                    paints.add(paint)
                }
            }


            var titleColor = getResourceIdToColor(R.color.colorTextBlack)
            if (warning!!.toBoolean()) titleColor = getResourceIdToColor(R.color.colorWarning)

            val titleTypeface = Typeface.createFromAsset(getContext().assets, "font/notosanscjkkrbold.otf")
            val labelTypeface = Typeface.createFromAsset(getContext().assets, "font/notosanscjkkrlight.otf")

            titlePaint =
                Paint().apply {
                    isAntiAlias = true
                    color = titleColor
                    style = Paint.Style.FILL
                    textSize = titleSize
                    textAlign = Paint.Align.LEFT
                    typeface = titleTypeface
                }

            labelPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = labelSize
                    textAlign = Paint.Align.LEFT
                    typeface = labelTypeface
                }

            bgPaint.color = Color.parseColor(bgColor)


        } finally {
            attributeSet.recycle()
        }
    }

    fun getResourceIdToColor(resourceId: Int): Int {
        if (SDK_INT >= 23) return resources.getColor(resourceId, null)
        else return ResourcesCompat.getColor(context.resources, resourceId, null)
    }

    open fun setMarkerRect(widthTemp:Int, markerX: Float, target: Int, ratio: Float, top: Int, graphMargin: Int)  {

        try {
            val x = (markerX + target * ratio + graphMargin).toInt() - widthTemp/2
            val y = top - widthTemp
            val w = widthTemp + x
            val h = widthTemp + y

            markerRect.set(x, y, w, h)
        } catch (e: Exception) {
            Log.e("BulletGraph", e.message.toString())
        }
    }

    open fun getTextWidth(paint: Paint, text: String, rect: Rect) : Int {
        paint.getTextBounds(text, 0, text.length, rect)

        return rect.width()
    }
}