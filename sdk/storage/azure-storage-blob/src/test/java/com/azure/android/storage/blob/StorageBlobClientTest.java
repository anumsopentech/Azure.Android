package com.azure.android.storage.blob;

import com.azure.android.core.http.Callback;
import com.azure.android.core.http.ServiceCallTask;
import com.azure.android.core.http.ServiceClient;
import com.azure.android.core.internal.util.serializer.SerializerFormat;
import com.azure.android.storage.blob.models.BlobDownloadResponse;
import com.azure.android.storage.blob.models.BlobGetPropertiesHeaders;
import com.azure.android.storage.blob.models.BlobGetPropertiesResponse;
import com.azure.android.storage.blob.models.BlobItem;
import com.azure.android.storage.blob.models.BlockBlobItem;
import com.azure.android.storage.blob.models.BlockBlobsCommitBlockListResponse;
import com.azure.android.storage.blob.models.BlockBlobsStageBlockResponse;
import com.azure.android.storage.blob.models.ContainersListBlobFlatSegmentResponse;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import okhttp3.ResponseBody;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class StorageBlobClientTest {
    private static final MockWebServer mockWebServer = new MockWebServer();
    private static final String BASE_URL = mockWebServer.url("/").toString();
    private static StorageBlobClient storageBlobClient = new StorageBlobClient.Builder(
        new ServiceClient.Builder()
            .setBaseUrl(BASE_URL)
            .setSerializationFormat(SerializerFormat.XML))
        .build();

    @After
    public void tearDown() throws InterruptedException {
        // For ensuring the responses enqueued are consumed before making the next call.
        mockWebServer.takeRequest(20, TimeUnit.MILLISECONDS);
    }

    @Test
    public void newBuilder() {
        // Given a StorageBlobClient.

        // When creating another client based on the first one.
        StorageBlobClient otherStorageBlobClient = storageBlobClient.newBuilder().build();

        // Then the new client will contain the same properties as the original.
        assertEquals(storageBlobClient.getBlobServiceUrl(), otherStorageBlobClient.getBlobServiceUrl());
    }

    @Test
    public void getBlobServiceUrl() {
        assertEquals(storageBlobClient.getBlobServiceUrl(), BASE_URL);
    }

    @Test
    public void getBlobsInPage() {
        // Given a StorageBlobClient.

        // When requesting a list of the blobs in a container using getBlobsInPage().
        String responseBody = readFileToString("ListBlobsResponse.xml");

        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody(responseBody);

        mockWebServer.enqueue(mockResponse);

        List<BlobItem> blobItems = storageBlobClient.getBlobsInPage(null,
            "testContainer",
            null);

        // Then a list containing the details of the blobs will be returned by the service and converted to BlobItem
        // objects by the client.
        assertNotEquals(0, blobItems.size());
        assertEquals("test.jpg", blobItems.get(0).getName());
    }

    @Test
    public void getBlobsInPage_withCallback() {
        // Given a StorageBlobClient.

        // When requesting a list of the blobs in a container using getBlobsInPage() while providing a callback.
        String responseBody = readFileToString("ListBlobsResponse.xml");

        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody(responseBody);

        mockWebServer.enqueue(mockResponse);

        ServiceCallTask serviceCallTask = storageBlobClient.getBlobsInPage(null,
            "testContainer",
            null,
            new Callback<List<BlobItem>>() {
                @Override
                public void onResponse(List<BlobItem> response) {
                    // Then a list containing the details of the blobs will be returned to the callback by the service
                    // and converted to BlobItem objects by the client.
                    assertNotEquals(0, response.size());
                    assertEquals("test.jpg", response.get(0).getName());
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    @Test
    public void getBlobsInPageWithRestResponse() {
        // Given a StorageBlobClient.

        // When requesting a list of the blobs in a container using getBlobsInPageWithRestResponse().
        String responseBody = readFileToString("ListBlobsResponse.xml");

        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody(responseBody);

        mockWebServer.enqueue(mockResponse);

        ContainersListBlobFlatSegmentResponse response =
            storageBlobClient.getBlobsInPageWithRestResponse(null,
                "testContainer",
                null,
                null,
                null,
                null,
                null);

        // Then the client will return an object that contains both the details of the REST response and a list
        // with the details of the blobs.
        List<BlobItem> blobItems = response.getValue().getSegment() == null
            ? new ArrayList<>(0)
            : response.getValue().getSegment().getBlobItems();

        assertEquals(200, response.getStatusCode());
        assertNotEquals(0, blobItems.size());
        assertEquals("test.jpg", blobItems.get(0).getName());
    }

    @Test
    public void getBlobsInPageWithRestResponse_withCallback() {
        // Given a StorageBlobClient.

        // When requesting a list of the blobs in a container using getBlobsInPageWithRestResponse() while providing a
        // callback.
        String responseBody = readFileToString("ListBlobsResponse.xml");

        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody(responseBody);

        mockWebServer.enqueue(mockResponse);

        ServiceCallTask serviceCallTask = storageBlobClient.getBlobsInPageWithRestResponse(null,
            "testContainer",
            null,
            null,
            null,
            null,
            null,
            new Callback<ContainersListBlobFlatSegmentResponse>() {
                @Override
                public void onResponse(ContainersListBlobFlatSegmentResponse response) {
                    // Then the client will return an object that contains both the details of the REST response and
                    // a list with the details of the blobs to the callback.
                    List<BlobItem> blobItems = response.getValue().getSegment() == null
                        ? new ArrayList<>(0)
                        : response.getValue().getSegment().getBlobItems();

                    assertEquals(200, response.getStatusCode());
                    assertNotEquals(0, blobItems.size());
                    assertEquals("test.jpg", blobItems.get(0).getName());
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    @Test
    public void getBlobProperties() {
        // Given a StorageBlobClient.

        // When requesting the properties of a blob using getBlobProperties().
        MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/text");

        mockWebServer.enqueue(response);

        // Then an object with the blob properties will be returned by the client.
        BlobGetPropertiesHeaders blobGetPropertiesHeaders = storageBlobClient.getBlobProperties("container",
            "blob");

        assertEquals("application/text", blobGetPropertiesHeaders.getContentType());
    }

    @Test
    public void getBlobProperties_withCallback() {
        // Given a StorageBlobClient.

        // When requesting the properties of a blob using getBlobProperties() while providing a callback.
        MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/text");

        mockWebServer.enqueue(response);

        ServiceCallTask serviceCallTask = storageBlobClient.getBlobProperties("container",
            "blob",
            new Callback<BlobGetPropertiesHeaders>() {
                @Override
                public void onResponse(BlobGetPropertiesHeaders response) {
                    // Then an object with the blob properties will be returned by the client to the callback.
                    assertEquals("application/text", response.getContentType());
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    @Test
    public void getBlobPropertiesWithRestResponse() {
        // Given a StorageBlobClient.

        // When requesting the properties of a blob using getBlobPropertiesAsHeaders() while providing a callback
        MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/text");

        mockWebServer.enqueue(response);

        // Then the client will return an object that contains both the details of the REST response and
        // a an object with the blob properties.
        BlobGetPropertiesResponse blobGetPropertiesResponse =
            storageBlobClient.getBlobPropertiesWithRestResponse("container",
                "blob",
                null,
                null,
                null,
                null,
                null,
                null);

        assertEquals("application/text", blobGetPropertiesResponse.getDeserializedHeaders().getContentType());
    }

    @Test
    public void getBlobPropertiesWithRestResponse_withCallback() {
        // Given a StorageBlobClient.

        // When requesting the properties of a blob using getBlobPropertiesWithRestResponse() while providing a
        // callback.
        MockResponse response = new MockResponse()
            .setResponseCode(200)
            .setHeader("Content-Type", "application/text");

        mockWebServer.enqueue(response);

        ServiceCallTask serviceCallTask = storageBlobClient.getBlobPropertiesWithRestResponse("container",
            "blob",
            null,
            null,
            null,
            null,
            null,
            null,
            new Callback<BlobGetPropertiesResponse>() {
                @Override
                public void onResponse(BlobGetPropertiesResponse response) {
                    // Then the client will return an object that contains both the details of the REST response and
                    // an object with the blob properties to the callback.
                    assertEquals("application/text", response.getDeserializedHeaders().getContentType());
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    @Test
    public void download() throws IOException {
        // Given a StorageBlobClient.

        // When requesting to download the contents of a blob using download().
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody("testBody");

        mockWebServer.enqueue(mockResponse);

        // Then an object with the blob's contents will be returned by the client.
        ResponseBody response = storageBlobClient.rawDownload(
            "testContainer",
            "testBlob");

        assertEquals("testBody", response.string());
    }

    @Test
    public void download_withCallback() {
        // Given a StorageBlobClient.

        // When requesting to download the contents of a blob using download() while providing a callback.
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody("testBody");

        mockWebServer.enqueue(mockResponse);

        ServiceCallTask serviceCallTask = storageBlobClient.rawDownload("testContainer",
            "testBlob",
            new Callback<ResponseBody>() {
                @Override
                public void onResponse(ResponseBody response) {
                    try {
                        // Then an object with the blob's contents will be returned by the client.
                        assertEquals("testBody", response.string());
                    } catch (IOException e) {
                        onFailure(e);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    @Test
    public void downloadWithRestResponse() throws IOException {
        // Given a StorageBlobClient.

        // When requesting to download the contents of a blob using downloadWithRestResponse().
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody("testBody");

        mockWebServer.enqueue(mockResponse);

        // Then an object with the blob's contents will be returned by the client, including its properties and
        // details from the REST response.
        BlobDownloadResponse response = storageBlobClient.rawDownloadWithRestResponse("testContainer",
            "testBlob",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(200, response.getStatusCode());
        assertEquals("testBody", response.getValue().string());
    }

    @Test
    public void downloadWithRestResponse_withCallback() {
        // Given a StorageBlobClient.

        // When requesting to download the contents of a blob using downloadWithRestResponse() while providing a
        // callback.
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(200)
            .setBody("testBody");

        mockWebServer.enqueue(mockResponse);

        ServiceCallTask serviceCallTask = storageBlobClient.rawDownloadWithRestResponse("testContainer",
            "testBlob",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            new Callback<BlobDownloadResponse>() {
                @Override
                public void onResponse(BlobDownloadResponse response) {
                    try {
                        // Then an object with the blob's contents will be returned by the client to the callback,
                        // including its properties and details from the REST response.
                        assertEquals(200, response.getStatusCode());
                        assertEquals("testBody", response.getValue().string());
                    } catch (IOException e) {
                        onFailure(e);
                    }
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    @Test
    public void stageBlock() {
        // Given a StorageBlobClient.

        // When sending a block's contents for staging using stageBlock().
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(201);

        mockWebServer.enqueue(mockResponse);

        // Then a response without body and status code 201 will be returned by the server.
        Void response = storageBlobClient.stageBlock("testContainer",
            "testBlob",
            null,
            new byte[0],
            null);

        assertNull(response);
    }

    @Test
    public void stageBlock_withCallback() {
        // Given a StorageBlobClient.

        // When sending a block's contents for staging.
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(201);

        mockWebServer.enqueue(mockResponse);

        ServiceCallTask serviceCallTask = storageBlobClient.stageBlock("testContainer",
            "testBlob",
            null,
            new byte[0],
            null,
            new Callback<Void>() {
                @Override
                public void onResponse(Void response) {
                    // Then a response without body and status code 201 will be returned by the server to the callback.
                    assertNull(response);
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    @Test
    public void stageBlockWithRestResponse() {
        // Given a StorageBlobClient.

        // When sending a block's contents for staging using stageBlockWithRestResponse().
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(201);

        mockWebServer.enqueue(mockResponse);

        // Then a response without body and status code 201 will be returned by the server.
        BlockBlobsStageBlockResponse response = storageBlobClient.stageBlockWithRestResponse("testContainer",
            "testBlob",
            null,
            new byte[0],
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(201, response.getStatusCode());
    }

    @Test
    public void stageBlockWithRestResponse_withCallback() {
        // Given a StorageBlobClient.

        // When sending a block's contents for staging using stageBlockWithRestResponse() while providing a callback.
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(201);

        mockWebServer.enqueue(mockResponse);

        ServiceCallTask serviceCallTask = storageBlobClient.stageBlockWithRestResponse("testContainer",
            "testBlob",
            null,
            new byte[0],
            null,
            null,
            null,
            null,
            null,
            null,
            new Callback<BlockBlobsStageBlockResponse>() {
                @Override
                public void onResponse(BlockBlobsStageBlockResponse response) {
                    // Then a response without body and status code 201 will be returned by the server to the callback.
                    assertEquals(201, response.getStatusCode());
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    @Test
    public void commitBlockList() {
        // Given a StorageBlobClient.

        // When committing a list of blocks for upload using commitBlockList().
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(201)
            .setHeader("x-ms-request-server-encrypted", false)
            .setHeader("ETag", "testEtag")
            .setBody("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

        mockWebServer.enqueue(mockResponse);

        // Then a response with the blob's details and status code 201 will be returned by the server.
        BlockBlobItem response = storageBlobClient.commitBlockList("testContainer",
            "testBlob",
            null,
            true);

        assertEquals(false, response.isServerEncrypted());
        assertEquals("testEtag", response.getETag());
    }

    @Test
    public void commitBlockList_withCallback() {
        // Given a StorageBlobClient.

        // When committing a list of blocks for upload using commitBlockList() while providing a callback.
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(201)
            .setHeader("x-ms-request-server-encrypted", false)
            .setHeader("ETag", "testEtag")
            .setBody("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

        mockWebServer.enqueue(mockResponse);

        ServiceCallTask serviceCallTask = storageBlobClient.commitBlockList("testContainer",
            "testBlob",
            null,
            true, new Callback<BlockBlobItem>() {
                @Override
                public void onResponse(BlockBlobItem response) {
                    // Then a response with the blob's details and status code 201 will be returned by the server to
                    // the callback.
                    assertEquals(false, response.isServerEncrypted());
                    assertEquals("testEtag", response.getETag());
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    @Test
    public void commitBlockListWithRestResponse() {
        // Given a StorageBlobClient.

        // When committing a list of blocks for upload using commitBlockListWithRestResponse().
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(201)
            .setHeader("x-ms-request-server-encrypted", false)
            .setHeader("ETag", "testEtag")
            .setBody("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

        mockWebServer.enqueue(mockResponse);

        // Then a response with the blob's details and status code 201 will be returned by the server.
        BlockBlobsCommitBlockListResponse response = storageBlobClient.commitBlockListWithRestResponse("testContainer",
            "testBlob",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null);

        assertEquals(false, response.getBlockBlobItem().isServerEncrypted());
        assertEquals("testEtag", response.getBlockBlobItem().getETag());
    }

    @Test
    public void commitBlockListWithRestResponse_withCallback() {
        // Given a StorageBlobClient.

        // When committing a list of blocks for upload using commitBlockListWithRestResponse() while providing a
        // callback.
        MockResponse mockResponse = new MockResponse()
            .setResponseCode(201)
            .setHeader("x-ms-request-server-encrypted", false)
            .setHeader("ETag", "testEtag")
            .setBody("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");

        mockWebServer.enqueue(mockResponse);

        ServiceCallTask serviceCallTask = storageBlobClient.commitBlockListWithRestResponse("testContainer",
            "testBlob",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null, new Callback<BlockBlobsCommitBlockListResponse>() {
                @Override
                public void onResponse(BlockBlobsCommitBlockListResponse response) {
                    // Then a response with the blob's details and status code 201 will be returned by the server to
                    // the callback.
                    assertEquals(false, response.getBlockBlobItem().isServerEncrypted());
                    assertEquals("testEtag", response.getBlockBlobItem().getETag());
                }

                @Override
                public void onFailure(Throwable t) {
                    throw new RuntimeException(t);
                }
            });

        // Also, a non-null ServiceCall object in a not canceled state will be returned by the client.
        assertNotNull(serviceCallTask);
        assertFalse(serviceCallTask.isCanceled());
    }

    private static String readFileToString(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream =
                 Files.lines(Paths.get("src", "test", "resources", filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return contentBuilder.toString();
    }
}
