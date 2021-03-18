package com.example.googlemap.categoryRepo


import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.googlemap.category.CategoryFrag
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryActivityTest {

    @Test
    fun assert_companion_object_not_null() {

        val instance = CategoryFrag
        Assert.assertNotNull(instance)
    }
}