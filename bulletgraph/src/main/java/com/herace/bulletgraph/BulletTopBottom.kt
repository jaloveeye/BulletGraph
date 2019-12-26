package com.herace.bulletgraph

import android.util.AttributeSet
import android.content.Context
import android.graphics.*
import android.util.Log
import java.lang.Exception

/**
 * @author Herace(jaloveeye@gmail.com)
 * Class: BulletTopBottom
 * Created by Herace on 2019/11/27.
 * Description:
 */
class BulletTopBottom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : BulletGraph(context, attrs, defStyleAttr)
{
    private var graphRect: RectF = RectF(0F, 0F, 0F, 0F)

    var topbottom: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }

    var bt_graph_color: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }

    var bt_text_color: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }


    val labelTopPaint: Paint
    val labelBottomPaint: Paint

    init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletTopBottom, 0, 0)

        try {
            topbottom = attributeSet.getString(R.styleable.BulletTopBottom_topbottom)
            if (topbottom.isNullOrBlank()) topbottom = "false"

            bt_graph_color = attributeSet.getString(R.styleable.BulletTopBottom_bt_graph_color)
            bt_text_color = attributeSet.getString(R.styleable.BulletTopBottom_bt_text_color)

            val labelTypeface = Typeface.createFromAsset(getContext().assets, "font/notosanscjkkrlight.otf")
            labelTopPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = labelSize
                    textAlign = Paint.Align.RIGHT
                    typeface = labelTypeface
                }

            labelBottomPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = labelSize
                    textAlign = Paint.Align.LEFT
                    typeface = labelTypeface
                }
        } finally {
            attributeSet.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        try {
            /**
             * Set min width & min height
             */
            var mWIDTH = width
            var mHEIGHT = height
            if (width < minWidth) mWIDTH = minWidth
            if (height < 100) mHEIGHT = minHeight


            /**
             * Draw Background Color
             */
            canvas?.drawRect(0.0F, 0.0f, mWIDTH.toFloat(), mHEIGHT.toFloat(), bgPaint)


            /**
             * Set Top & Bottom to Graph
             */
            val top = mHEIGHT.toFloat() * 0.5f
            val bottom = top + resources.getDimension(R.dimen.graph_height)


            /**
             * Draw graph
             */
            val cornerRadius = 0F

            val graphWidth = mWIDTH - graphMargin * 2

            graphRect.set(0f + graphMargin, top, graphWidth + graphMargin, bottom)
            canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[0])

            paints[1].color = Color.parseColor(bt_graph_color)
            //val paint = if (topbottom!!.toBoolean()) paints[1] else paints[2]

            if (topbottom!!.toBoolean()) graphRect.set(
                graphWidth - (graphWidth.toFloat() * value / 100) + graphMargin,
                top,
                graphWidth + graphMargin,
                bottom
            )
            else graphRect.set(
                0f + graphMargin,
                top,
                (graphWidth.toFloat()) * value / 100 + graphMargin,
                bottom
            )
            canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[1])


            /**
             * Draw Title Text
             */
            val titleTypeface = Typeface.createFromAsset(getContext().assets, "font/notosanscjkkrbold.otf")
            titlePaint =
                Paint().apply {
                    isAntiAlias = true
                    color = Color.parseColor(bt_text_color)
                    style = Paint.Style.FILL
                    textSize = titleSize
                    textAlign = Paint.Align.LEFT
                    typeface = titleTypeface
                }
            val titleMarginTop = resources.getDimension(R.dimen.title_margin_top)
            canvas?.drawText(title!!, graphMargin, titleSize + titleMarginTop, titlePaint)


            /**
             * Draw axis label
             */
            val labelMarginTop = resources.getDimension(R.dimen.label_margin_top)
            val labelBottom = bottom + labelSize + labelMarginTop

            val labelTopText = "상위"
            val labelBottomText = "하위"
            canvas?.drawText(labelBottomText, graphMargin, labelBottom, labelBottomPaint)
            canvas?.drawText(labelTopText, mWIDTH - graphMargin, labelBottom, labelTopPaint)

            val labelTop = top - labelSize
            val labelValueText = "${value}%"
            val labelValueX =
                if (topbottom!!.toBoolean()) graphMargin + graphWidth.toFloat() * (100f - value) / 100 - getTextWidth(
                    labelPaint,
                    labelValueText,
                    boundRect
                ) / 2
                else graphMargin + graphWidth.toFloat() * value / 100 - getTextWidth(
                    labelPaint,
                    labelValueText,
                    boundRect
                ) / 2
            canvas?.drawText(labelValueText, labelValueX, labelTop, labelBottomPaint)

            val temp = getTextWidth(labelPaint, labelValueText, boundRect) / 2
            drawCircle(canvas, labelValueX + temp, top, bottom,  paints[1].color)
        } catch (e: Exception) {
            Log.e("Bullet Graph", e.message)
        }
    }

    private fun drawCircle(canvas: Canvas?, markerX: Float, top: Float, bottom: Float, color: Int) {
        try {
            val x = markerX
            val y = top + (bottom - top) / 2

            val radius = resources.getDimension(R.dimen.graph_height)
            val radius2 = resources.getDimension(R.dimen.circle_radius)

            val paint1 = Paint()
            paint1.color = getResourceIdToColor(R.color.colorBulletWhite)
            paint1.strokeWidth = resources.getDimension(R.dimen.graph_height)
            paint1.style = Paint.Style.FILL
            paint1.isAntiAlias = true
            paint1.isDither = true
            canvas?.drawCircle(x, y, radius, paint1)

            val paint2 = Paint()
            paint2.color = color
            paint2.strokeWidth = resources.getDimension(R.dimen.graph_height)
            paint2.style = Paint.Style.STROKE
            paint2.isAntiAlias = true
            paint2.isDither = true
            canvas?.drawCircle(x, y, radius2, paint2)
        } catch (e: Exception) {
            Log.e("Bullet Graph", e.message)
        }
    }
}