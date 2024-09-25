package com.example.settings.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.PathInterpolator
import android.view.animation.Transformation
import android.widget.LinearLayout
import androidx.core.graphics.PathParser
import androidx.recyclerview.widget.RecyclerView
import com.example.settings.adapter.model.SettingsItem
import com.example.settings.databinding.V2PressExpandableSectionBinding
import com.example.views.adapter.adaptersdelegate.AdapterItemDelegate

class PressExpandableOptionDelegate : AdapterItemDelegate<SettingsItem> {
    override fun forItem(item: SettingsItem): Boolean = item is SettingsItem.PressExpandableOption

    override fun getViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        val binding =
            V2PressExpandableSectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PressOptionViewHolder(binding)
    }

    override fun bindViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        item: SettingsItem,
        payloads: MutableList<Any>
    ) = (viewHolder as PressOptionViewHolder).bind(item as SettingsItem.PressExpandableOption)


    inner class PressOptionViewHolder(private val binding: V2PressExpandableSectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SettingsItem.PressExpandableOption) {
            binding.title.text = item.title
            binding.subtitle.text = item.subtitle
            binding.icon.setImageResource(item.icon)
            binding.root.shapeAppearanceModel =
                SettingsItem.setupCornersToMaterialCardView(item.cornersType, binding.root)
            binding.root.setOnClickListener { item.action.invoke(item) }
            if (item.isExpand) expand() else collapse()

        }

        fun expand() {
            binding.expandableLayout.visibility = View.VISIBLE
            val targetHeight: Int = LinearLayout.LayoutParams.WRAP_CONTENT

            val a = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
                    binding.expandableLayout.layoutParams.height = targetHeight * interpolatedTime.toInt()
                    binding.expandableLayout.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.duration = 700
            val path =
                PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
            val pathInterpolator = PathInterpolator(path)
            a.interpolator = pathInterpolator
            binding.expandableLayout.startAnimation(a)
        }
        fun collapse() {
            val initialHeight = binding.expandableLayout.layoutParams.height

            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
//                if (interpolatedTime == 0F)
                    if (interpolatedTime == 1F) {
                        binding.expandableLayout.layoutParams.height = 1
                        binding.expandableLayout.visibility = View.GONE
                    } else {
                        binding.expandableLayout.layoutParams.height =
                            initialHeight - (initialHeight * interpolatedTime).toInt()
                    }
                    binding.expandableLayout.requestLayout()
                }


                override fun willChangeBounds(): Boolean {
                    return true
                }
            }

            a.duration = 700
            val path =
                PathParser.createPathFromPathData("M 0,0 C 0.05, 0, 0.133333, 0.06, 0.166666, 0.4 C 0.208333, 0.82, 0.25, 1, 1, 1")
            val pathInterpolator = PathInterpolator(path)
            a.interpolator = pathInterpolator
            binding.expandableLayout.startAnimation(a)
        }

    }
}