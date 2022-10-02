package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()




    private val reminder1 = ReminderDataItem("Title1","Description1","Location1",1.0,1.0)
    private val reminders = mutableListOf<ReminderDTO>()
    private lateinit var fakeDataSource:FakeDataSource
    private lateinit var saveReminderViewModel: SaveReminderViewModel


    @Before
    fun setupViewModel(){
        fakeDataSource = FakeDataSource(reminders)
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)

        //Save Reminder
        saveReminderViewModel.validateAndSaveReminder(reminder1)
    }

    @Test
    fun testSaveReminderView(){

        runBlockingTest {
            var stringId1: String


            //test reminder
            stringId1 = (fakeDataSource.getReminder(reminder1.id) as Result.Success).data.id
            Assert.assertEquals(reminder1.id, stringId1)


        }

    }




}