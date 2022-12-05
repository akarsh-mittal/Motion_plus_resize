package com.example.android.motion_plus_resize

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.android.motion_plus_resize.R

class MainActivity : AppCompatActivity() {
    lateinit var floatView: LinearLayout
    lateinit var parentView: RelativeLayout
    lateinit var floatWindowLayoutParams: WindowManager.LayoutParams
    lateinit var kHandle: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var rightDY: Float = 0.0f
        var rightDX: Float = 0.0f

        var newScale: Double = 1.0
        var temp3: Float =0f
        var temp4:Float = 0f


        parentView = findViewById(R.id.parent)
        floatView = findViewById(R.id.child)
        kHandle = findViewById(R.id.handle)


        kHandle.setOnClickListener() {
            Log.d("***", "CHALA?")
        }
        floatView.setOnTouchListener(View.OnTouchListener { view, event ->
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {

                    rightDX = view!!.getX() - event.rawX
                    rightDY = view!!.getY() - event.rawY;
//                    Log.d("&&&&",
//                        "$rightDY .... ${view!!.getY()} .... ${event.rawY} ... ${floatView.width}....${parentView.height}")
//
//                    Log.d("****", "${floatView.getX()}, ${floatView.translationX}, ${floatView.getLeft()}")

                }
                MotionEvent.ACTION_MOVE -> {

//                    floatView.background.alpha=100

                    floatView.setAlpha(.5f)

                    var xDisplacement = event.rawX + rightDX

                    var yDisplacement =
                        event.rawY + rightDY // (chota raw y movement (minus of two rawY (diff event)) + view.getY) bachega

//                    Log.d("&&&&", "${newScale}, ${floatView.width}, ${floatView.width * newScale}")
                    var temp1 =(floatView.width - floatView.width*newScale)/2
                    var temp2= (floatView.height - floatView.height*newScale)/2
                    if (xDisplacement > parentView.width - (floatView.width - temp1)) {
                        xDisplacement = (parentView.width - (floatView.width - temp1)).toFloat()
                    } else if (xDisplacement < 0) {
                        xDisplacement = 0f
                    }
                    if (yDisplacement > parentView.height - floatView.height) {
                        yDisplacement = (parentView.height - floatView.height).toFloat()
                    } else if (yDisplacement < 0) {
                        yDisplacement = 0f
                    }

                    view!!.animate()
                        .x(xDisplacement)
                        .y(yDisplacement)
                        .setDuration(0)
                        .start()

                    temp3= xDisplacement
                    temp4= yDisplacement

                    kHandle.animate().x((xDisplacement + (floatView.width-temp1)).toFloat())
                        .y((yDisplacement + (floatView.height -temp2)).toFloat()).setDuration(0).start()


                }
                else -> {
                    floatView.setAlpha(1f)
                    return@OnTouchListener false
                }
            }
            true
        })


        kHandle.setOnTouchListener(object : OnTouchListener {
            var centerX = 0f
            var centerY = 0f
            var startR = 0f
            var startX = 0f
            var startY = 0f
            var startScale = 0f

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event?.action == MotionEvent.ACTION_DOWN) {
//                    Log.d("****",
//                        "${floatView.getLeft()},   ${floatView.x},   ${floatView.getRight()},  ${floatView.width},  ${parentLayout.width} ")
//                    Log.d("****", "${(floatView.getLeft() + floatView.getRight()) / 2f}")

                    // calculate center of image
                    centerX = (floatView.getLeft() + floatView.getRight()) / 2f
                    centerY = (floatView.getTop() + floatView.getBottom()) / 2f;

                    Log.d("****", "${event.getRawX()},  ${kHandle.getX()},    ${centerX}")
                    // recalculate coordinates of starting point
                    startX = event.getRawX() - kHandle.getX() + centerX;
                    startY = event.getRawY() - kHandle.getY() + centerY;

//                    Log.d("****", "${startX}")

                    // get starting distance and scale
                    startR = Math.hypot((event.getRawX() - startX).toDouble(),
                        (event.getRawY() - startY).toDouble()).toFloat()
                    startScale = floatView.getScaleX()

//                    Log.d("****",
//                        "${(event.getRawX() - startX)},  startR: ${startR},  startScale: ${startScale}")

                } else if (event?.action == MotionEvent.ACTION_MOVE) {

//                    if (kHandle.getX() + kHandle.width +10 >= parentLayout.width) {
//                        return true
//                    }

                    // calculate new distance
                    var newR: Double = Math.hypot((event.getRawX() - startX).toDouble(),
                        (event.getRawY() - startY).toDouble())

                    //set new scale
                    newScale = newR / startR * startScale


//                    Log.d("****", "newR: ${newR},    scale: ${newScale}")

//                    Log.d("****",
//                        " ${kHandle.width+kHandle.getX()},  kHandle.getX(): ${kHandle.getX()}, parentLayout.width: ${parentView.width} ")

                    //setting constraints
                    if (newScale <0.5) {
                        newScale =0.5
                    }
//                    if(newScale>=1.5) {
//                        newScale -= (newScale-1.5)
//                    }

                    if(newScale>1.1) {
                        newScale=1.1
                    }

//                    Log.d("****", "x: ${kHandle.getX()},  width: ${kHandle.width}")


//                    if(kHandle.getX() + kHandle.width >= parentLayout.width) {
//                        kHandle.setX(kHandle.getX()-7*kHandle.width)
//                    }
                    floatView.setScaleX(newScale.toFloat())
                    floatView.setScaleY(newScale.toFloat())

//                    floatView.requestLayout()

//                    Log.d("****", "width ${floatView.width * newScale}")

//                    val params = floatView.layoutParams as ViewGroup.LayoutParams
//
//                    params.setScaleX(newScale.toFloat())
//                    params.setScaleY (newScale.toFloat())
//
//                    floatView.setLayoutParams(params)

//                    floatView.requestLayout()


                    //move handler
                    Log.d("&&&&", "$centerX, $centerY, ${floatView.width}, ${floatView.width*newScale} $temp3}")
                    kHandle.setX((centerX+temp3 + floatView.getWidth() / 2f * newScale).toFloat())
                    kHandle.setY((centerY+temp4 + floatView.getHeight() / 2f * newScale).toFloat())


                } else if (event?.getAction() == MotionEvent.ACTION_UP) {
                }

                return true
            }
        })



    }

}

