package com.udacity.project4.locationreminders.reminderslist

import android.content.Context
import android.os.Bundle
import android.provider.CalendarContract.Reminders
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {
    @Test
    fun reminders_DisplayedInUi(){

        //Given reminder to display
        val reminder = ReminderDataItem("Title1","Description1","Location1",1.0,1.0)
        val fragment= launchFragmentInContainer<ReminderListFragment>(null,R.style.AppTheme)
        val navController = mock(NavController::class.java)



        fragment.onFragment{
            Navigation.setViewNavController(it.view!!,navController)
        }

        //  testing the displayed data on the UI.
        fragment.withFragment {
            this._viewModel.remindersList.value = listOf(reminder)
        }


        //testing the navigation of the fragments.
        onView(withId(R.id.addReminderFAB)).perform(click())
        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )


        Thread.sleep(3000)
    }
}