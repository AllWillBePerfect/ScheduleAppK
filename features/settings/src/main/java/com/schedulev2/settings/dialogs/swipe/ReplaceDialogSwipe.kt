package com.schedulev2.settings.dialogs.swipe

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
import com.schedulev2.settings.dialogs.adapter.ReplaceItemDelegate
import com.schedulev2.settings.dialogs.adapter.model.GroupItem
import com.schedulev2.views.adapter.adaptersdelegate.UniversalRecyclerViewAdapter

class ReplaceDialogSwipe(
    private val adapter: UniversalRecyclerViewAdapter<GroupItem>,
    val context: Context,
    private val swipeDeleteCallback: (GroupItem.Replace) -> Unit,
    private val swipeChangeCallback: (GroupItem.Replace) -> Unit,
) : ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {

    private val backgroundCornerOffset = 60
    private val background: ColorDrawable = ColorDrawable(Color.RED)
    private val shapeBackground = ShapeDrawable(RectShape())
    private val backgroundLayer: ColorDrawable = ColorDrawable(Color.GRAY)
    private val iconDelete: Drawable =
        ContextCompat.getDrawable(
            context,
            com.schedulev2.values.R.drawable.baseline_delete_24
        )!!
    private val iconChange: Drawable =
        ContextCompat.getDrawable(
            context,
            com.schedulev2.values.R.drawable.change
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
        val isActive = viewHolder is ReplaceItemDelegate.ReplaceItemViewHolder
        return if (isActive) return super.getSwipeDirs(recyclerView, viewHolder) else 0
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is ReplaceItemDelegate.ReplaceItemViewHolder)
            if (direction == ItemTouchHelper.LEFT) {
                val item = adapter.items[viewHolder.adapterPosition]
                adapter.notifyItemChanged(viewHolder.adapterPosition)
                swipeDeleteCallback.invoke((item as GroupItem.Replace))
            } else if (direction == ItemTouchHelper.RIGHT) {
                val item = adapter.items[viewHolder.adapterPosition]
                adapter.notifyItemChanged(viewHolder.adapterPosition)
                swipeChangeCallback.invoke((item as GroupItem.Replace))
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

        val iconMargin = (itemView.height - iconDelete.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - iconDelete.intrinsicHeight) / 2
        val iconBottom = iconTop + iconDelete.intrinsicHeight


        if (dX > 0) {
            val iconLeftMargin = (itemView.height - iconChange.intrinsicHeight) / 2
            val iconEditTop = itemView.top + (itemView.height - iconChange.intrinsicHeight) / 2
            val iconEditBottom = iconEditTop + iconChange.intrinsicHeight

            val iconEditLeft = itemView.left + iconLeftMargin
            val iconEditRight = iconEditLeft + iconChange.intrinsicWidth
            iconChange.setBounds(iconEditLeft, iconEditTop, iconEditRight, iconEditBottom)

            shapeBackground.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
            )
            iconDelete.setBounds(0, 0, 0, 0)

        } else if (dX >= maxWidth) {
            val iconLeft = itemView.right - iconMargin - iconDelete.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom)

            shapeBackground.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            iconChange.setBounds(0, 0, 0, 0)

        }  else {
            shapeBackground.setBounds(0, 0, 0, 0)
            iconDelete.setBounds(0, 0, 0, 0)
            iconChange.setBounds(0, 0, 0, 0)
        }

        if (dX == 0F) {
            shapeBackground.setBounds(0, 0, 0, 0)
            iconDelete.setBounds(0, 0, 0, 0)
            iconChange.setBounds(0, 0, 0, 0)

        }

        shapeBackground.draw(c)
        iconDelete.draw(c)
        iconChange.draw(c)
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT)
    }
}