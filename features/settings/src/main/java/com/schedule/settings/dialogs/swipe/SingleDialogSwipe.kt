package com.schedule.settings.dialogs.swipe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.RoundRectShape
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.schedule.settings.dialogs.adapter.SingleItemDelegate
import com.schedule.settings.dialogs.adapter.model.GroupItem
import com.schedule.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter

class SingleDialogSwipe(
    private val adapter: UniversalRecyclerViewAdapter<GroupItem>,
    private val context: Context,
    private val swipeDeleteCallback: (GroupItem.Single) -> Unit
) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {

    private val backgroundCornerOffset = 60
    private val background: ColorDrawable = ColorDrawable(Color.RED)
    private val shapeBackground = ShapeDrawable(RectShape())
    private val backgroundLayer: ColorDrawable = ColorDrawable(Color.GRAY)
    private val icon: Drawable =
        ContextCompat.getDrawable(
            context,
            com.schedule.values.R.drawable.baseline_delete_24
        )!!

    init {
        val typedValue = TypedValue()
        val theme = context.theme
        val attr = com.google.android.material.R.attr.colorPrimary
        theme.resolveAttribute(
            attr,
            typedValue,
            true
        )
        val color = if (typedValue.resourceId != 0)
            ContextCompat.getColor(context, typedValue.resourceId)
        else typedValue.data
        shapeBackground.paint.color = color

        val cornerSize = 16F
        val noCornerSize = 0F
        val floatArray =
            floatArrayOf(
                cornerSize,
                cornerSize,
                cornerSize,
                cornerSize,
                cornerSize,
                cornerSize,
                cornerSize,
                cornerSize
            )
        val rectShape = RoundRectShape(floatArray, null, null)
        shapeBackground.shape = rectShape
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val isActive = viewHolder is SingleItemDelegate.SingleItemViewHolder
        return if (isActive) return super.getSwipeDirs(recyclerView, viewHolder) else 0
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is SingleItemDelegate.SingleItemViewHolder) {
            val item = adapter.items[viewHolder.adapterPosition]
            adapter.notifyItemChanged(viewHolder.adapterPosition)
            swipeDeleteCallback.invoke((item as GroupItem.Single))
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val maxWidth = -itemView.width

        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight


        if (dX > 0) {
            shapeBackground.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
            )
        } else if (dX >= maxWidth) {
            icon.setBounds(itemView.left - 100, itemView.top, itemView.right, itemView.bottom)

            val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            shapeBackground.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top,
                itemView.right,
                itemView.bottom
            )

        } else {
            shapeBackground.setBounds(0, 0, 0, 0)
            icon.setBounds(0, 0, 0, 0)
        }

        if (dX == 0F) {
            shapeBackground.setBounds(0, 0, 0, 0)
            icon.setBounds(0, 0, 0, 0)
        }

        shapeBackground.draw(c)
        icon.draw(c)
    }
}