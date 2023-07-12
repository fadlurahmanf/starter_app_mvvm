package com.fadlurahmanf.starterappmvvm.feature.sp.data.storage

import com.github.ivanshafran.sharedpreferencesmock.SPMockBuilder
import org.junit.Before
import org.junit.Test

class ExampleSpStorageTest {
    private lateinit var storage: ExampleSpStorage
    private val spMock: SPMockBuilder = SPMockBuilder()
    @Before
    fun before() {
        storage = ExampleSpStorage(spMock.createContext())
    }


    @Test
    fun tes() {
        storage.exampleRawString = "TES RAW"
    }
}