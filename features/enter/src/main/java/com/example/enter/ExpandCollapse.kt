package com.example.enter

import android.content.res.Resources
import android.view.View
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.PathParser


class ExpandCollapse {
    private fun moveButton() {
        /*Path path = PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1");
        PathInterpolator pathInterpolator = new PathInterpolator(path);
        ObjectAnimator animation = ObjectAnimator.ofFloat(binding.button, "translationY", 200f);

        animation.setInterpolator(pathInterpolator);
        animation.setDuration(2000);*/
    }

    companion object {
        fun expand(v: View, initialHeight: Int) {
            val matchParentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec((v.parent as View).width, View.MeasureSpec.EXACTLY)
            val wrapContentMeasureSpec =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
            val targetHeight: Int = v.measuredHeight;

//            val targetHeight: Int = initialHeight.dpToPx();

            /*val constraintLayout: ConstraintLayout = v as ConstraintLayout
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout);
            constraintSet.connect()
            constraintSet.applyTo(constraintLayout)*/


            // Older versions of android (pre API 21) cancel animations for views with a height of 0.
            v.layoutParams.height = 1
            v.visibility = View.VISIBLE
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    v.layoutParams.height =
                        if (interpolatedTime == 1f //                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        ) targetHeight else (targetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // Expansion speed of 1dp/ms
            a.setDuration(
                (targetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
            )
            a.setDuration(700)
            val path =
                PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
            val pathInterpolator = PathInterpolator(path)
            a.interpolator = pathInterpolator
            v.startAnimation(a)
        }

        fun collapse(v: View) {
            val initialHeight = v.measuredHeight
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE
                    } else {
                        v.layoutParams.height =
                            initialHeight - (initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            // Collapse speed of 1dp/ms
            a.setDuration(
                (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
            )
            a.setDuration(700)
            val path =
                PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
            val pathInterpolator = PathInterpolator(path)
            a.interpolator = pathInterpolator
            v.startAnimation(a)
        }
    }


}

private fun Int.dpToPx(): Int {
    return this * Resources.getSystem().getDisplayMetrics().density.toInt()
}
