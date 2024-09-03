package com.example.settings.adapter.model

import androidx.annotation.DrawableRes
import com.example.views.adapter.adaptersdelegate.CustomDiffUtilCallback
import com.google.android.material.card.MaterialCardView
import com.google.android.material.shape.ShapeAppearanceModel

sealed class SettingsItem {
    data class Section(val title: String) : SettingsItem()
    data class PressOption(
        val title: String,
        val subtitle: String,
        @DrawableRes
        val icon: Int,
        val cornersType: CornersType,
        val action: () -> Unit,
    ) : SettingsItem()
    data class SwitchOption(
        val title: String,
        val subtitle: String,
        @DrawableRes
        val icon: Int,
        val isChecked: Boolean,
        val cornersType: CornersType,
        val action: () -> Unit,
    ): SettingsItem()

    enum class CornersType {
        SINGLE, TOP, BOTTOM, NO_CORNERS
    }



    class SettingsItemDiffUtil : CustomDiffUtilCallback<SettingsItem> {
        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<SettingsItem>,
            newList: List<SettingsItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return if (oldItem is Section && newItem is Section)
                oldItem == newItem
            else if (oldItem is PressOption && newItem is PressOption)
                oldItem == newItem
            else false
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<SettingsItem>,
            newList: List<SettingsItem>
        ): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }

        override fun getChangePayload(
            oldItemPosition: Int,
            newItemPosition: Int,
            oldList: List<SettingsItem>,
            newList: List<SettingsItem>
        ): Any? = null
    }

    companion object {
        const val CORNER_SIZE_VALUE = 40F
        fun setupCornersToMaterialCardView(cornersType: SettingsItem.CornersType, view: MaterialCardView): ShapeAppearanceModel {
            return when (cornersType) {
                SettingsItem.CornersType.SINGLE -> view.shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(SettingsItem.CORNER_SIZE_VALUE).build()

                SettingsItem.CornersType.TOP -> view.shapeAppearanceModel.toBuilder()
                    .setTopLeftCornerSize(SettingsItem.CORNER_SIZE_VALUE)
                    .setTopRightCornerSize(SettingsItem.CORNER_SIZE_VALUE)
                    .setBottomLeftCornerSize(0F)
                    .setBottomRightCornerSize(0F)
                    .build()

                SettingsItem.CornersType.BOTTOM -> view.shapeAppearanceModel.toBuilder()
                    .setTopLeftCornerSize(0F)
                    .setTopRightCornerSize(0F)
                    .setBottomLeftCornerSize(SettingsItem.CORNER_SIZE_VALUE)
                    .setBottomRightCornerSize(SettingsItem.CORNER_SIZE_VALUE)
                    .build()

                SettingsItem.CornersType.NO_CORNERS -> view.shapeAppearanceModel.toBuilder()
                    .setAllCornerSizes(0F)
                    .build()

            }
        }

    }

}
