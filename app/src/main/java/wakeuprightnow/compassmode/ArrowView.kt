package wakeuprightnow.compassmode


import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View

class ArrowView constructor(context: Context, val w: Int, val h: Int): View(context){
    var alength: Float = 100.0f
    var arrowd: Float = 20.0f
    var arroww: Float = 10.0f

    var startX: Float = 160.0f
    var startY: Float = 240.0f
    var degree: Float = 0.0f
    var stopX: Float = 0.0f
    var stopY: Float = 0.0f

    var paint: Paint
    var paint1: Paint //big circle
    var paint2: Paint //number text
    var paint3: Paint //little circle
    var radius: Float = 500F
    var radius2: Float = 50F

    var pointNumX: Float = 0.0f
    var pointNumY: Float = 0.0f
    var pointNum: Int = 0


    init {
        startX = w/2.0f
        startY = h/2.0f
        paint = Paint()
        paint.strokeWidth = 5.0f
        paint.color = Color.BLACK

        paint1 = Paint()
        paint1.setColor(Color.parseColor("#90E0EF"))
        paint1.setAntiAlias(true)
        paint1.setDither(true)

        paint2 = Paint()
        paint2.color = Color.BLACK
        paint2.textSize = 80f

        paint3 = Paint()
        paint3.setColor(Color.parseColor("#FDFCDC"))
        paint3.setAntiAlias(true)
        paint3.setDither(true)


        var shorter = w
        if (h < w)
            shorter = h
        alength = shorter/3.0f
    }

    fun getX(angle: Float): Float{
        var x = (startX + 410 * Math.cos(angle * Math.PI / 180)).toFloat()
        return x
    }

    fun getY(angle: Float): Float {
        var y = (startY - 450 * Math.sin(angle * Math.PI / 180)).toFloat()
        return y
    }

    fun getAngle(num: Int): Float{
        var angle = 90f - num*36f
        if(angle < 0f){
            angle = angle + 360f
        }
        return angle
    }

    fun drawNum(num: String, angle: Float, canvas: Canvas){
        canvas.drawText(num, getX(angle) - 25, getY(angle) + 30, paint2);
    }

    fun drawRedCircle(num: Int, canvas: Canvas){
        pointNumX = getX(getAngle(num))
        pointNumY = getY(getAngle(num))
        canvas.drawCircle(pointNumX, pointNumY, radius2, paint3);
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


        var radian = degree*Math.PI/180.0f
        stopX = startX + (alength*Math.sin(radian)).toFloat()
        stopY = startY - (alength*Math.cos(radian)).toFloat()
        canvas.drawColor(Color.WHITE);

        canvas.drawCircle(startX, startY, radius, paint1);
        drawRedCircle(pointNum, canvas)
        canvas.drawLine(startX, startY, stopX, stopY, paint);

        for (i in 0..9){
            drawNum(i.toString(), getAngle(i), canvas)
        }

        var v3x: Float = stopX + ((startX-stopX)*arrowd)/alength
        var v3y: Float = stopY + ((startY-stopY)*arrowd)/alength

        var diffx: Float = Math.abs((arroww*(stopY-startY))/alength)
        var diffy: Float = Math.abs((arroww*(stopX-startX))/alength)

        var leftax: Float = v3x - diffx
        var rightax: Float = v3x + diffx

        var leftay: Float = v3y + diffy
        var rightay: Float = v3y - diffy

        if ((startX-stopX) < 0.0) {
            leftay = v3y - diffy;
            rightay = v3y + diffy;
        }

        if ((startY-stopY) < 0.0) {
            leftax = v3x + diffx;
            rightax = v3x - diffx;
        }

        canvas.drawLine(leftax, leftay, stopX, stopY, paint);
        canvas.drawLine(rightax, rightay, stopX, stopY, paint);
    }


}