package com.herace.bulletgraph

import android.util.AttributeSet
import android.view.View
import android.content.Context
import android.graphics.*
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import java.lang.Exception

open class BulletGraph @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr)
{

    private var minWidth = 200;
    private var minHeight = 100;

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
    private var LABEL_1: Array<CharSequence>? = null
    private var LABEL_2: Array<CharSequence>? = null
    private var RANGE: Array<CharSequence>? = null



    init {
        val ATTRS = context.theme.obtainStyledAttributes(attrs, R.styleable.BulletGraph, 0, 0)

        try {
            numberOfFields = ATTRS.getInt(R.styleable.BulletGraph_noOfFields, numberOfFields)
            bgColor = ATTRS.getString(R.styleable.BulletGraph_bgColor)
            isWarning = ATTRS.getBoolean(R.styleable.BulletGraph_isWarning, isWarning)
            title = ATTRS.getString(R.styleable.BulletGraph_title)
            if (title.isNullOrBlank()) title = ""
            value = ATTRS.getInt(R.styleable.BulletGraph_value, value)
            isASC = ATTRS.getBoolean(R.styleable.BulletGraph_isASC, isASC)

            LABEL_1 = ATTRS.getTextArray(R.styleable.BulletGraph_Label_1)
            LABEL_2 = ATTRS.getTextArray(R.styleable.BulletGraph_Label_2)

            RANGE = ATTRS.getTextArray(R.styleable.BulletGraph_Range)
        } catch (e: Exception) {
            Log.e("error", e.localizedMessage.toString())
        } finally {
            ATTRS.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        /**
         * Paint 정의
         */
        var paints: MutableList<Paint> = mutableListOf()
        var paintWhite = Paint()
        var paintGray = Paint()
        var paintYellow = Paint()
        var paintOrange = Paint()
        var paintDarkOragne = Paint()
        var paintRed = Paint()
        var paintDarkRed = Paint()
        var paintBlue = Paint()

        if (SDK_INT >= 23) {
            paintWhite.color = resources.getColor(R.color.colorBulletWhite, null)
            paintGray.color = resources.getColor(R.color.colorBulletGray, null)
            paintYellow.color = resources.getColor(R.color.colorBulletYellow, null)
            paintOrange.color = resources.getColor(R.color.colorBulletOrange, null)
            paintDarkOragne.color = resources.getColor(R.color.colorBulletDarkOrage, null)
            paintRed.color = resources.getColor(R.color.colorBulletRed, null)
            paintDarkRed.color = resources.getColor(R.color.colorBulletDarkRed, null)
            paintBlue.color = resources.getColor(R.color.colorBulletBlue, null)
        } else {
            paintWhite.color = resources.getColor(R.color.colorBulletWhite)
            paintGray.color = resources.getColor(R.color.colorBulletGray)
            paintYellow.color = resources.getColor(R.color.colorBulletYellow)
            paintOrange.color = resources.getColor(R.color.colorBulletOrange)
            paintDarkOragne.color = resources.getColor(R.color.colorBulletDarkOrage)
            paintRed.color = resources.getColor(R.color.colorBulletRed)
            paintDarkRed.color = resources.getColor(R.color.colorBulletDarkRed)
            paintBlue.color = resources.getColor(R.color.colorBulletBlue)
        }

        paints.add(paintGray)
        paints.add(paintYellow)
        paints.add(paintOrange)
        paints.add(paintDarkOragne)
        paints.add(paintRed)
        paints.add(paintDarkRed)

        /**
         * 가로 세로 설정
         */
        var WIDTH = minWidth
        var HEIGHT = minHeight
        if (width > minWidth) WIDTH = width
        if (height > minHeight) HEIGHT = height

        /**
         * 배경 색상
         */
        val bgPaint = Paint()
        bgPaint.setColor(Color.parseColor(bgColor))
        canvas?.drawRect(0.0F, 0.0f, WIDTH.toFloat(), HEIGHT.toFloat(), bgPaint);


        /**
         * marker image 설정
         */
        val marker_red = BitmapFactory.decodeResource(
            context.resources, R.drawable.mark_red
        )
        val marker_blue = BitmapFactory.decodeResource(
            context.resources, R.drawable.mark_blue
        )

        /**
         * block 좌표 계산
         */
        val top = HEIGHT.toFloat() * 0.4f
        val bottom = HEIGHT.toFloat() * 0.6f


        /**
         * 블럭 갯수
         */
        val number:Int = numberOfFields
        val ratio: Float = WIDTH.toFloat() / (number + 1).toFloat()


        /**
         * 블럭 그리기
         */
        for (i in 0 until number) {
            canvas?.drawRect(i * ratio + ratio / 2f , top, ( i + 1) * ratio + ratio / 2f, bottom, paints[i])
        }


        /**
         * 타이틀
         */
        val titleSize = 40f
        var titleColor = resources.getColor(R.color.colorBulletBlack)
        if (isWarning) titleColor = resources.getColor(R.color.colorBulletRed)
        val titlePaint =
            Paint().apply {
                isAntiAlias = true
                color = titleColor
                style = Paint.Style.FILL
                textSize = titleSize
                textAlign = Paint.Align.LEFT
                typeface = Typeface.DEFAULT
            }
        canvas?.drawText(title!!, ratio / 2f, titleSize, titlePaint)



        /**
         * Axis 텍스트 그리기
         */
//        val axisTexts1: List<String> =  listOf("정상", "경도", "중등도", "중증", "말기")
//        val axisTexts2: List<String> =  listOf("90", "60", "30", "15", "")
//        val axisTexts1: List<String> =  listOf("말기", "중증", "중등도", "경도", "정상")
//        val axisTexts2: List<String> =  listOf("", "15", "30", "60", "90", "")

        val text1Size = 40f
        val textPaint =
            Paint().apply {
                isAntiAlias = true
                color = resources.getColor(R.color.colorBulletBlack)
                style = Paint.Style.FILL
                textSize = text1Size
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT
            }

        val text1Bottom = bottom + text1Size + 10f
        val text2Bottom = bottom + text1Size * 2 + 10f

        for (i in 0 until number) {
            canvas?.drawText(LABEL_1?.get(i).toString(), (i + 1) * ratio + ratio / 2f, text1Bottom, textPaint)
            canvas?.drawText(LABEL_2?.get(i).toString(), (i + 1) * ratio + ratio / 2f, text2Bottom, textPaint)
        }


        /**
         * marker 그리기
         */
//        val max = 1000
//        val min = 0
//        val range = listOf<Int>(max, 90, 60, 30, 15, min)
//        val range = listOf<Int>(min, 15, 30, 60, 90, max)

        /**
         * 마커의 x좌표 계산하기
         */
        var marker_x: Float = 0f
        var target = 0
        for (i in 0 until RANGE?.size?.minus(1)!!) {
            if (isASC) {
                if (value >= Integer.parseInt(RANGE?.get(i).toString()) && value <  Integer.parseInt(RANGE?.get(i + 1).toString())) {
                    target = i
                    val startVal =  Integer.parseInt(RANGE?.get(i + 1).toString()).toFloat()
                    val endVal = Integer.parseInt(RANGE?.get(i).toString()).toFloat()

                    val ratioValue = (value.toFloat() - endVal) / (startVal - endVal)

                    marker_x = ratio * ratioValue
                    break
                }
            } else {
                if (value < Integer.parseInt(RANGE?.get(i).toString()) && value >=  Integer.parseInt(RANGE?.get(i + 1).toString())) {
                    target = i
                    val startVal =  Integer.parseInt(RANGE?.get(i).toString()).toFloat()
                    val endVal = Integer.parseInt(RANGE?.get(i + 1).toString()).toFloat()

                    val ratioValue = 1F - (value.toFloat() - endVal) / (startVal - endVal)

                    marker_x = ratio * ratioValue

                    break
                }
            }

        }

        val x = (marker_x + target * ratio + ratio / 2f).toInt() - (WIDTH/22)/2
        val y = top.toInt() - WIDTH/22
        val w = WIDTH/22 + x
        val h = WIDTH/22 + y

        val dst = Rect(x, y, w, h)

        if (isWarning) canvas?.drawBitmap(marker_red, null, dst, null)
        else canvas?.drawBitmap(marker_blue, null, dst, null)
    }
}