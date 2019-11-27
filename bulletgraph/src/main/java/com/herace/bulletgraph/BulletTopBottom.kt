package com.herace.bulletgraph

import android.util.AttributeSet
import android.content.Context
import android.graphics.*

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
    private var paintCyan = Paint()
    private var paintLightRed = Paint()


    /**
     * make image to marker
     */
    private val markerRed = BitmapFactory.decodeResource(context.resources, R.drawable.marker_circle_red)
    private val markerBlue = BitmapFactory.decodeResource(context.resources, R.drawable.marker_circle_cyan)

    private var graphRect: RectF = RectF(0F, 0F, 0F, 0F)

    private var isTop: Boolean = false

    val labelTopPaint: Paint
    val labelBottomPaint: Paint


    init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletTopBottom, 0, 0)

        try {
            isTop = attributeSet.getBoolean(R.styleable.BulletTopBottom_isTop, isTop)

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
        val bottom = top + 20f


        /**
         * Draw graph
         */
        val cornerRadius = 25F
        val graphMargin = 40f

        val graphWidth = mWIDTH - graphMargin * 2

        graphRect.set(0f + graphMargin, top, graphWidth + graphMargin, bottom)
        canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paints[0])

        val paint = if(isTop) paints[1] else paints[2]

        if (isTop) graphRect.set(graphWidth - (graphWidth.toFloat() * value / 100) + graphMargin, top, graphWidth + graphMargin, bottom)
        else graphRect.set(0f + graphMargin, top, (graphWidth.toFloat()) * value / 100 + graphMargin, bottom)
        canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paint)


        /**
         * Draw Title Text
         */
        val titleMarginTop = 40f
        titlePaint.color = if (isWarning) getResourceIdToColor(R.color.colorBulletLightRed) else getResourceIdToColor(R.color.colorBulletGray)
        canvas?.drawText(title!!, graphMargin, titleSize + titleMarginTop, titlePaint)


        /**
         * Draw axis label
         */
        val labelMarginTop = 40f
        val labelBottom = bottom + labelSize + labelMarginTop

        val labelTopText = "상위"
        val labelBottomText = "하위"
        canvas?.drawText(labelBottomText, graphMargin, labelBottom, labelBottomPaint)
        canvas?.drawText(labelTopText, mWIDTH - graphMargin, labelBottom, labelTopPaint)

        val labelTop = top - labelSize
        val labelValueText = "${value}%"
        var labelValueX = if (isTop) graphMargin + graphWidth.toFloat() * (100f - value) / 100 - getTextWidth(labelPaint,labelValueText, boundRect) / 2
                                else graphMargin + graphWidth.toFloat() * value / 100 - getTextWidth(labelPaint,labelValueText, boundRect) / 2
        canvas?.drawText(labelValueText, labelValueX, labelTop, labelBottomPaint)

        val markerSize = 60
        val markerCenter = top.toInt() + (bottom - top).toInt()
        markerRect.set(labelValueX.toInt(), markerCenter - markerSize/2 - 10, labelValueX.toInt() + markerSize, markerCenter + markerSize/2 - 10)
        if (isTop) canvas?.drawBitmap(markerBlue, null, markerRect, null)
        else canvas?.drawBitmap(markerRed, null, markerRect, null)
    }
}