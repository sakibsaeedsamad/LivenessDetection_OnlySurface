package com.sssakib.livenessdetection

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceView;


class CircularSurfaceView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    SurfaceView(context, attrs, defStyle) {
    private var borderWidth = 0
    private var canvasSize = 0
    private val paintBorder: Paint?
    private val path: Path

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.CircularSurfaceViewStyle
    ) {
    }

    fun setBorderWidth(borderWidth: Int) {
        this.borderWidth = borderWidth
        requestLayout()
        this.invalidate()
    }

    fun setBorderColor(borderColor: Int) {
        if (paintBorder != null) paintBorder.setColor(borderColor)
        this.invalidate()
    }

    override fun dispatchDraw(canvas: Canvas) {
        Log.d("view", "canvas")
        canvasSize = canvas.getWidth()
        if (canvas.getHeight() < canvasSize) canvasSize = canvas.getHeight()

        // circleCenter is the x or y of the view's center
        // radius is the radius in pixels of the cirle to be drawn
        // paint contains the shader that will texture the shape
        val circleCenter = (canvasSize - borderWidth * 2) / 2
        canvas.drawCircle(
            (circleCenter + borderWidth).toFloat(), (circleCenter + borderWidth).toFloat(),
            (canvasSize - borderWidth * 2) / 2 - borderWidth - 4.0f, paintBorder!!
        )
        path.addCircle(
            (circleCenter + borderWidth).toFloat(), (circleCenter + borderWidth).toFloat(),
            (canvasSize - borderWidth * 2) / 2 - 4.0f, Path.Direction.CW
        )
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        Log.d("width", width.toString())
        Log.d("height", height.toString())
        setMeasuredDimension(width, height)
    }

    private fun measureWidth(measureSpec: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)
        result = if (specMode == MeasureSpec.EXACTLY) {
            // The parent has determined an exact size for the child.
            specSize
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            specSize
        } else {
            // The parent has not imposed any constraint on the child.
            canvasSize
        }
        return result
    }

    private fun measureHeight(measureSpecHeight: Int): Int {
        var result = 0
        val specMode = MeasureSpec.getMode(measureSpecHeight)
        val specSize = MeasureSpec.getSize(measureSpecHeight)
        result = if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            specSize
        } else if (specMode == MeasureSpec.AT_MOST) {
            // The child can be as large as it wants up to the specified size.
            specSize
        } else {
            // Measure the text (beware: ascent is a negative number)
            canvasSize
        }
        return result + 2
    }

    init {

        // init paint
        paintBorder = Paint()
        paintBorder.setAntiAlias(true)
        path = Path()

        // load the styled attributes and set their properties
        val attributes: TypedArray =
            context.obtainStyledAttributes(attrs, R.styleable.CircularSurfaceView, defStyle, 0)
        val defaultBorderSize = (4 * getContext().resources.displayMetrics.density + 0.5f).toInt()
        setBorderWidth(
            attributes.getDimensionPixelOffset(
                R.styleable.CircularSurfaceView_border_width,
                defaultBorderSize
            )
        )
        setBorderColor(
            attributes.getColor(
                R.styleable.CircularSurfaceView_border_color,
                Color.WHITE
            )
        )
    }
}