package com.herace.bulletgraph

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.content.Context

/**
 * @author Herace(jaloveeye@gmail.com)
 * Class: BulletEmptyCircle
 * Created by Herace on 2019/12/19.
 * Description:
 */
class BulletEmptyCircle @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : BulletGraph(context, attrs, defStyleAttr)
{
    private var paintCyan = Paint()
    private var paintLightRed = Paint()

    private var graphRect: RectF = RectF(0F, 0F, 0F, 0F)

    var comment: String? = ""
        set(value) {
            if (value != null) {
                field = value
                invalidate()
            }
        }

    val labelTopPaint: Paint
    val labelBottomPaint: Paint
    val commentPaint: Paint

    init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletTopBottom, 0, 0)

        try {
            comment = attributeSet.getString(R.styleable.BulletTopBottom_comment)
            if (comment.isNullOrBlank()) comment = null

            title = attributeSet.getString(R.styleable.BulletGraph_title)
            if (title.isNullOrBlank()) title = null


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

            var commentColor = getResourceIdToColor(R.color.colorTextBlack)
            if (warning!!.toBoolean()) commentColor = getResourceIdToColor(R.color.colorWarning)

            commentPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = commentColor
                    style = Paint.Style.FILL
                    textSize = titleSize
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
        var graphTop: Float
        if (comment != null) graphTop = 0.65f
        else graphTop = 0.5f
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

        graphRect.set(0f + graphMargin, top, (graphWidth.toFloat()) * value / 100 + graphMargin, bottom)
        canvas?.drawRoundRect(graphRect, cornerRadius, cornerRadius, paint)

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
        val titleMarginTop = resources.getDimension(R.dimen.title_margin_top)
        if (title != null) canvas?.drawText(title!!, graphMargin, titleSize + titleMarginTop, titlePaint)


        if (comment != null) canvas?.drawText(comment!!, graphMargin, titleSize * 2 + titleMarginTop, commentPaint)

        /**
         * Draw axis label
         */
        val labelTop = top - labelSize
        val labelValueText = "${value}%"
        val labelValueX =graphMargin + graphWidth.toFloat() * value / 100 - getTextWidth(labelPaint,labelValueText, boundRect) / 2
        canvas?.drawText(labelValueText, labelValueX, labelTop, labelBottomPaint)

        val temp =  getTextWidth(labelPaint,labelValueText, boundRect) / 2
        drawCircle(canvas, labelValueX + temp, top, bottom, R.color.colorWarning)
    }

    private fun drawCircle(canvas: Canvas?, markerX: Float, top: Float, bottom: Float, color: Int) {

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
    }
}