package com.samir.viewmodels

import com.samir.core.BaseResponse
import com.samir.core.User
import com.samir.domain.repo.LoginRepository
import com.samir.domain.state.UiState
import com.samir.domain.usercase.LoginUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel
    private lateinit var loginUseCase: LoginUseCase
    private val repository = FakeLoginRepository()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        loginUseCase = LoginUseCase(repository)
        viewModel = LoginViewModel(loginUseCase)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() {
        assertEquals(UiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `login success updates state to Success`() = runTest {
        val user = User(1, "test@email.com", "First", "Last", "123456789")
        repository.loginResult = BaseResponse(true, "Success", user)

        viewModel.login("test@email.com", "password")
        
        // Advance until idle to process the coroutine
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals(user, (state as UiState.Success).data.data)
        assertTrue(state.data.success)
    }

    @Test
    fun `login with invalid credentials returns unsuccessful response`() = runTest {
        repository.loginResult = BaseResponse(false, "Invalid credentials", null)

        viewModel.login("wrong@email.com", "wrong")
        
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Loading)
        assertEquals(false, (state as UiState.Success).data.success)
        assertEquals("Invalid credentials", state.data.message)
    }

    @Test
    fun `login failure due to exception updates state to Error`() = runTest {
        repository.shouldThrowException = true

        viewModel.login("test@email.com", "password")
        
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Error)
        assertEquals("Login failed", (state as UiState.Error).message)
    }

    private class FakeLoginRepository : LoginRepository {
        var loginResult: BaseResponse<User>? = null
        var shouldThrowException = false

        override suspend fun login(email: String, password: String): BaseResponse<User> {
            if (shouldThrowException) {
                throw Exception("Login failed")
            }
            return loginResult ?: BaseResponse(false, "Unknown", null)
        }
    }
}
