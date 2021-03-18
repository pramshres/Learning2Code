package com.example.googlemap.categoryRepo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.googlemap.category.CategoryListRepository
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CatRepoTest {

    @Test
    fun assert_if_null() {

        val catRepoInstance = CategoryListRepository
        Assert.assertNotNull(catRepoInstance.getCategoryList())

    }


    @Test
    fun create_new_cat_repo_test() {

        val catRepoInstance = CategoryListRepository
        Assert.assertTrue((catRepoInstance.getCategoryList().value!!.size >0))

    }

}