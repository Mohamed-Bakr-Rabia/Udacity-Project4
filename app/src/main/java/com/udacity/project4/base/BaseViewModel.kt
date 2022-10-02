package com.udacity.project4.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.udacity.project4.utils.SingleLiveEvent

/**
 * Base class for View Models to declare the common LiveData objects in one place
 */
abstract class BaseViewModel(app: Application) : AndroidViewModel(app) {

    val navigationCommand: com.udacity.project4.utils.SingleLiveEvent<NavigationCommand> =
        com.udacity.project4.utils.SingleLiveEvent()
    val showErrorMessage: com.udacity.project4.utils.SingleLiveEvent<String> =
        com.udacity.project4.utils.SingleLiveEvent()
    val showSnackBar: com.udacity.project4.utils.SingleLiveEvent<String> =
        com.udacity.project4.utils.SingleLiveEvent()
    val showSnackBarInt: com.udacity.project4.utils.SingleLiveEvent<Int> =
        com.udacity.project4.utils.SingleLiveEvent()
    val showToast: com.udacity.project4.utils.SingleLiveEvent<String> =
        com.udacity.project4.utils.SingleLiveEvent()
    val showLoading: com.udacity.project4.utils.SingleLiveEvent<Boolean> =
        com.udacity.project4.utils.SingleLiveEvent()
    val showNoData: MutableLiveData<Boolean> = MutableLiveData()

}