
package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource (var reminders: MutableList<ReminderDTO>): ReminderDataSource {


    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        reminders.let {
            if (it.isNotEmpty())
            return Result.Success(it)
        }
        return Result.Error("Reminders Empty")

    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        reminders.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {

         reminders.forEach{
              if (it.id == id )
                  return  Result.Success(it)

        }
        return Result.Error("Reminder Not Found")

    }

    override suspend fun deleteAllReminders() {
        reminders.clear()
    }


}