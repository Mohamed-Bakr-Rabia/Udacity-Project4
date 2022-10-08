package com.udacity.project4.locationreminders.data.local

import android.app.Application
import androidx.arch.core.executor.TaskExecutorWithFakeMainThread
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.get
import org.koin.test.get



@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    //testing RemindersLocalRepository
    @Test
    fun insertionAndDeletingReminders(){
         runBlocking {
             val reminderDTO = ReminderDTO("Title1","Description1","Location1",1.0,1.0)

             //getting the real Repository
             val repository :ReminderDataSource = get().koin.get()

             //saveReminder
             repository.saveReminder(reminderDTO)

             //loadReminder
             val loadReminder = repository.getReminder(reminderDTO.id) as Result.Success

             //check if the loaded reminder equal the reminder we just created
             Assert.assertEquals(loadReminder.data,reminderDTO)


             //delete all reminder
             repository.deleteAllReminders()

             //load reminders from data source should be empty
             val loadAllReminder = repository.getReminders() as Result.Success

             //compare it to empty data list
             Assert.assertEquals(loadAllReminder.data, listOf<ReminderDTO>())
         }
    }
}