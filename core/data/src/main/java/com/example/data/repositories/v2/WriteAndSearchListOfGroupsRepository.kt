package com.example.data.repositories.v2

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.data.repositories.ScheduleApiRepository
import com.example.models.ui.ScheduleEntity
import com.example.models.ui.ScheduleGroupsListEntity
import com.example.utils.CustomExceptions
import com.example.utils.Result
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors
import javax.inject.Inject

interface WriteAndSearchListOfGroupsRepository {
    fun setText(charSequence: CharSequence)

    fun runTypeSubject()
    fun fetchGroup(groupName: String)

    fun getGroupsLiveData(): LiveData<Result<List<ScheduleGroupsListEntity.ScheduleGroupEntity>>>
    fun getFetchScheduleLiveData(): LiveData<Result<ScheduleEntity>>

    fun clear()
    class Impl @Inject constructor(
        private val scheduleApiRepository: ScheduleApiRepository
    ) : WriteAndSearchListOfGroupsRepository {

        private val disposables = CompositeDisposable()

        private val editTextSubject = PublishSubject.create<CharSequence>()
//        private fun getEditTextSubject(): Observable<CharSequence> = editTextSubject.hide().share()

        private val groupsSubject =
            BehaviorSubject.create<List<ScheduleGroupsListEntity.ScheduleGroupEntity>>()

//        private fun getGroupsSubject(): Observable<List<ScheduleGroupsListEntity.ScheduleGroupEntity>> =
//            groupsSubject.hide().share()

        private val _groupsLiveData =
            MutableLiveData<Result<List<ScheduleGroupsListEntity.ScheduleGroupEntity>>>(
                Result.Success(
                    emptyList()
                )
            )

        override fun getGroupsLiveData(): LiveData<Result<List<ScheduleGroupsListEntity.ScheduleGroupEntity>>> =
            _groupsLiveData

        private val _fetchScheduleLiveData = MutableLiveData<Result<ScheduleEntity>>()
        override fun getFetchScheduleLiveData(): LiveData<Result<ScheduleEntity>> =
            _fetchScheduleLiveData


        override fun setText(charSequence: CharSequence) = editTextSubject.onNext(charSequence)

        override fun runTypeSubject() {
            editTextSubject
                .doOnEach { _groupsLiveData.value = Result.Loading }
                .debounceIf({ it.length > 1 }, 450L, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.io())
                .switchMapSingle { string ->
                    scheduleApiRepository.fetchGroupListSingle(string.toString())
                        .map { listGroups -> listGroups.choices.sortedBy { group -> group.name } }
                        .doOnError{ Log.d("WriteAndSearchListOfGroupsRepository", "editTextSubject onError: $it")}
                        .onErrorReturn { emptyList() }
                }
                .subscribeBy(
                    onNext = { list ->
                        Log.d(
                            "WriteAndSearchListOfGroupsRepository",
                            "editTextSubject onNext: $list"
                        )
                        if (list.isNullOrEmpty())
                            groupsSubject.onNext(groupsSubject.value ?: emptyList())
                        else
                            groupsSubject.onNext(list)
                    },
                    onComplete = {},
                    onError = {

                    }
                ).addTo(disposables)

            Observable.combineLatest(editTextSubject, groupsSubject) { text, groups ->
                groups.stream().filter { group -> group.name.contains(text, ignoreCase = true) }
                    .limit(ITEMS_LIMIT).collect(
                        Collectors.toList()
                    )
            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onNext = { list -> _groupsLiveData.value = Result.Success(list) },
                onComplete = {},
                onError = {

                },
            )
                .addTo(disposables)
        }

        override fun fetchGroup(groupName: String) {
            _fetchScheduleLiveData.value = Result.Loading
            Observable.just(groupName)
                .switchMapSingle {
                    scheduleApiRepository.fetchScheduleByGroupNameSingle(it)
                        .onErrorResumeNext {
                            Single.just(groupName)
                                .flatMap { name -> scheduleApiRepository.fetchGroupListSingle(name) }
                                .map { groupList -> groupList.choices.sortedBy { group -> group.name } }
                                .flatMap { list ->
                                    scheduleApiRepository.fetchScheduleByHtmlSingle(
                                        (list.find { group -> group.name == groupName }
                                            ?: list.first()).group)
                                }
                        }
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError {
                    Log.d("WriteAndSearchListOfGroupsRepository", "fetchGroup onError: $it")
                }
                .subscribeBy(
                    onNext = { schedule ->
                        _fetchScheduleLiveData.value = Result.Success(schedule)
                    },
                    onComplete = {},
                    onError = {
                        when (it) {
                            is UnknownHostException ->
                                _fetchScheduleLiveData.value =
                                    Result.Error(Throwable("Отсутствует подключение к интернету"))

                            is NullPointerException ->
                                _fetchScheduleLiveData.value =
                                    Result.Error(Throwable("Не удалось получить группу. Возможно, группы не существует"))

                            else -> _fetchScheduleLiveData.value = Result.Error(it)
                        }
                    },
                ).addTo(disposables)
        }

        private fun <T> Observable<T>.debounceIf(
            predicate: (T) -> Boolean,
            timeout: Long,
            unit: TimeUnit,
        ): Observable<T> {
            return this.publish { sharedSrc ->
                Observable.merge(sharedSrc.debounce(timeout, unit).filter { predicate(it) },
                    sharedSrc.filter { !predicate(it) })
            }
        }

        override fun clear() = disposables.clear()

        companion object {
            private const val ITEMS_LIMIT: Long = 8
        }
    }
}