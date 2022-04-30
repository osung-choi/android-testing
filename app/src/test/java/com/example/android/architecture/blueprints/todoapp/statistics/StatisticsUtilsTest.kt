package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.assertEquals
import org.junit.Test

//TDD 관련 Google I/O : https://www.youtube.com/watch?v=pK7W5npkhho&t=222s)
class StatisticsUtilsTest {

    @Test
    fun getActiveAndCompletedStats_noCompleted_returnHundredZero() {
        // Create an active task (작업 목록 만들기)
        val tasks = listOf(
            Task("title", "desc", isCompleted = false)
        )

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertEquals(result.activeTasksPercent, 100f)
        assertEquals(result.completedTasksPercent, 0f)

        // Hamcrest
        // 다양한 조건의 Match rule 을 손쉽게 작성하고 테스트 할 수 있는 library로
        // jUnit 이나 Mokoto 와 연계하여 사용할 수 있다.
        // Truth 라이브러리 도 비슷한 기능 (대체 가능)
        assertThat(result.activeTasksPercent, `is`(100f))
        assertThat(result.completedTasksPercent, `is`(0f))

        // 테스트 구조의 한 가지 방법은 Given, When, Then 규칙을 따르는 것이다.
        // Given : 테스트에 필요한 객체와 앱 상태를 설정 (활성화 된 작업 목록을 주입하는 과정)
        // When : 테스트 중인 개체에 대한 실제 작업을 수행한다. (getActiveAndCompletedStats 함수 호출)
        // Then : 테스트 통과 또는 실패 여부 판단. (다수의 assert 함수 호출)

        // 테스트 함수 명 규칙
        // 테스트중인 메소드 이름_조치 또는 입력_예상한 결과
        // getActiveAndCompletedStats(메소드명)_noCompleted(입력)_returnHundredZero(결과)
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnZero() {
        // Create an active task
        val tasks = emptyList<Task>()

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_noActive_returnZeroHundred() {
        // Create an active task
        val tasks = listOf(
            Task("title", "desc", isCompleted = true)
        )

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_error_returnZero() {
        // Create an active task
        val tasks = null

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_both_returnFortySixty() {
        val tasks = listOf(
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false),
        )

        // Call your function
        val result = getActiveAndCompletedStats(tasks)

        // Check the result
        assertThat(result.activeTasksPercent, `is`(40f))
        assertThat(result.completedTasksPercent, `is`(60f))
    }
}