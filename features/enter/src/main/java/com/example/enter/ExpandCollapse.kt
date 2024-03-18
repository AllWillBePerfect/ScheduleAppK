package com.example.enter

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.PathParser
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


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

    /*private fun expand() {

        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(
                (binding.ll.parent as View).width,
                View.MeasureSpec.EXACTLY
            )
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(
                (binding.ll.parent as View).height,
                View.MeasureSpec.AT_MOST
            )
//        binding.ll.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        binding.ll.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        val targetHeight: Int = binding.ll.measuredHeight;
//        val insets = ViewCompat.getRootWindowInsets(activity?.window?.decorView!!)
        val insets = ViewCompat.getRootWindowInsets(binding.root) ?: return
        val keyboardInsets = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
//        val targetHeight: Int = binding.root.rootView.height - binding.root.height - binding.submitButton.height - binding.groupTextInputLayout.height;
        var r = Rect()
        binding.root.rootView.getWindowVisibleDisplayFrame(r)
//        val targetHeight: Int = binding.root.rootView.height - (r.bottom - r.top) - binding.submitButton.height
        val targetHeight: Int =
            binding.root.height - (binding.toolbar.height + binding.groupTextInputLayout.height + binding.submitButton.height + 1)
        Toast.makeText(requireContext(), (targetHeight).toString(), Toast.LENGTH_SHORT).show()


        *//*val param = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT,
            1.0f
        )
        binding.ll.setLayoutParams(param)*//*
        binding.ll.layoutParams.height = 1
        val lp = (binding.ll.layoutParams as LinearLayout.LayoutParams)
        val a = object : Animation() {

            //            val targetHeight = param.height
            override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                binding.ll.layoutParams.height = if (interpolatedTime == 1f) targetHeight
                else (targetHeight * interpolatedTime).toInt()
                binding.ll.requestLayout()
                *//*if (interpolatedTime == 1F) {
                    binding.ll.layoutParams.height = binding.ll.height
                    lp.weight = 0F
                } else {
                    lp.weight = 100F * interpolatedTime
                }

                binding.ll.layoutParams = lp
                binding.ll.requestLayout()*//*
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        a.setDuration(700)
        val path =
            PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
        val pathInterpolator = PathInterpolator(path)
        a.interpolator = pathInterpolator
        binding.ll.startAnimation(a)

    }

    private fun collapse() {
        val initialHeight = binding.ll.layoutParams.height
        (binding.ll.layoutParams as LinearLayout.LayoutParams).weight = 0F
        binding.ll.layoutParams.height = initialHeight
        val a: Animation = object : Animation() {
            val lp = (binding.ll.layoutParams as LinearLayout.LayoutParams)
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    binding.ll.layoutParams.height = 0
                } else {
                    binding.ll.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                }
                binding.ll.requestLayout()

                *//*lp.weight = 100F - (100F * interpolatedTime)
                binding.ll.layoutParams = lp
                binding.ll.requestLayout()*//*
            }

            *//*override fun willChangeBounds(): Boolean {
                return true
            }*//*
        }

        a.setDuration(700)
        val path =
            PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
        val pathInterpolator = PathInterpolator(path)
        a.interpolator = pathInterpolator
        binding.ll.startAnimation(a)
    }*/


}

fun Int.dpToPx(): Int {
    return this * Resources.getSystem().getDisplayMetrics().density.toInt()
}


