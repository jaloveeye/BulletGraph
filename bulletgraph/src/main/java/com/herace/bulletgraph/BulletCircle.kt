package com.herace.bulletgraph

import android.util.AttributeSet
import android.content.Context
import android.graphics.*
import android.util.Log
import java.lang.Exception

/**
 * @author Herace(jaloveeye@gmail.com)
 * Class: BulletCircle
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

    var subTitle: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }

    /**
     * make image to marker
     */
    private val markerRed = BitmapFactory.decodeResource(context.resources, R.drawable.marker_circle_red)
    private val markerBlue = BitmapFactory.decodeResource(context.resources, R.drawable.marker_circle_cyan)

    private var graphRect: RectF = RectF(0F, 0F, 0F, 0F)

    private val subTitleSize = resources.getDimension(R.dimen.title_text_size)
    private lateinit var subTitlePaint: Paint

    val labelStartPaint: Paint
    val labelEndPaint: Paint

    init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletCircle, 0, 0)

        try {
            subTitle = attributeSet.getString(R.styleable.BulletCircle_subTitle)
            if (subTitle.isNullOrBlank()) subTitle = ""

            paintCyan.color = getResourceIdToColor(R.color.colorNormal)
            paintLightRed.color = getResourceIdToColor(R.color.colorWarning)

            paints.remove(paintYellow)
            paints.remove(paintOrange)
            paints.remove(paintDarkOragne)
            paints.remove(paintRed)
            paints.remove(paintDarkRed)

            paints.add(paintCyan)
            paints.add(paintLightRed)


            labelStartPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = resources.getDimension(R.dimen.label_text_size)
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.DEFAULT
                }

            labelEndPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = resources.getDimension(R.dimen.label_text_size)
                    textAlign = Paint.Align.RIGHT
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
            if (height < minHeight) mHEIGHT = minHeight


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
             * Number of Fields to Graph
             */
            val number: Int = numberOfFields
            val ratio: Float = (mWIDTH.toFloat() - graphMargin * 2) / number.toFloat()


            /**
             * Draw graph
             * 처음, 마지막 먼저 그리고 가운데를 그려야 함.
             */
            val cornerRadius = 0F

            graphRect.set(graphMargin, top, ratio + graphMargin + cornerRadius, bottom)
            canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[0])

            graphRect.set(
                (number - 1) * ratio + graphMargin - cornerRadius,
                top,
                (number) * ratio + graphMargin,
                bottom
            )
            canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[number - 1])

            for (i in 1 until number.minus(1)) {
                graphRect.set(i * ratio + graphMargin, top, (i + 1) * ratio + graphMargin, bottom)
                canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[i])
            }


            /**
             * Draw Title Text
             */
            var titleColor = getResourceIdToColor(R.color.colorTextBlack)
            if (warning!!.toBoolean()) titleColor = getResourceIdToColor(R.color.colorWarning)
            titlePaint =
                Paint().apply {
                    isAntiAlias = true
                    color = titleColor
                    style = Paint.Style.FILL
                    textSize = titleSize
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.DEFAULT_BOLD
                }
            subTitlePaint =
                Paint().apply {
                    isAntiAlias = true
                    color = titleColor
                    style = Paint.Style.FILL
                    textSize = resources.getDimension(R.dimen.title_text_size)
                    textAlign = Paint.Align.LEFT
                    typeface = Typeface.DEFAULT
                }
            val titleMarginTop = resources.getDimension(R.dimen.title_margin_top)
            canvas?.drawText(title!!, graphMargin, titleSize + titleMarginTop, titlePaint)
            canvas?.drawText(
                subTitle!!,
                graphMargin,
                titleSize + subTitleSize + titleMarginTop * 2,
                subTitlePaint
            )


            /**
             * Draw axis label
             */
            val labelMarginTop = resources.getDimension(R.dimen.label_margin_top)
            val LabelABottom = bottom + labelSize + labelMarginTop
            val LabelBBottom = bottom + labelSize * 2 + labelMarginTop

            canvas?.drawText(labelA?.get(0).toString(), graphMargin, LabelABottom, labelStartPaint)
            canvas?.drawText(labelB?.get(0).toString(), graphMargin, LabelBBottom, labelStartPaint)

            canvas?.drawText(
                labelA?.get(number).toString(),
                number * ratio + graphMargin,
                LabelABottom,
                labelEndPaint
            )
            canvas?.drawText(
                labelB?.get(number).toString(),
                number * ratio + graphMargin,
                LabelBBottom,
                labelEndPaint
            )

            for (i in 1 until number) {
                canvas?.drawText(
                    labelA?.get(i).toString(),
                    i * ratio + graphMargin - getTextWidth(
                        labelPaint,
                        labelA?.get(i).toString(),
                        boundRect
                    ) / 2,
                    LabelABottom,
                    labelPaint
                )
                canvas?.drawText(
                    labelB?.get(i).toString(),
                    i * ratio + graphMargin - getTextWidth(
                        labelPaint,
                        labelB?.get(i).toString(),
                        boundRect
                    ) / 2,
                    LabelBBottom,
                    labelPaint
                )
            }

            /**
             * Calculation to x axis coordinates to marker
             */
            var target = 0
            var ratioValue = 0f
            var markerColor = R.color.colorNormal
            for (i in 0 until range?.size?.minus(1)!!) {
                if (isASC) {
                    if (value >= Integer.parseInt(range?.get(i).toString()) && value <= Integer.parseInt(
                            range?.get(i + 1).toString()
                        )
                    ) {
                        target = i
                        val startVal = Integer.parseInt(range?.get(i + 1).toString()).toFloat()
                        val endVal = Integer.parseInt(range?.get(i).toString()).toFloat()
                        ratioValue = (value.toFloat() - endVal) / (startVal - endVal)
                        markerColor = paints[i].color
                        break
                    }
                } else {
                    if (value <= Integer.parseInt(range?.get(i).toString()) && value >= Integer.parseInt(
                            range?.get(i + 1).toString()
                        )
                    ) {
                        target = i
                        val startVal = Integer.parseInt(range?.get(i).toString()).toFloat()
                        val endVal = Integer.parseInt(range?.get(i + 1).toString()).toFloat()
                        ratioValue = 1F - (value.toFloat() - endVal) / (startVal - endVal)
                        markerColor = paints[i].color
                        break
                    }
                }
            }

            val markerX = ratio * ratioValue
            drawCircle(
                canvas,
                markerX,
                target,
                ratio,
                top,
                bottom,
                graphMargin.toInt(),
                markerColor
            )
//        if (warning!!.toBoolean()) drawCircle(canvas, markerX, target, ratio, top, bottom, graphMargin.toInt(), R.color.colorWarning)
//        else drawCircle(canvas, markerX, target, ratio, top, bottom, graphMargin.toInt(), R.color.colorNormal)
        } catch (e: Exception) {
            Log.e("BulletGraph", e.message)
        }
    }

    private fun drawCircle(canvas: Canvas?, markerX: Float, target: Int, ratio: Float, top: Float, bottom: Float, graphMargin: Int, color: Int) {

        try {
            val x = markerX + target * ratio + graphMargin
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
//        paint2.color = getResourceIdToColor(color)
            paint2.color = color
            paint2.strokeWidth = resources.getDimension(R.dimen.graph_height)
            paint2.style = Paint.Style.STROKE
            paint2.isAntiAlias = true
            paint2.isDither = true
            canvas?.drawCircle(x, y, radius2, paint2)
        } catch (e: Exception) {
            Log.e("BulletGraph", e.message)
        }
    }
}