package com.udacity.project4.locationreminders.reminderslist

import android.content.Context
import android.os.Bundle
import android.provider.CalendarContract.Reminders
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers
import org.hamcrest.Matchers.not
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//Testing the ReminderListFragment
@MediumTest
class ReminderListFragmentTest {
    @Test
    fun reminders_DisplayedInUi(){

        //GIVEN - Create Reminder That will Be displayed in the fragment
        val reminder = ReminderDataItem("Title1","Description1","Location1",1.0,1.0)




        //WHEN - launch the Fragment
        val fragment = launchFragmentInContainer<ReminderListFragment>(null,R.style.AppTheme)

        //create mock nav controller to mock the navigation
        val navController = mock(NavController::class.java)

        //add the mock nav controller to the fragment view
        fragment.onFragment{
            Navigation.setViewNavController(it.view!!,navController)
        }
        // add the reminder that we created to the reminder list to be displayed in th UI Fragment
        fragment.withFragment {
            this._viewModel.remindersList.value = listOf(reminder)
        }
        //perform click to the FAB button to test the navigation
        onView(withId(R.id.addReminderFAB)).perform(click())





        //THEN
        //verify the navigation to the SaveReminderFragment
        verify(navController).navigate(
            ReminderListFragmentDirections.toSaveReminder()
        )


    }
}