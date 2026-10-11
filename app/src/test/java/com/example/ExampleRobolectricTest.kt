package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.EduRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Padho Bhai", appName)
    }

    @Test
    fun `verify course syllabus and ray optics lectures`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val repository = EduRepository(context)
        val batch = repository.getMainBatch()

        assertEquals("PARISHRAM 2026 (Class 12th)", batch.title)
        assertTrue(batch.subjects.isNotEmpty())

        val physics = batch.subjects.firstOrNull { it.key == "physics" }
        assertNotNull(physics)

        val rayOptics = physics?.chapters?.firstOrNull { it.name == "Ray Optics and Optical Instruments" }
        assertNotNull(rayOptics)
        assertEquals(10, rayOptics?.lectures?.size)
    }
}
