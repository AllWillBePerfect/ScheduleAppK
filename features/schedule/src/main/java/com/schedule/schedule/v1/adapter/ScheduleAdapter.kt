package com.schedule.schedule.v1.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.schedule.schedule.R
import com.schedule.schedule.databinding.ItemScheduleCurrentDayBinding
import com.schedule.schedule.databinding.ItemScheduleEmptyBinding
import com.schedule.schedule.databinding.ItemScheduleOtherDayBinding
import com.schedule.utils.DayOfWeek
import com.schedule.utils.ScheduleItem
import com.schedule.utils.StudyPeriod


class ScheduleAdapter(private val attrColors: AttrColors) :
    ListAdapter<ScheduleItem, RecyclerView.ViewHolder>(ScheduleItemDiffUtils()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_schedule_other_day -> {
                Log.d("onCreateViewHolder", "other viewholder")
                OtherDayViewHolder(
                    ItemScheduleOtherDayBinding.inflate(inflater, parent, false)
                )
            }

            R.layout.item_schedule_current_day -> {
                Log.d("onCreateViewHolder", "current viewholder")
                CurrentDayViewHolder(
                    ItemScheduleCurrentDayBinding.inflate(inflater, parent, false)
                )
            }

            R.layout.item_schedule_empty -> {
                Log.d("onCreateViewHolder", "empty viewholder")
                EmptyViewHolder(
                    ItemScheduleEmptyBinding.inflate(inflater, parent, false)
                )
            }

            else -> throw IllegalStateException("Unknown viewType in onCreateViewHolder ScheduleAdapter")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = currentList[position]) {
            is ScheduleItem.OtherDay -> (holder as OtherDayViewHolder).bind(
                item,
                position
            )

            is ScheduleItem.CurrentDay -> (holder as CurrentDayViewHolder).bind(
                item,
                position
            )

            is ScheduleItem.Empty -> {}
        }

    }

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is ScheduleItem.OtherDay -> R.layout.item_schedule_other_day
            is ScheduleItem.CurrentDay -> R.layout.item_schedule_current_day
            is ScheduleItem.Empty -> R.layout.item_schedule_empty
            else -> throw IllegalStateException("Unknown view type in ScheduleAdapter")
        }
    }

    inner class OtherDayViewHolder(val binding: ItemScheduleOtherDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.firstLesson.lessonItemLinearLayout.shapeAppearanceModel =
                    binding.firstLesson.lessonItemLinearLayout.shapeAppearanceModel.toBuilder()
                        .setTopLeftCornerSize(20F)
                        .setTopRightCornerSize(20F)
                        .build()

                binding.seventhLesson.lessonItemLinearLayout.shapeAppearanceModel =
                    binding.seventhLesson.lessonItemLinearLayout.shapeAppearanceModel.toBuilder()
                        .setBottomLeftCornerSize(20F)
                        .setBottomRightCornerSize(20F)
                        .build()
            }

        fun bind(item: ScheduleItem.OtherDay, itemIndex: Int) {

            when (itemIndex) {
                0 -> binding.weekDayTextView.text = DayOfWeek.MONDAY.titleRusShort
                1 -> binding.weekDayTextView.text = DayOfWeek.TUESDAY.titleRusShort
                2 -> binding.weekDayTextView.text = DayOfWeek.WEDNESDAY.titleRusShort
                3 -> binding.weekDayTextView.text = DayOfWeek.THURSDAY.titleRusShort
                4 -> binding.weekDayTextView.text = DayOfWeek.FRIDAY.titleRusShort
                5 -> binding.weekDayTextView.text = DayOfWeek.SATURDAY.titleRusShort
            }

            binding.firstLesson.timeTextView.text = StudyPeriod.FIRST_LESSON.fullTime
            binding.secondLesson.timeTextView.text = StudyPeriod.SECOND_LESSON.fullTime
            binding.thirdLesson.timeTextView.text = StudyPeriod.THIRD_LESSON.fullTime
            binding.fourthLesson.timeTextView.text = StudyPeriod.FOURTH_LESSON.fullTime
            binding.fifthLesson.timeTextView.text = StudyPeriod.FIFTH_LESSON.fullTime
            binding.sixthLesson.timeTextView.text = StudyPeriod.SIXTH_LESSON.fullTime
            binding.seventhLesson.timeTextView.text = StudyPeriod.SEVENTH_LESSON.fullTime

            binding.date.text = item.date
//            binding.weekDayTextView.text = item.dayOfWeekName

            binding.firstLesson.lessonTextView.text = item.lessonsList[1]
            binding.secondLesson.lessonTextView.text = item.lessonsList[2]
            binding.thirdLesson.lessonTextView.text = item.lessonsList[3]
            binding.fourthLesson.lessonTextView.text = item.lessonsList[4]
            binding.fifthLesson.lessonTextView.text = item.lessonsList[5]
            binding.sixthLesson.lessonTextView.text = item.lessonsList[6]
            binding.seventhLesson.lessonTextView.text = item.lessonsList[7]


        }


    }

    inner class CurrentDayViewHolder(val binding: ItemScheduleCurrentDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

            init {
                binding.firstLesson.lessonItemLinearLayout.shapeAppearanceModel =
                    binding.firstLesson.lessonItemLinearLayout.shapeAppearanceModel.toBuilder()
                        .setTopLeftCornerSize(20F)
                        .setTopRightCornerSize(20F)
                        .build()

                binding.seventhLesson.lessonItemLinearLayout.shapeAppearanceModel =
                    binding.seventhLesson.lessonItemLinearLayout.shapeAppearanceModel.toBuilder()
                        .setBottomLeftCornerSize(20F)
                        .setBottomRightCornerSize(20F)
                        .build()
            }

        fun bind(item: ScheduleItem.CurrentDay, itemIndex: Int) {


            /*if (this@ScheduleAdapter::lessonProgress.isInitialized) {
                when (lessonProgress.studyPeriod.periodCode) {
                    1 -> {
//                        binding.firstLesson.timeCardView.setCardBackgroundColor(Color.TRANSPARENT)
//                        binding.firstLesson.timeTextView.setTextColor(attrColors.textColorOther)
                        binding.firstLesson.progressIndicator.visibility = View.GONE
                    }

                    2 -> {
//                        binding.secondLesson.timeCardView.setCardBackgroundColor(Color.TRANSPARENT)
//                        binding.secondLesson.timeTextView.setTextColor(attrColors.textColorOther)
                        binding.secondLesson.progressIndicator.visibility = View.GONE
                    }

                    3 -> {
//                        binding.thirdLesson.timeCardView.setCardBackgroundColor(Color.TRANSPARENT)
//                        binding.thirdLesson.timeTextView.setTextColor(attrColors.textColorOther)
                        binding.thirdLesson.progressIndicator.visibility = View.GONE
                    }

                    4 -> {
//                        binding.fourthLesson.timeCardView.setCardBackgroundColor(Color.TRANSPARENT)
//                        binding.fourthLesson.timeTextView.setTextColor(attrColors.textColorOther)
                        binding.fourthLesson.progressIndicator.visibility = View.GONE
                    }

                    5 -> {
//                        binding.fifthLesson.timeCardView.setCardBackgroundColor(Color.TRANSPARENT)
//                        binding.fifthLesson.timeTextView.setTextColor(attrColors.textColorOther)
                        binding.fifthLesson.progressIndicator.visibility = View.GONE
                    }

                    6 -> {
//                        binding.sixthLesson.timeCardView.setCardBackgroundColor(Color.TRANSPARENT)
//                        binding.sixthLesson.timeTextView.setTextColor(attrColors.textColorOther)
                        binding.sixthLesson.progressIndicator.visibility = View.GONE
                    }

                    7 -> {
//                        binding.seventhLesson.timeCardView.setCardBackgroundColor(Color.TRANSPARENT)
//                        binding.seventhLesson.timeTextView.setTextColor(attrColors.textColorOther)
                        binding.seventhLesson.progressIndicator.visibility = View.GONE
                    }

                    11 -> {
                        binding.firstLessonBreak.linearLayout.visibility = View.GONE
                    }

                    12 -> {
                        binding.secondLessonBreak.linearLayout.visibility = View.GONE
                    }

                    13 -> {
                        binding.thirdLessonBreak.linearLayout.visibility = View.GONE
                    }

                    14 -> {
                        binding.fourthLessonBreak.linearLayout.visibility = View.GONE
                    }

                    15 -> {
                        binding.fifthLessonBreak.linearLayout.visibility = View.GONE
                    }

                    16 -> {
                        binding.sixthLessonBreak.linearLayout.visibility = View.GONE
                    }
                }
            }*/

            binding.firstLesson.progressIndicator.visibility = View.GONE
            binding.secondLesson.progressIndicator.visibility = View.GONE
            binding.thirdLesson.progressIndicator.visibility = View.GONE
            binding.fourthLesson.progressIndicator.visibility = View.GONE
            binding.fifthLesson.progressIndicator.visibility = View.GONE
            binding.sixthLesson.progressIndicator.visibility = View.GONE
            binding.seventhLesson.progressIndicator.visibility = View.GONE

            binding.firstLessonBreak.linearLayout.visibility = View.GONE
            binding.secondLessonBreak.linearLayout.visibility = View.GONE
            binding.thirdLessonBreak.linearLayout.visibility = View.GONE
            binding.fourthLessonBreak.linearLayout.visibility = View.GONE
            binding.fifthLessonBreak.linearLayout.visibility = View.GONE
            binding.sixthLessonBreak.linearLayout.visibility = View.GONE

            binding.beforeLesson.linearLayout.visibility = View.GONE
            binding.afterLesson.linearLayout.visibility = View.GONE

            val lessonProgress = item.lessonProgress
            Log.d("ScheduleAdapter currentItemBind", "bind ${item.lessonProgress}")


            when (item.lessonProgress.studyPeriod.periodCode) {
                1 -> {
//                    binding.firstLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.firstLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.firstLesson.progressIndicator.progress = lessonProgress.progressValue
                    binding.firstLesson.progressIndicator.visibility = View.VISIBLE
                }

                2 -> {
//                    binding.secondLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.secondLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.secondLesson.progressIndicator.progress = lessonProgress.progressValue
                    binding.secondLesson.progressIndicator.visibility = View.VISIBLE
                }

                3 -> {
//                    binding.thirdLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.thirdLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.thirdLesson.progressIndicator.progress = lessonProgress.progressValue
                    binding.thirdLesson.progressIndicator.visibility = View.VISIBLE
                }

                4 -> {
//                    binding.thirdLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.thirdLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.fourthLesson.progressIndicator.progress = lessonProgress.progressValue
                    binding.fourthLesson.progressIndicator.visibility = View.VISIBLE
                }

                5 -> {
//                    binding.fifthLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.fifthLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.fifthLesson.progressIndicator.progress = lessonProgress.progressValue
                    binding.fifthLesson.progressIndicator.visibility = View.VISIBLE
                }

                6 -> {
//                    binding.sixthLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.sixthLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.sixthLesson.progressIndicator.progress = lessonProgress.progressValue
                    binding.sixthLesson.progressIndicator.visibility = View.VISIBLE
                }

                7 -> {
//                    binding.seventhLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.seventhLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.seventhLesson.progressIndicator.progress = lessonProgress.progressValue
                    binding.seventhLesson.progressIndicator.visibility = View.VISIBLE
                }

                11 -> {
//                    binding.seventhLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.seventhLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.firstLessonBreak.progressIndicator.progress =
                        lessonProgress.progressValue
                    binding.firstLessonBreak.linearLayout.visibility = View.VISIBLE

                }

                12 -> {
//                    binding.seventhLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.seventhLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.secondLessonBreak.progressIndicator.progress =
                        lessonProgress.progressValue
                    binding.secondLessonBreak.linearLayout.visibility = View.VISIBLE
                }

                13 -> {
//                    binding.seventhLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.seventhLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.thirdLessonBreak.progressIndicator.progress =
                        lessonProgress.progressValue
                    binding.thirdLessonBreak.linearLayout.visibility = View.VISIBLE
                }

                14 -> {
//                    binding.seventhLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.seventhLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.fourthLessonBreak.progressIndicator.progress =
                        lessonProgress.progressValue
                    binding.fourthLessonBreak.linearLayout.visibility = View.VISIBLE
                }

                15 -> {
//                    binding.seventhLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.seventhLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.fifthLessonBreak.progressIndicator.progress =
                        lessonProgress.progressValue
                    binding.fifthLessonBreak.linearLayout.visibility = View.VISIBLE
                }

                16 -> {
//                    binding.seventhLesson.timeCardView.setCardBackgroundColor(attrColors.colorPrimary)
//                    binding.seventhLesson.timeTextView.setTextColor(attrColors.textColorCurrent)
                    binding.sixthLessonBreak.progressIndicator.progress =
                        lessonProgress.progressValue
                    binding.sixthLessonBreak.linearLayout.visibility = View.VISIBLE
                }

                21 -> {

                    binding.beforeLesson.time.text = StudyPeriod.BEFORE_LESSONS.fullTime
                    binding.beforeLesson.title.text = "Занятия скоро начнутся"

                    binding.beforeLesson.progressIndicator.progress =
                        lessonProgress.progressValue
                    binding.beforeLesson.linearLayout.visibility = View.VISIBLE
                }

                22 -> {

                    binding.afterLesson.progressIndicator.progress =
                        lessonProgress.progressValue
                    binding.afterLesson.linearLayout.visibility = View.VISIBLE
                }


            }

            when (itemIndex) {
                0 -> binding.weekDayTextView.text = DayOfWeek.MONDAY.titleRus
                1 -> binding.weekDayTextView.text = DayOfWeek.TUESDAY.titleRus
                2 -> binding.weekDayTextView.text = DayOfWeek.WEDNESDAY.titleRus
                3 -> binding.weekDayTextView.text = DayOfWeek.THURSDAY.titleRus
                4 -> binding.weekDayTextView.text = DayOfWeek.FRIDAY.titleRus
                5 -> binding.weekDayTextView.text = DayOfWeek.SATURDAY.titleRus
            }


            binding.firstLesson.timeTextView.text = StudyPeriod.FIRST_LESSON.fullTime
            binding.secondLesson.timeTextView.text = StudyPeriod.SECOND_LESSON.fullTime
            binding.thirdLesson.timeTextView.text = StudyPeriod.THIRD_LESSON.fullTime
            binding.fourthLesson.timeTextView.text = StudyPeriod.FOURTH_LESSON.fullTime
            binding.fifthLesson.timeTextView.text = StudyPeriod.FIFTH_LESSON.fullTime
            binding.sixthLesson.timeTextView.text = StudyPeriod.SIXTH_LESSON.fullTime
            binding.seventhLesson.timeTextView.text = StudyPeriod.SEVENTH_LESSON.fullTime

            binding.firstLessonBreak.timeTextView.text = StudyPeriod.FIRST_LESSON_BREAK.fullTime
            binding.secondLessonBreak.timeTextView.text = StudyPeriod.SECOND_LESSON_BREAK.fullTime
            binding.thirdLessonBreak.timeTextView.text = StudyPeriod.THIRD_LESSON_BREAK.fullTime
            binding.fourthLessonBreak.timeTextView.text = StudyPeriod.FOURTH_LESSON_BREAK.fullTime
            binding.fifthLessonBreak.timeTextView.text = StudyPeriod.FIFTH_LESSON_BREAK.fullTime
            binding.sixthLessonBreak.timeTextView.text = StudyPeriod.SIXTH_LESSON_BREAK.fullTime

//            viewStubBinding!!.time.text = StudyPeriod.BEFORE_LESSONS.fullTime
//            viewStubBinding!!.title.text = "Занятия скоро начнутся"
            binding.afterLesson.time.text = StudyPeriod.AFTER_LESSONS.fullTime
            binding.afterLesson.title.text = "На сегодня занятия закончились"

            binding.date.text = item.date
//            binding.weekDayTextView.text = item.dayOfWeekName

            binding.firstLesson.lessonTextView.text = item.lessonsList[1]
            binding.secondLesson.lessonTextView.text = item.lessonsList[2]
            binding.thirdLesson.lessonTextView.text = item.lessonsList[3]
            binding.fourthLesson.lessonTextView.text = item.lessonsList[4]
            binding.fifthLesson.lessonTextView.text = item.lessonsList[5]
            binding.sixthLesson.lessonTextView.text = item.lessonsList[6]
            binding.seventhLesson.lessonTextView.text = item.lessonsList[7]


        }

    }


    inner class EmptyViewHolder(val binding: ItemScheduleEmptyBinding) :
        RecyclerView.ViewHolder(binding.root) {}


    private class ScheduleItemDiffUtils : DiffUtil.ItemCallback<ScheduleItem>() {
        override fun areItemsTheSame(oldItem: ScheduleItem, newItem: ScheduleItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ScheduleItem, newItem: ScheduleItem): Boolean =
            oldItem == newItem

        /*override fun getChangePayload(oldItem: ScheduleItem, newItem: ScheduleItem): Any? {
            return if (oldItem is ScheduleItem.CurrentDay && newItem is ScheduleItem.CurrentDay) {
                oldItem.lessonProgress != newItem.lessonProgress
            } else {
                oldItem.text != newItem.text
            }

        }*/
    }

    data class AttrColors(
        @ColorInt val colorPrimary: Int,
        @ColorInt val textColorOther: Int,
        @ColorInt val textColorCurrent: Int,
    )
}
