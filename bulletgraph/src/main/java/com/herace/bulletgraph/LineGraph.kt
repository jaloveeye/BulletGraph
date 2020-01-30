package com.herace.bulletgraph

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import java.lang.Exception

open class LineGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr
) {
    var paintPointLabel = Paint()
    var paintLabel = Paint()
    var paintPointValue = Paint()
    var paintValue = Paint()
    var bgPaint = Paint()

    val textPaint: Paint
    val labelPointPaint: Paint
    val valuePointPaint: Paint
    var dashLinePaint: Paint
    val dashLinePointPaint: Paint
    val linePaint: Paint

    val baseLine = Path()
    val baseLine2 = Path()

    val paintRadius1 : Paint
    val paintRadius2 : Paint

    var valueArray : MutableList<String> = mutableListOf()
    var labelArray : MutableList<String> = mutableListOf()

    var numberOfFields = 0

    var line_values: String? = ""
        set(value) {
            field = value
            invalidate()
        }

    var line_labels: String? = ""
        set(value) {
            field = value
            invalidate()
        }

    var line_color: String? = ""
        set(value) {
            field = value
            invalidate()
        }

    var line_bgcolor: String? = ""
        set(value) {
            field = value
            invalidate()
        }

    var bottomGap: String?
    var bottomTemp: Int

    var boundRect = Rect(0, 0, 0, 0)
    val graphMargin = resources.getDimension(R.dimen.line_graph_margin)
    val radius = resources.getDimension(R.dimen.line_graph_height)
    val radius2 = resources.getDimension(R.dimen.line_circle_radius)

    init {
        val attributeSet = context.theme.obtainStyledAttributes(attrs, R.styleable.LineGraph, 0, 0)

        try {

            line_color = attributeSet.getString(R.styleable.LineGraph_line_color)
            if (line_color.isNullOrBlank()) line_color = "#ff647d"

            line_bgcolor = attributeSet.getString(R.styleable.LineGraph_line_bgcolor)
            if (line_bgcolor.isNullOrBlank()) line_bgcolor = "#FFFFFF"
            bgPaint.color = Color.parseColor(line_bgcolor)

            line_values = attributeSet.getString(R.styleable.LineGraph_line_values)
            if (line_values.isNullOrBlank()) line_values = ""

            line_labels = attributeSet.getString(R.styleable.LineGraph_line_labels)
            if (line_labels.isNullOrBlank()) line_labels = ""

            bottomGap = attributeSet.getString(R.styleable.LineGraph_bottom_gap)
            if (bottomGap.isNullOrBlank()) bottomTemp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0.0F, resources.displayMetrics).toInt()
            else bottomTemp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bottomGap!!.toFloat(), resources.displayMetrics).toInt()

            val boldTypeface = Typeface.createFromAsset(getContext().assets, "font/notosanscjkkrbold.otf")
            val demilightTypeface = Typeface.createFromAsset(getContext().assets, "font/notosanscjkkrdemilight.otf")

            textPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = Color.parseColor("#000000")
                    style = Paint.Style.FILL
                    textSize = resources.getDimension(R.dimen.line_graph_text_size)
                    textAlign = Paint.Align.LEFT
                    typeface = demilightTypeface
                }

            labelPointPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = Color.parseColor("#000000")
                    style = Paint.Style.FILL
                    textSize = resources.getDimension(R.dimen.line_graph_text_size)
                    textAlign = Paint.Align.LEFT
                    typeface = boldTypeface
                }

            valuePointPaint =
                Paint().apply {
                    isAntiAlias = true
                    color = Color.parseColor(line_color)
                    style = Paint.Style.FILL
                    textSize = resources.getDimension(R.dimen.line_graph_text_size)
                    textAlign = Paint.Align.LEFT
                    typeface = boldTypeface
                }

            linePaint =
                Paint().apply {
                    strokeWidth = resources.getDimension(R.dimen.graph_line_stoke_width)
                    isAntiAlias = true
                    color = Color.parseColor(line_color)
                }


