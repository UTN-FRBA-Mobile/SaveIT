package com.example.saveit

import androidx.fragment.app.Fragment

interface NavegacionInterface {
    /**
     * Trigger a navigation to the specified fragment, optionally adding a transaction to the back
     * stack to make this navigation reversible.
     */
    fun showFragment(fragment: Fragment, addToBackstack: Boolean)
}