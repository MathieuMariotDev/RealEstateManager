package com.openclassrooms.realestatemanager


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.openclassrooms.realestatemanager.ui.realEstate.MainActivity
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class TextFieldErrorTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        val actionMenuItemView = onView(
            allOf(
                withId(R.id.realestate_add), withContentDescription("Add"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.materialToolbar),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        actionMenuItemView.perform(click())
    }

    @Test
    fun checkDisplayErrorEmptyTxt() { //Work
        val extendedFloatingActionButton = onView(
            allOf(
                withId(R.id.Button_add), withText("Validation"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.create_container),
                        0
                    ),
                    5
                )
            )
        )
        extendedFloatingActionButton.perform(scrollTo(), click())
        onView(withId(R.id.textField_nbBathrooms)).check(matches(hasDescendant(withText("This field must be completed"))))
    }

    @Test
    fun checkDisplayErrorIsNotNumber() {

        val textInputEditText = onView(
            allOf(withId(R.id.textview_nbRooms))
        )

        textInputEditText.perform(click())

        textInputEditText.perform(replaceText("a"), closeSoftKeyboard())

        val extendedFloatingActionButton = onView(
            allOf(
                withId(R.id.Button_add), withText("Validation"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.create_container),
                        0
                    ),
                    5
                )
            )
        )
        extendedFloatingActionButton.perform(scrollTo(), click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
