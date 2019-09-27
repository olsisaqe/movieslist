package com.olsisaqe.movielist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.olsisaqe.movielist.model.api.MoviesData
import com.olsisaqe.movielist.model.api.MoviesDetail
import com.olsisaqe.movielist.model.ui.Movie
import com.olsisaqe.movielist.model.ui.Resource
import com.olsisaqe.movielist.model.ui.Resource.Status.LOADING
import com.olsisaqe.movielist.model.ui.Resource.Status.ERROR
import com.olsisaqe.movielist.model.ui.Resource.Status.SUCCESS
import com.olsisaqe.movielist.network.MoviesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @Mock
    private lateinit var moviesRepository: MoviesRepository
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var moviesData: MoviesData
    private lateinit var moviesDetail: MoviesDetail
    private val error = kotlin.Throwable()
    private val moviesEvents = mutableListOf<Resource<List<Movie>>>()

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        val json = Json(JsonConfiguration.Stable)
        javaClass.classLoader?.getResourceAsStream("movies_data.json")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?.let {
                moviesData = json.parse(MoviesData.serializer(), it)
            }
        javaClass.classLoader?.getResourceAsStream("movies_detail.json")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?.let {
                moviesDetail = json.parse(MoviesDetail.serializer(), it)
            }
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
        moviesEvents.clear()
    }

    @Test
    fun onSuccessReturnMovies() {
        initSuccess()

        val viewModel = MainViewModel(moviesRepository)
        viewModel.moviesLiveData.observeForever { moviesEvents.add(it) }

        verifyBlocking(moviesRepository, times(1)) {
            moviesDetail()
            moviesData()
        }
        assertEquals(moviesEvents.map { it.status }, listOf(LOADING, SUCCESS))
        val successMovieIds = moviesEvents[1].data?.map { it.movieId }
        val successMovieTitles = moviesEvents[1].data?.mapNotNull { it.title }
        val successMovieSubtitles = moviesEvents[1].data?.mapNotNull { it.subtitle }
        val successMoviePrice = moviesEvents[1].data?.mapNotNull { it.price }
        val successMovieImage = moviesEvents[1].data?.mapNotNull { it.image }

        assertTrue {
            successMovieIds?.containsAll(moviesData.movie_data.map { it.movie_id }) ?: false
        }
        assertTrue {
            successMovieIds?.containsAll(moviesDetail.movie_offers.map { it.movie_id }) ?: false
        }
        assertTrue {
            successMovieTitles?.containsAll(moviesData.movie_data.map { it.title }) ?: false
        }
        assertTrue {
            successMovieSubtitles?.containsAll(moviesData.movie_data.map { it.sub_title }) ?: false
        }
        assertTrue {
            successMoviePrice?.containsAll(moviesDetail.movie_offers.map { it.price }) ?: false
        }
        assertTrue {
            successMovieImage?.containsAll(moviesDetail.movie_offers
                .map { moviesDetail.image_base + it.image }
            ) ?: false
        }
    }

    @Test
    fun onRefreshReturnMovies() {
        initSuccess()
        val viewModel = MainViewModel(moviesRepository)
        viewModel.moviesLiveData.observeForever { moviesEvents.add(it) }
        runBlockingTest { viewModel.refresh() }

        verifyBlocking(moviesRepository, times(2)) {
            moviesDetail()
            moviesData()
        }

        assertEquals(moviesEvents.map { it.status }, listOf(LOADING, SUCCESS, LOADING, SUCCESS))

        val successMovieIds = moviesEvents[3].data?.map { it.movieId }
        val successMovieTitles = moviesEvents[3].data?.mapNotNull { it.title }
        val successMovieSubtitles = moviesEvents[3].data?.mapNotNull { it.subtitle }
        val successMoviePrice = moviesEvents[3].data?.mapNotNull { it.price }
        val successMovieImage = moviesEvents[3].data?.mapNotNull { it.image }

        assertTrue {
            successMovieIds?.containsAll(moviesData.movie_data.map { it.movie_id }) ?: false
        }
        assertTrue {
            successMovieIds?.containsAll(moviesDetail.movie_offers.map { it.movie_id }) ?: false
        }
        assertTrue {
            successMovieTitles?.containsAll(moviesData.movie_data.map { it.title }) ?: false
        }
        assertTrue {
            successMovieSubtitles?.containsAll(moviesData.movie_data.map { it.sub_title }) ?: false
        }
        assertTrue {
            successMoviePrice?.containsAll(moviesDetail.movie_offers.map { it.price }) ?: false
        }
        assertTrue {
            successMovieImage?.containsAll(moviesDetail.movie_offers
                .map { moviesDetail.image_base + it.image }
            ) ?: false
        }
    }

    private fun initSuccess() {
        moviesRepository.stub {
            onBlocking { moviesData() }.doReturn(moviesData)
            onBlocking { moviesDetail() }.doReturn(moviesDetail)
        }
    }

    private fun initError() {
        moviesRepository.stub {
            onBlocking { moviesData() }.doThrow(error)
            onBlocking { moviesDetail() }.doThrow(error)
        }
    }

}