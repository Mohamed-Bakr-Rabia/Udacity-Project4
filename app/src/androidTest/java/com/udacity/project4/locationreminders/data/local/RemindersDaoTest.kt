package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)

//Testing for the reminder dao that will test the database
@SmallTest
class RemindersDaoTest {

    @get :Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    //create the database object
    private lateinit var database: RemindersDatabase

    @Before
    fun init(){
        //initiate the database in memory for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java)
            .allowMainThreadQueries()
            .build()

    }

    @After
    //close database after finish testing
    fun closeDb() = database.close()


    @Test
    //testing the insertion and retrieving data
    fun insertingAndRetrievingDataById () = runBlockingTest {

        //GIVEN
        // create 2 reminder the will be saved in the database
        val reminderDTO = ReminderDTO("Title1", "Description1", "Location1", 1.0, 1.0)
        val reminderDTO2 = ReminderDTO("Title2", "Description2", "Location2", 2.0, 2.0)


        //THEN
        //save the 2 reminder in the database
        database.reminderDao().saveReminder(reminderDTO)
        database.reminderDao().saveReminder(reminderDTO2)



        //get 1 reminder by id and compare with the reminder we created
        val loaded = database.reminderDao().getReminderById(reminderDTO.id)
        Assert.assertEquals(loaded?.id,reminderDTO.id)

    }

    @Test
    fun checkDataNotFound() = runBlockingTest {
        //Will check with empty list
        Assert.assertEquals(database.reminderDao().getReminders(),listOf<ReminderDTO>())
    }
}