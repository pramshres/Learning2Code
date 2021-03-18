package com.example.googlemap.suite

import com.example.googlemap.ForholdInstrumentationTest
import com.example.googlemap.FrontpageFragInstrumentationTest
import com.example.googlemap.KorttidInstrumentationTest
import com.example.googlemap.LangtidInstrumentationTest
import com.example.googlemap.categoryRepo.CatRepoTest
import com.example.googlemap.categoryRepo.CategoryActivityTest
import org.junit.runner.RunWith
import org.junit.runners.Suite


@RunWith(Suite::class)
@Suite.SuiteClasses(

    ForholdInstrumentationTest::class,
    FrontpageFragInstrumentationTest::class,
    KorttidInstrumentationTest::class,
    LangtidInstrumentationTest::class,
    CatRepoTest::class,
    CategoryActivityTest::class

)
class TestSuite {




}