//            val dashPathEffect = DashPathEffect(floatArrayOf(8.0f, 10.0f), 0.0F)

            paintRadius1 =
                Paint().apply {
                    isAntiAlias = true
                    color = Color.parseColor("#FFFFFF")
                    style = Paint.Style.FILL
                    strokeWidth = resources.getDimension(R.dimen.line_graph_height)
                    isDither = true
                }

            paintRadius2 =
                Paint().apply {
                    isAntiAlias = true
                    color = Color.parseColor(line_color)
                    style = Paint.Style.STROKE
                    strokeWidth = resources.getDimension(R.dimen.line_graph_height)
                    isDither = true
                }


            dashLinePaint =
                Paint().apply {
                    color = Color.parseColor("#b3aba6")
                    strokeWidth = 2.0F
                    style = Paint.Style.STROKE
                }

            dashLinePointPaint =
                Paint().apply {
                    color = Color.parseColor("#595757")
                    strokeWidth = 2.0F
                    style = Paint.Style.STROKE
                }

//            dashLinePaint =
//                Paint().apply {
//                    color = Color.parseColor("#d9d8d6")
//                    pathEffect = dashPathEffect
//                    strokeWidth = 2.0F
//                    style = Paint.Style.STROKE
//                }
//
//            dashLinePointPaint =
//                Paint().apply {
//                    color = Color.parseColor("#b9b8b8")
//                    pathEffect = dashPathEffect
//                    strokeWidth = 4.0F
//                    style = Paint.Style.STROKE
//                }


        } finally {
            attributeSet.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        try {

            if (line_values != null) {
                valueArray = line_values?.split("|")!!.toMutableList()
                labelArray = line_labels?.split("|")!!.toMutableList()

                numberOfFields = valueArray.size

                var mWIDTH = width
                var mHEIGHT = height


                /**
                 * 그래프 top, bottom 설정
                 */
                val labelBottomValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0F, resources.displayMetrics)
                val graphTop =  labelBottomValue * 5
                val graphTop2 =  labelBottomValue
                val graphBottom = height - labelBottomValue * 5 - bottomTemp
                val graphBottom2 = height - labelBottomValue * 3

                canvas?.drawRect(0.0F, 0.0f, mWIDTH.toFloat(), mHEIGHT.toFloat(), bgPaint)


                /**
                 * x 좌표 계산
                 */
                val number: Int = numberOfFields
//                val ratio: Float = (mWIDTH.toFloat() - graphMargin * 2) / number.toFloat()
                val xArrays = mutableListOf<Float>()
                val marginTemp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20.0F, resources.displayMetrics)
                val widthRatio = (mWIDTH.toFloat() - marginTemp * 2) / number.minus(1).toFloat()
                xArrays.add(marginTemp)
                xArrays.add(marginTemp + widthRatio)
                xArrays.add(marginTemp + widthRatio * 2)
                xArrays.add(marginTemp + widthRatio * 3)

                /**
                 * draw label
                 */
                val labelBottom = mHEIGHT - labelBottomValue

                for (i in 0 until number.minus(1)) {
                    canvas?.drawText(
                        labelArray[i].toString(),
                        xArrays[i] - getTextWidth(
                            textPaint,
                            labelArray[i].toString(),
                            boundRect
                        ) / 2,
                        labelBottom,
                        textPaint
                    )
                }

                canvas?.drawText(
                    labelArray[number - 1].toString(),
                    xArrays[number - 1] - getTextWidth(
                        labelPointPaint,
                        labelArray[number - 1].toString(),
                        boundRect
                    ) / 2,
                    labelBottom,
                    labelPointPaint
                )

                /**
                 * Y 좌표 계산하기
                 */
                val floatValueArray = valueArray.map { it.replace(",", "").toFloat() }
                val maxValue = floatValueArray.max()!!
                val minValue = floatValueArray.min()!!
                val gapValue = maxValue - minValue

                val gapHeight = graphBottom - graphTop

                var calYArray = mutableListOf<Float>()

                for (i in 0 until number) {

                    val calcY = graphBottom - ( gapHeight *  (floatValueArray[i] - minValue) / gapValue)
                    calYArray.add(calcY)
                }


                /**
                 * 점선 그리기...
                 */
                val dashLength = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2.0F, resources.displayMetrics)
                val dashSpace = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4.0F, resources.displayMetrics)
                for (i in 0 until number.minus(1)) {
                    drawDashLine2(canvas, baseLine, xArrays[i], xArrays[i], graphTop2, graphBottom2, dashLinePaint, dashLength, dashSpace)

//                    canvas?.drawLine(xArrays[i],
//                        graphTop2,
//                        xArrays[i],
//                        graphBottom2, dashLinePaint)
                }

                drawDashLine2(canvas, baseLine2, xArrays[number - 1], xArrays[number - 1], calYArray[number - 1], graphBottom2, dashLinePointPaint, dashLength, dashSpace)
                drawDashLine2(canvas, baseLine, xArrays[number - 1], xArrays[number - 1], graphTop2, calYArray[number - 1], dashLinePaint, dashLength, dashSpace)
