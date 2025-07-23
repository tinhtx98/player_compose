package com.tinhtx.player.common

import org.junit.Test
import org.junit.Assert.*

class ResourceTest {

    @Test
    fun `Success resource should contain data`() {
        val data = "test data"
        val resource = Resource.Success(data)

        assertEquals(data, resource.data)
        assertNull(resource.message)
    }

    @Test
    fun `Error resource should contain message`() {
        val message = "error message"
        val resource = Resource.Error<String>(message)

        assertEquals(message, resource.message)
        assertNull(resource.data)
    }

    @Test
    fun `Loading resource can contain data`() {
        val data = "loading data"
        val resource = Resource.Loading(data)

        assertEquals(data, resource.data)
        assertNull(resource.message)
    }
}
