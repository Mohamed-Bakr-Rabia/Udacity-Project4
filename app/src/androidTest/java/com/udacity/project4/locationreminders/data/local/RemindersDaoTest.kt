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
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get :Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: RemindersDatabase

    @Before
    fun init(){
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),RemindersDatabase::class.java).build()

    }

    @After
    fun closeDb() = database.close()


    @Test
    fun insertingAndRetrievingData () = runBlockingTest {
        //Given
        val reminderDTO = ReminderDTO("Title1", "Description1", "Location1", 1.0, 1.0)
        val reminderDTO2 = ReminderDTO("Title2", "Description2", "Location2", 2.0, 2.0)
        database.reminderDao().saveReminder(reminderDTO)

        //When
        val loaded = database.reminderDao().getReminderById(reminderDTO.id)
        val loadNotInserted = database.reminderDao().getReminderById(reminderDTO2.id)


        //then
        Assert.assertEquals(loaded?.id,reminderDTO.id)
        Assert.assertEquals(loaded?.title,reminderDTO.title)

        //whenDataNotFound
        Assert.assertEquals(loadNotInserted?.title,null)


    }
}