//                canvas?.drawLine(xArrays[number - 1],
//                    calYArray[number - 1],
//                    xArrays[number - 1],
//                    graphBottom2, dashLinePointPaint)
//
//                canvas?.drawLine(xArrays[number - 1],
//                    graphTop2,
//                    xArrays[number - 1],
//                    calYArray[number - 1], dashLinePaint)

                /**
                 * draw line
                 */
                for (i in 0 until number.minus(1)) canvas?.drawLine(xArrays[i], calYArray[i], xArrays[i + 1], calYArray[i + 1], linePaint)

                /**
                 * draw marker
                 */
                for (i in 0 until number) {
                    canvas?.drawCircle(
                        xArrays[i],
                        calYArray[i],
                        radius2,
                        paintRadius1
                    )

                    canvas?.drawCircle(
                        xArrays[i],
                        calYArray[i],
                        radius2,
                        paintRadius2
                    )
                }

                /**
                 * draw value text
                 */
                for (i in 0 until number.minus(1)) {
                    canvas?.drawText(
                        valueArray[i].toString(),
                        xArrays[i] - getTextWidth(
                            textPaint,
                            valueArray[i].toString(),
                            boundRect
                        ) / 2,
                        calYArray[i] - labelBottomValue - 10.0F,
                        textPaint
                    )
                }

                canvas?.drawText(
                    valueArray[number - 1].toString(),
                    xArrays[number - 1] - getTextWidth(
                        valuePointPaint,
                        valueArray[number - 1].toString(),
                        boundRect
                    ) / 2,
                    calYArray[number - 1] - labelBottomValue - 10.0F,
                    valuePointPaint
                )
            }

        } catch (e: Exception) {
            Log.e("Bullet Graph", e.message.toString())
        }
    }

    fun drawDashLine(canvas: Canvas?, line: Path, start: Float, end: Float, top: Float, bottom: Float, paint: Paint, length: Float, space: Float) {

        var isDrawing = true
        var isFinish = false

        var _top = top

        while (!isFinish) {

            if (isDrawing) {
                line.moveTo(start, _top)
                line.lineTo(start, _top + length)
                canvas?.drawPath(line, paint)

                isDrawing = false

                _top += length
            } else {

                _top += space

                if (_top > bottom) isFinish = true

                isDrawing = true
            }

        }
    }

    fun drawDashLine2(canvas: Canvas?, line: Path, start: Float, end: Float, top: Float, bottom: Float, paint: Paint, length: Float, space: Float) {

        var isDrawing = true
        var isFinish = false

        var _bottom = bottom

        while (!isFinish) {

            if (isDrawing) {
                line.moveTo(start, _bottom)
                line.lineTo(start, _bottom - length)
                canvas?.drawPath(line, paint)

                isDrawing = false

                _bottom -= length
            } else {

                _bottom -= space

                if (_bottom < top) isFinish = true

                isDrawing = true
            }

        }
    }

    open fun getTextWidth(paint: Paint, text: String, rect: Rect) : Int {
        paint.getTextBounds(text, 0, text.length, rect)

        return rect.width()
    }
}