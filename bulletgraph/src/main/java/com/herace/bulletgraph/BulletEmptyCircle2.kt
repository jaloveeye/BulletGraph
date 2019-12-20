package com.herace.bulletgraph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import java.lang.Exception

/**
 * @author Herace(jaloveeye@gmail.com)
 * Class: BulletEmptyCircle2
 * Created by Herace on 2019/12/20.
 * Description:
 */
class BulletEmptyCircle2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : BulletGraph(context, attrs, defStyleAttr)
{
    private var paintCyan = Paint()
    private var paintLightRed = Paint()

    private var graphRect: RectF = RectF(0F, 0F, 0F, 0F)

    var value3: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }


    var comment: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }

    var start: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }

    var end: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }


    val labelTopPaint: Paint
    val labelBottomPaint: Paint

    init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletEmptyCircle2, 0, 0)

        try {
            title = attributeSet.getString(R.styleable.BulletGraph_title)
            if (title.isNullOrBlank()) title = null

            value3 = attributeSet.getString(R.styleable.BulletEmptyCircle2_value3)
            if (value3.isNullOrBlank()) value3 = ""

            start = attributeSet.getString(R.styleable.BulletEmptyCircle2_start)
            if (start.isNullOrBlank()) start = ""

            end = attributeSet.getString(R.styleable.BulletEmptyCircle2_end)
            if (end.isNullOrBlank()) end = ""


            comment = attributeSet.getString(R.styleable.BulletEmptyCircle_comment)
            if (comment.isNullOrBlank()) comment = null

            paintCyan.color = getResourceIdToColor(R.color.colorBulletCyan)
            paintLightRed.color = getResourceIdToColor(R.color.colorBulletLightRed)

            paints.remove(paintYellow)
            paints.remove(paintOrange)
            paints.remove(paintDarkOragne)
            paints.remove(paintRed)
            paints.remove(paintDarkRed)

            paints.add(paintCyan)
            paints.add(paintLightRed)

            labelTopPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = labelSize
                    textAlign = Paint.Align.RIGHT
                    typeface = Typeface.DEFAULT
                }

            labelBottomPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = labelSize
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.DEFAULT
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
            var graphTop = 0.3f
            val top = mHEIGHT.toFloat() * graphTop
            val bottom = top + resources.getDimension(R.dimen.graph_height)


            /**
             * Draw graph
             */
            val cornerRadius = 0F

            val graphWidth = mWIDTH - graphMargin * 2

            graphRect.set(0f + graphMargin, top, graphWidth + graphMargin, bottom)
            canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[0])

            val paint = paints[2]

            /**
             * Draw axis label
             */
            val labelMarginTop = resources.getDimension(R.dimen.label_margin_top)
            val labelBottom = bottom + labelSize + labelMarginTop

            val labelTopText = start
            val labelBottomText = end
            if (labelTopText != null) canvas?.drawText(
                labelTopText!!,
                graphMargin,
                labelBottom,
                labelBottomPaint
            )
            if (labelBottomText != null) canvas?.drawText(
                labelBottomText!!,
                mWIDTH - graphMargin,
                labelBottom,
                labelTopPaint
            )

            /**
             * Draw axis label
             */
            val labelTop = top - labelSize

            if (value3 != null) {
                val valueInt = value3!!.toDoubleOrNull()?.toInt()

                val startValue = start!!.toDouble().toInt()
                val endValue = end!!.toDouble().toInt()
                val rangeValue = endValue - startValue

                if (valueInt != null) {
                    graphRect.set(
                        0f + graphMargin,
                        top,
                        (graphWidth.toFloat()) * valueInt / rangeValue + graphMargin,
                        bottom
                    )
                    canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paint)
                    val labelValueText = "${value3}"
                    val labelValueX =
                        graphMargin + graphWidth.toFloat() * valueInt / rangeValue - getTextWidth(
                            labelPaint,
                            labelValueText,
                            boundRect
                        ) / 2
                    canvas?.drawText(labelValueText, labelValueX, labelTop, labelBottomPaint)
                    val temp = getTextWidth(labelPaint, labelValueText, boundRect) / 2
                    drawCircle(canvas, labelValueX + temp, top, bottom, R.color.colorWarning)
                }
            }
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
            paint2.color = getResourceIdToColor(color)
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