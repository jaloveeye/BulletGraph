package com.herace.bulletgraph

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import androidx.core.graphics.toColor
import java.lang.Exception

/**
 * @author Herace(jaloveeye@gmail.com)
 * Class: BulletCircle2
 * Created by Herace on 2019/12/18.
 * Description:
 */
class BulletCircle2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : BulletGraph(context, attrs, defStyleAttr)
{
    var subTitle: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }

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

            val labelTypeface = Typeface.createFromAsset(getContext().assets, "font/notosanscjkkrlight.otf")
            labelStartPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = resources.getDimension(R.dimen.label_text_size)
                    textAlign = Paint.Align.LEFT
                    typeface = labelTypeface
                }

            labelEndPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = getResourceIdToColor(R.color.colorBulletBlack)
                    style = Paint.Style.FILL
                    textSize = resources.getDimension(R.dimen.label_text_size)
                    textAlign = Paint.Align.RIGHT
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

            if (text_color != null) {

                label_1_textArray = label_1_text?.split("|")!!.toMutableList()
                label_2_textArray = label_2_text?.split("|")!!.toMutableList()
                text_color_Array = text_color?.split("|")!!.toMutableList()
                graph_color_Array = graph_color?.split("|")!!.toMutableList()
                graph_range_Array = graph_range?.split("|")!!.toMutableList()


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
                val top = mHEIGHT.toFloat() * 0.65f
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

                paints[0].color = Color.parseColor(graph_color_Array[0])
                graphRect.set(graphMargin, top, ratio + graphMargin + cornerRadius, bottom)
                canvas?.drawRoundRect(
                    graphRect,
                    cornerRadius,
                    cornerRadius,
                    paints[0]
                )

                graphRect.set(
                    (number - 1) * ratio + graphMargin - cornerRadius,
                    top,
                    (number) * ratio + graphMargin,
                    bottom
                )

                paints[number - 1].color = Color.parseColor(graph_color_Array[number - 1])
                canvas?.drawRoundRect(
                    graphRect,
                    cornerRadius,
                    cornerRadius,
                    paints[number - 1]
                )

                for (i in 1 until number.minus(1)) {
                    graphRect.set(
                        i * ratio + graphMargin,
                        top,
                        (i + 1) * ratio + graphMargin,
                        bottom
                    )

                    paints[i].color = Color.parseColor(graph_color_Array[i])
                    canvas?.drawRoundRect(
                        graphRect,
                        cornerRadius,
                        cornerRadius,
                        paints[i]
                    )
                }


                /**
                 * Draw axis label
                 */
                val labelMarginTop = resources.getDimension(R.dimen.label_margin_top)
                val LabelABottom = bottom + labelSize + labelMarginTop


                for (i in 0 until number) {
                    canvas?.drawText(
                        label_1_textArray.get(i).toString(),
                        (i) * ratio + graphMargin + (ratio - getTextWidth(
                            labelPaint,
                            label_1_textArray.get(i).toString(),
                            boundRect
                        )) / 2,
                        LabelABottom,
                        labelPaint
                    )
                }

                for (i in 0 until number - 1) {
                    canvas?.drawText(
                        label_2_textArray.get(i).toString(),
                        (i + 1) * ratio + graphMargin - getTextWidth(
                            labelPaint,
                            labelB?.get(i).toString(),
                            boundRect
                        ) / 2,
                        top - labelSize,
                        labelPaint
                    )
                }

                /**
                 * Calculation to x axis coordinates to marker
                 */
                var target = 0
                var ratioValue = 0f
                var markerColor = R.color.colorNormal

                for (i in 0 until graph_range_Array.size.minus(1)) {
                    if (isASC) {
                        if (value >= Integer.parseInt(graph_range_Array.get(i)) && value < Integer.parseInt(
                                graph_range_Array.get(i + 1)
                            )
                        ) {
                            target = i
                            val startVal = Integer.parseInt(graph_range_Array.get(i + 1)).toFloat()
                            val endVal = Integer.parseInt(graph_range_Array.get(i)).toFloat()
                            ratioValue = (value.toFloat() - endVal) / (startVal - endVal)
                            markerColor =  paints[i].color
                            break
                        }
                    } else {
                        if (value <= Integer.parseInt(graph_range_Array.get(i)) && value > Integer.parseInt(
                                graph_range_Array.get(i + 1)
                            )
                        ) {
                            target = i
                            val startVal = Integer.parseInt(graph_range_Array.get(i)).toFloat()
                            val endVal = Integer.parseInt(graph_range_Array.get(i + 1)).toFloat()
                            ratioValue = 1F - (value.toFloat() - endVal) / (startVal - endVal)
                            markerColor =  paints[i].color
                            break
                        }
                    }
                }


                /**
                 * Draw Title Text
                 */
                val titleTypeface =
                    Typeface.createFromAsset(getContext().assets, "font/notosanscjkkrbold.otf")
                val subTitleTypeface =
                    Typeface.createFromAsset(getContext().assets, "font/notosanscjkkrmedium.otf")
                var titleColor = text_color_Array[target]
                titlePaint =
                    Paint().apply {
                        isAntiAlias = true
                        color = Color.parseColor(titleColor)
                        style = Paint.Style.FILL
                        textSize = titleSize
                        textAlign = Paint.Align.LEFT
                        typeface = titleTypeface
                    }

                subTitlePaint =
                    Paint().apply {
                        isAntiAlias = true
                        color = Color.parseColor(titleColor)
                        style = Paint.Style.FILL
                        textSize = resources.getDimension(R.dimen.title_text_size)
                        textAlign = Paint.Align.LEFT
                        typeface = subTitleTypeface
                    }


                val titleMarginTop = resources.getDimension(R.dimen.title_margin_top)
                canvas?.drawText(title!!, graphMargin, titleSize + titleMarginTop, titlePaint)
                canvas?.drawText(
                    subTitle!!,
                    graphMargin,
                    titleSize + subTitleSize + titleMarginTop * 2,
                    subTitlePaint
                )

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
            }
        } catch (e: Exception) {
            Log.e("Bullet Graph", e.message)
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