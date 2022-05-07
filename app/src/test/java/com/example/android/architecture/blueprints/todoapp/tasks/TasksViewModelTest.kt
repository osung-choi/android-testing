package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/*
AndroidX Test
 - 테스트용 라이브러리 모음
 - 테스트에서 사용하는 애플리케이션 컨텍스트 가져오는 함수 : ApplicationProvider.getApplicationContext()
 - 로컬 테스트와 계측 테스트 모두에서 작동하도록 빌드되었다.
    - 로컬 테스트에서 작성된 코드를 계측 테스트로 옮겨도 동일하게 실행 할 수 있다.
    - getApplicationContext는 로컬 또는 계측에 따라 약간 다르게 동작한다
      - 계층 테스트 : 애뮬레이터 또는 실제 장치에 연결할 때 실제 응용 프로그램 컨텍스트가 제공
      - 로컬 테스트 : 시뮬레이션 된 Android 환경을 제공

Robolectric
  - Robolectric은 로컬 테스트에서 시뮬레이션 된 Android 환경을 제공한다.
  - 테스트를 위해 시뮬레이션 된 Android 환경을 생성하고 애뮬레이터를 부팅하거나 기기에서 실행하는 것 보다 빠르게 실행되는 라이브러리이다.
  - @RunWith(AndroidJUnit4::class)
  - 로컬 또는 계측에 따라 약간 다르게ㅌ 동작한다.
    - 계층 테스트 : 실제 기기 또는 애뮬레이터
    - 로컬 테스트 : 시뮬레이션 된 Android

LiveData 를 테스트하기 위해선 두 가지를 수행하는 것이 좋다.
  - InstantTaskExecutorRule 사용
  - LiveData 관찰 보장

InstantTaskExecutorRule
  - 안드로이드 구성요소 관련 작업들을 모두 한 스레드에서 실행되게 한다.
  - 모든 작업이 synchronous 하게 동작 하여 테스팅을 원활하게 할 수 있다. (동기화 걱정 필요 없어진다.)
  - LiveData를 테스트한다면 필수적으로 사용해야 한다.
  - JUnit Rule은 test 전/후에 정의된 코드들이 실행되도록 만든다.

LiveData 테스트코드 관찰
LiveData는 LifecycleOwner, onChanged Action을 위한 Observer을 필요로 하지만
테스트에서는 Activity나 Fragment가 없기 때문에 Lifecycle과 무관하게 쓸 수 있는 observeForever()를 사용해야 한다.

val observer = Observer<Event<Unit>> {}

try {
    tasksViewModel.newTaskEvent.observeForever(observer)
} finally {
    tasksViewModel.newTaskEvent.removeObserver(observer)
}

하지만 LiveData를 테스트하고자 할 때 마다 등록/취소를 작성해야 하므로 보일러플레이트 코드가 생긴다.
따라서 JetPack 라이브러리고 제공되지는 않지만 구글에서는 LiveData<T>.getOrAwaitValue라는 Util 메소드를 가이드하고 있다.
https://medium.com/androiddevelopers/unit-testing-livedata-and-other-common-observability-problems-bb477262eb04
*/

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    //Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }


    @Test
    fun addNewTask_setsNewTakEvent() {
        // Given a fresh TasksViewModel
        // 반복되는 작업 @Before로 정의
        //val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        // When adding a new task
        tasksViewModel.addNewTask()

        // Then the new task event is triggered
        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {
        // Given a fresh ViewModel
        // 반복되는 작업 @Before로 정의
        //val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())\

        // When the filter type is All_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Then the "Add task" action is visible
        val value = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()

        assertThat(value, `is`(true))
    }
}