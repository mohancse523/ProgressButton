package com.nihaskalam.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Nihas Kalam on 21/12/16.
 */

public class Utils {

    public static Matcher<View> withCompoundDrawable(final int resourceId) {
        return new BoundedMatcher<View, Button>(Button.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has compound drawable resource " + resourceId);
            }

            @Override
            public boolean matchesSafely(Button textView) {
                for (Drawable drawable : textView.getCompoundDrawables()) {
                    if (sameBitmap(textView.getContext(), drawable, resourceId)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Matcher<View> withBackground(final int resourceId) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                return sameBitmap(view.getContext(), view.getBackground(), resourceId);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has background resource " + resourceId);
            }
        };
    }

    public static boolean sameBitmap(Context context, Drawable drawable, int resourceId) {
        Drawable otherDrawable = ContextCompat.getDrawable(context, resourceId);
        if (drawable == null || otherDrawable == null) {
            return false;
        }
        if (drawable instanceof StateListDrawable && otherDrawable instanceof StateListDrawable) {
            drawable = drawable.getCurrent();
            otherDrawable = otherDrawable.getCurrent();
        }
        if (drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            Bitmap otherBitmap = ((BitmapDrawable) otherDrawable).getBitmap();
            return bitmap.sameAs(otherBitmap);
        }
        return false;
    }

    public static void clickAndShowIdleState(String text,int id) throws InterruptedException {
        Espresso.onView(withId(id)).perform(click());
        Thread.sleep(Constants.MORPH_DURATION);
        Espresso.onView(withId(R.id.circularButton2)).check(matches(withText(text)));
    }

    public static void doProgress(String text) throws InterruptedException {
        Espresso.onView(allOf(withId(R.id.button), withText(text))).perform(click());
        Thread.sleep(Constants.TIME_GAP_BETWEEN_MANUAL_PROGRESS);
    }
}
