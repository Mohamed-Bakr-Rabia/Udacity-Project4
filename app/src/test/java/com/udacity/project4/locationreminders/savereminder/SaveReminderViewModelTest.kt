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

//Create a SaveReminderViewModelTest To Test saveReminder function inside A SaveReminderViewModel

class SaveReminderViewModelTest {

    //Create the reminder that will be save later using validateAndSaveReminder in the SaveReminderViewModel
    private val reminder1 = ReminderDataItem("Title1","Description1","Location1",1.0,1.0)
    private val reminder2 = ReminderDataItem("Title2","Description2","Location2",2.0,2.0)
    private val reminder3 = ReminderDataItem("Title3","Description3","Location3",3.0,3.0)

    private val reminders = mutableListOf<ReminderDTO>()

    //create fakeDataSource will be used by saveReminderViewModel
    private lateinit var fakeDataSource:FakeDataSource
    //create SaveReminderViewModel
    private lateinit var saveReminderViewModel: SaveReminderViewModel


    //initiate objects before the test
    @Before
    fun setupViewModel(){
        //initiate fakeDataSource and saveReminderViewModel
        fakeDataSource = FakeDataSource(reminders)
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)

    }

    @Test
    fun testSaveReminderView(){
        //call validateAndSaveReminder to save 3 reminders
        saveReminderViewModel.validateAndSaveReminder(reminder1)
        saveReminderViewModel.validateAndSaveReminder(reminder2)
        saveReminderViewModel.validateAndSaveReminder(reminder3)


        runBlockingTest {
            //load the data from the data source
            var list = ( fakeDataSource.getReminders() as Result.Success ).data


            //convert list from ReminderDTO objects to ReminderDataItem objects
            var reminderDataListItem = list.map {
                ReminderDataItem(it.title,it.description,it.location,it.latitude,it.longitude,it.id)

            }

            //compare the loaded list from the data source to the list we created
            Assert.assertEquals(reminderDataListItem, listOf(reminder1,reminder2,reminder3))

        }

    }

    @Test
    fun check_loading (){

        //save reminders to the datasource
        saveReminderViewModel.validateAndSaveReminder(reminder1)
        saveReminderViewModel.validateAndSaveReminder(reminder2)
        saveReminderViewModel.validateAndSaveReminder(reminder3)

        //observe the loading object
        val showLoading = saveReminderViewModel.showLoading.getOrAwaitValue()


        //make sure loading = false means it's not loading
        Assert.assertEquals(showLoading,false)

    }




}