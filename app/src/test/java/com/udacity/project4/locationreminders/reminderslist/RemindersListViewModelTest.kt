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
//Create a RemindersListViewModelTest To Test The Function inside A RemindersListViewModel
class RemindersListViewModelTest {

    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var remindersListViewModel: RemindersListViewModel
    private lateinit var saveReminderViewModel: SaveReminderViewModel

    //initiate that will show in Ui
    private val reminder1 = ReminderDataItem("Title1","Description1","Location1",1.0,1.0)
    private val reminder2 = ReminderDataItem("Title2","Description2","Location2",2.0,2.0)
    private val reminder3 = ReminderDataItem("Title3","Description3","Location3",3.0,3.0)


    private val fakeList = listOf(reminder1,reminder2,reminder3)
    private val reminders = mutableListOf<ReminderDTO>()

    //initiate objects before the test
    @Before
    fun setupViewModel(){
        //initiate a fake data source for the reminder
        fakeDataSource = FakeDataSource(reminders)

        //initiate remindersListViewModel to test the load function
        remindersListViewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)

        //initiate SaveReminderViewModel to save the data inside of a fake date source
        // so it can be loaded later from the remindersListViewModel

        saveReminderViewModel = SaveReminderViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)

    }

    //Test the load function inside a RemindersListViewModel see if it load the reminder in th Ui
    @Test
    fun loadRemindersList_getFromDataSource(){


        //First save 3 reminder in the local fake data source

            saveReminderViewModel.validateAndSaveReminder(reminder1)
            saveReminderViewModel.validateAndSaveReminder(reminder2)
            saveReminderViewModel.validateAndSaveReminder(reminder3)

        //test the load data function see if it load the data that i save
        remindersListViewModel.loadReminders()


        //observe the reminder list that will be show in the ui
        // see if it has the save list that we added
        val reminderList = remindersListViewModel.remindersList.getOrAwaitValue()

        //test the results
        Assert.assertEquals(reminderList,fakeList)


    }

    //test loadReminder see if it will return error if the list was empty
    @Test
    fun loadReminderList_shouldReturnError(){

        //load the data the we didn't save
        remindersListViewModel.loadReminders()

        //observe the showSnackBar live data see if it change to Reminders Empty
        val error = remindersListViewModel.showSnackBar.getOrAwaitValue()

        //test the results
        Assert.assertEquals(error,"Reminders Empty")

    }

    //call stopKoin after the test to Stop the current Koin application
    @After
    fun tearDown(){
        stopKoin()
    }




}