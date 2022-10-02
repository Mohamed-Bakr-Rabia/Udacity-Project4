package com.udacity.project4.locationreminders.reminderslist

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var remindersListViewModel: RemindersListViewModel
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    private val reminder1 = ReminderDataItem("Title1","Description1","Location1",1.0,1.0)
    private val reminder2 = ReminderDataItem("Title2","Description2","Location2",2.0,2.0)
    private val reminder3 = ReminderDataItem("Title3","Description3","Location3",3.0,3.0)
    private val fakeList = listOf(reminder1,reminder2,reminder3)
    private val reminders = mutableListOf<ReminderDTO>()


    @Before
    fun setupViewModel(){

        fakeDataSource = FakeDataSource(reminders)
        remindersListViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)
        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)

    }

    @Test
    fun loadRemindersList_getFromDataSource(){


        //save reminder into fakeDateSource

            saveReminderViewModel.validateAndSaveReminder(reminder1)
            saveReminderViewModel.validateAndSaveReminder(reminder2)
            saveReminderViewModel.validateAndSaveReminder(reminder3)


        remindersListViewModel.loadReminders()

        //check_loading
        val loading = remindersListViewModel.showLoading.getOrAwaitValue()
        //check_list
        val reminderList = remindersListViewModel.remindersList.getOrAwaitValue()
        Assert.assertEquals(reminderList,fakeList)
        Assert.assertEquals(loading,true)

    }

    @Test
    fun loadReminderList_shouldReturnError(){
        remindersListViewModel.loadReminders()

        val error = remindersListViewModel.showSnackBar.getOrAwaitValue()

        Assert.assertEquals(error,"Reminders Empty")

    }

    @After
    fun tearDown(){
        stopKoin()
    }




}