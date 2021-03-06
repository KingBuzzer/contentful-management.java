/*
 * Copyright (C) 2017 Contentful GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.contentful.java.cma

import com.contentful.java.cma.lib.ModuleTestUtils
import com.contentful.java.cma.lib.TestCallback
import com.contentful.java.cma.lib.TestUtils
import com.contentful.java.cma.model.CMASpace
import com.contentful.java.cma.model.CMAType
import okhttp3.mockwebserver.MockResponse
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.junit.Test as tests

class SpaceTests : BaseTest() {
    @org.junit.Test fun testCreate() {
        val requestBody = TestUtils.fileToString("space_create_request.json")
        val responseBody = TestUtils.fileToString("space_create_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().create(
                "xxx", TestCallback()) as TestCallback)!!

        assertEquals("spaceid", result.id)
        assertEquals("xxx", result.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @org.junit.Test fun testCreateInOrg() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        assertTestCallback(client!!.spaces().async().create(
                "whatever", "org", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals("org", recordedRequest.getHeader("X-Contentful-Organization"))
    }

    @org.junit.Test fun testCreateInOrgWithObject() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        assertTestCallback(client!!.spaces().async().create(
                CMASpace().setName("foo"), "org", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals("org", recordedRequest.getHeader("X-Contentful-Organization"))
    }

    @org.junit.Test fun testCreateWithObject() {
        server!!.enqueue(MockResponse().setResponseCode(200).setBody("{}"))

        assertTestCallback(client!!.spaces().async().create(
                CMASpace().setName("name").setDefaultLocale("my locale"),
                "org", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("POST", recordedRequest.method)
        assertEquals("/spaces", recordedRequest.path)
        assertEquals("org", recordedRequest.getHeader("X-Contentful-Organization"))
        assertEquals(32, recordedRequest.body.readUtf8().indexOf("my locale"))
    }

    @org.junit.Test fun testDelete() {
        val requestBody = "203"
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(requestBody))

        assertTestCallback(client!!.spaces().async().delete(
                "spaceid", TestCallback()) as TestCallback)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("DELETE", recordedRequest.method)
        assertEquals("/spaces/spaceid", recordedRequest.path)
    }

    @org.junit.Test fun testFetchAll() {
        val responseBody = TestUtils.fileToString("space_fetch_all_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async()
                .fetchAll(TestCallback()) as TestCallback)!!

        val items = result.items
        assertEquals(CMAType.Array, result.system.type)
        assertEquals(2, items.size)
        assertEquals(2, result.total)
        assertEquals(0, result.skip)
        assertEquals(25, result.limit)

        // Space #1
        var sys = items[0].system
        assertEquals(CMAType.Space, sys.type)
        assertEquals("id1", sys.id)
        assertEquals(1, sys.version)
        assertEquals("2014-03-21T08:43:52Z", sys.createdAt)
        assertEquals("2014-04-27T18:16:10Z", sys.updatedAt)
        assertEquals("space1", items[0].name)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("user1", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("user1", sys.updatedBy.system.id)

        // Space #2
        sys = items[1].system
        assertEquals(CMAType.Space, sys.type)
        assertEquals("id2", sys.id)
        assertEquals(2, sys.version)
        assertEquals("2014-05-19T09:00:27Z", sys.createdAt)
        assertEquals("2014-07-09T07:48:24Z", sys.updatedAt)
        assertEquals("space2", items[1].name)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("user2", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("user2", sys.updatedBy.system.id)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces?limit=100", request.path)
    }

    @org.junit.Test fun testFetchWithId() {
        val responseBody = TestUtils.fileToString("space_fetch_one_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().fetchOne(
                "spaceid", TestCallback()) as TestCallback)!!

        val sys = result.system
        assertEquals(CMAType.Space, sys.type)
        assertEquals("id1", sys.id)
        assertEquals(1, sys.version)
        assertEquals("2014-03-21T08:43:52Z", sys.createdAt)
        assertEquals("2014-04-27T18:16:10Z", sys.updatedAt)
        assertEquals("space1", result.name)

        // Created By
        assertEquals(CMAType.Link, sys.createdBy.system.type)
        assertEquals(CMAType.User, sys.createdBy.system.linkType)
        assertEquals("user1", sys.createdBy.system.id)

        // Updated By
        assertEquals(CMAType.Link, sys.updatedBy.system.type)
        assertEquals(CMAType.User, sys.updatedBy.system.linkType)
        assertEquals("user1", sys.updatedBy.system.id)

        // Request
        val request = server!!.takeRequest()
        assertEquals("GET", request.method)
        assertEquals("/spaces/spaceid", request.path)
    }

    @org.junit.Test fun testFetchAllLocales() {
        val responseBody = TestUtils.fileToString("space_fetch_locales_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        val result = assertTestCallback(client!!.spaces().async().fetchLocales(
                "spaceid", TestCallback()) as TestCallback)!!

        val item = result.items[0]
        assertEquals("U.S. English", item.name)
        assertEquals("en-US", item.code)
        assertNull(item.fallbackCode)
        assertTrue(item.isDefault)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("GET", recordedRequest.method)
        assertEquals("/spaces/spaceid/locales?limit=100", recordedRequest.path)
    }

    @org.junit.Test fun testUpdate() {
        val requestBody = TestUtils.fileToString("space_update_request.json")
        val responseBody = TestUtils.fileToString("space_update_response.json")
        server!!.enqueue(MockResponse().setResponseCode(200).setBody(responseBody))

        var space = gson!!.fromJson(
                TestUtils.fileToString("space_update_object.json"),
                CMASpace::class.java)

        space.name = "newname"

        space = assertTestCallback(client!!.spaces().async().update(
                space, TestCallback()) as TestCallback)

        assertEquals(2, space.system.version)
        assertEquals("newname", space.name)

        // Request
        val recordedRequest = server!!.takeRequest()
        assertEquals("PUT", recordedRequest.method)
        assertEquals("/spaces/spaceid", recordedRequest.path)
        assertNotNull(recordedRequest.getHeader("X-Contentful-Version"))
        assertEquals(requestBody, recordedRequest.body.readUtf8())
    }

    @org.junit.Test(expected = Exception::class)
    fun testUpdateFailsWithoutVersion() {
        ModuleTestUtils.assertUpdateWithoutVersion {
            val space: CMASpace = CMASpace().setName("name").setId("id")
            client!!.spaces().update(space)
        }
    }
}