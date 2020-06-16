// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.storage.blob;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.NetworkType;

import com.azure.android.core.http.Callback;
import com.azure.android.core.http.ServiceCall;
import com.azure.android.core.http.ServiceClient;
import com.azure.android.core.http.interceptor.AddDateInterceptor;
import com.azure.android.core.internal.util.serializer.SerializerFormat;
import com.azure.android.core.util.CoreUtil;
import com.azure.android.storage.blob.models.AccessTier;
import com.azure.android.storage.blob.models.BlobDownloadAsyncResponse;
import com.azure.android.storage.blob.models.BlobGetPropertiesHeaders;
import com.azure.android.storage.blob.models.BlobHttpHeaders;
import com.azure.android.storage.blob.models.BlobItem;
import com.azure.android.storage.blob.models.BlobRange;
import com.azure.android.storage.blob.models.BlobRequestConditions;
import com.azure.android.storage.blob.models.BlobGetPropertiesResponse;
import com.azure.android.storage.blob.models.BlockBlobItem;
import com.azure.android.storage.blob.models.BlockBlobsCommitBlockListResponse;
import com.azure.android.storage.blob.models.BlockBlobsStageBlockResponse;
import com.azure.android.storage.blob.models.ContainersListBlobFlatSegmentResponse;
import com.azure.android.storage.blob.models.CpkInfo;
import com.azure.android.storage.blob.models.ListBlobsIncludeItem;
import com.azure.android.storage.blob.models.ListBlobsOptions;
import com.azure.android.storage.blob.transfer.DownloadRequest;
import com.azure.android.storage.blob.transfer.StorageBlobClientMap;
import com.azure.android.storage.blob.transfer.TransferClient;
import com.azure.android.storage.blob.transfer.TransferInfo;
import com.azure.android.storage.blob.transfer.UploadRequest;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.ResponseBody;

/**
 * Client for Storage Blob service.
 */
public class StorageBlobClient {
    private final String id;
    private final ServiceClient serviceClient;
    private final StorageBlobServiceImpl storageBlobServiceClient;
    private final Constraints transferConstraints;

    private StorageBlobClient(String id, ServiceClient serviceClient, Constraints transferConstraints) {
        this.id = id;
        this.serviceClient = serviceClient;
        this.storageBlobServiceClient = new StorageBlobServiceImpl(this.serviceClient);
        this.transferConstraints = transferConstraints;
    }

    /**
     * Creates a new {@link Builder} with initial configuration copied from this {@link StorageBlobClient}.
     *
     * @param storageBlobClientId The unique ID for the new {@link StorageBlobClient}. This identifier is used to
     *                            associate the {@link StorageBlobClient} with the upload and download transfers it
     *                            initiates. When a transfer is reloaded from disk (e.g. after an application crash),
     *                            it can only be resumed once a client with the same storageBlobClientId has been
     *                            initialized.
     * @return A new {@link Builder}.
     */
    public StorageBlobClient.Builder newBuilder(String storageBlobClientId) {
        return new Builder(storageBlobClientId, this);
    }

    /**
     * Gets the blob service base URL.
     *
     * @return The blob service base URL.
     */
    public String getBlobServiceUrl() {
        return this.serviceClient.getBaseUrl();
    }

    /**
     * Upload the content of a file.
     *
     * @param context       The application context.
     * @param containerName The container to upload the file to.
     * @param blobName      The name of the target blob holding the uploaded file.
     * @param file          The local file to upload.
     * @return A LiveData that streams {@link TransferInfo} describing the current state of the transfer.
     */
    public LiveData<TransferInfo> upload(Context context,
                                         String containerName,
                                         String blobName, File file) {
        final UploadRequest request = new UploadRequest.Builder()
            .storageClientId(this.id)
            .containerName(containerName)
            .blobName(blobName)
            .file(file)
            .constraints(this.transferConstraints)
            .build();
        return TransferClient.getInstance(context)
            .upload(request);
    }

    /**
     * Upload content identified by a given URI.
     *
     * @param context       The application context.
     * @param containerName The container to upload the file to.
     * @param blobName      The name of the target blob holding the uploaded file.
     * @param contentUri    The URI to the Content to upload, the contentUri is resolved using
     *                      {@link android.content.ContentResolver#openAssetFileDescriptor(Uri, String)} with mode as
     *                      "r". The supported URI schemes are: 'content://', 'file://' and 'android.resource://'.
     * @return A LiveData that streams {@link TransferInfo} describing the current state of the transfer.
     */
    public LiveData<TransferInfo> upload(Context context,
                                         String containerName,
                                         String blobName,
                                         Uri contentUri) {
        final UploadRequest request = new UploadRequest.Builder()
            .storageClientId(this.id)
            .containerName(containerName)
            .blobName(blobName)
            .contentUri(context, contentUri)
            .constraints(this.transferConstraints)
            .build();
        return TransferClient.getInstance(context)
            .upload(request);
    }

    /**
     * Download a blob.
     *
     * @param context       The application context.
     * @param containerName The container to download the blob from.
     * @param blobName      The name of the target blob to download.
     * @param file          The local file to download to.
     * @return A LiveData that streams {@link TransferInfo} describing the current state of the download.
     */
    public LiveData<TransferInfo> download(Context context,
                                           String containerName,
                                           String blobName,
                                           File file) {
        final DownloadRequest request = new DownloadRequest.Builder()
            .storageClientId(this.id)
            .containerName(containerName)
            .blobName(blobName)
            .file(file)
            .constraints(this.transferConstraints)
            .build();
        return TransferClient.getInstance(context)
            .download(request);
    }

    /**
     * Download a blob.
     *
     * @param context       The application context.
     * @param containerName The container to download the blob from.
     * @param blobName      The name of the target blob to download.
     * @param contentUri    The URI to the local content where the downloaded blob will be stored.
     * @return LiveData that streams {@link TransferInfo} describing the current state of the download.
     */
    public LiveData<TransferInfo> download(Context context,
                                           String containerName,
                                           String blobName,
                                           Uri contentUri) {
        final DownloadRequest request = new DownloadRequest.Builder()
            .storageClientId(this.id)
            .containerName(containerName)
            .blobName(blobName)
            .contentUri(context, contentUri)
            .constraints(this.transferConstraints)
            .build();
        return TransferClient.getInstance(context)
            .download(request);
    }

    /**
     * Pause a transfer identified by the given transfer ID. The pause operation is a best-effort, and a transfer
     * that is already executing may continue to transfer.
     * <p>
     * Upon successful scheduling of the pause, any observer observing on {@link LiveData<TransferInfo>} for this
     * transfer receives a {@link TransferInfo} event with state {@link TransferInfo.State#USER_PAUSED}.
     *
     * @param context    The application context.
     * @param transferId The transfer ID identifies the transfer to pause.
     */
    public void pause(Context context, long transferId) {
        TransferClient.getInstance(context)
            .pause(transferId);
    }

    /**
     * Resume a paused transfer.
     *
     * @param context    The application context
     * @param transferId The transfer ID identifies the transfer to resume.
     * @return A LiveData that streams {@link TransferInfo} describing the current state of the transfer.
     */
    public LiveData<TransferInfo> resume(Context context, long transferId) {
        return TransferClient.getInstance(context)
            .resume(transferId);
    }

    /**
     * Cancel a transfer identified by the given transfer ID. The cancel operation is a best-effort, and a transfer
     * that is already executing may continue to transfer.
     * <p>
     * Upon successful scheduling of the cancellation, any observer observing on {@link LiveData<TransferInfo>} for
     * this transfer receives a {@link TransferInfo} event with state {@link TransferInfo.State#CANCELLED}.
     *
     * @param transferId The transfer ID identifies the transfer to cancel.
     */
    public void cancel(Context context, long transferId) {
        TransferClient.getInstance(context)
            .cancel(transferId);
    }

    /**
     * Gets a list of blobs identified by a page id in a given container.
     *
     * @param pageId        Identifies the portion of the list to be returned.
     * @param containerName The container name.
     * @param options       The page options.
     * @return A list of blobs.
     */
    public List<BlobItem> getBlobsInPage(String pageId,
                                         String containerName,
                                         ListBlobsOptions options) {
        return this.storageBlobServiceClient.getBlobsInPage(pageId,
            containerName,
            options);
    }

    /**
     * Gets a list of blobs identified by a page id in a given container.
     *
     * @param pageId        Identifies the portion of the list to be returned.
     * @param containerName The container name.
     * @param options       The page options.
     * @param callback      Callback that receives the retrieved blob list.
     */
    public ServiceCall getBlobsInPage(String pageId,
                                      String containerName,
                                      ListBlobsOptions options,
                                      Callback<List<BlobItem>> callback) {
        return this.storageBlobServiceClient.getBlobsInPage(pageId,
            containerName,
            options,
            callback);
    }

    /**
     * Gets a list of blobs identified by a page id in a given container.
     *
     * @param pageId        Identifies the portion of the list to be returned.
     * @param containerName The container name.
     * @param prefix        Filters the results to return only blobs whose name begins with the specified prefix.
     * @param maxResults    Specifies the maximum number of blobs to return.
     * @param include       Include this parameter to specify one or more datasets to include in the response.
     * @param timeout       The timeout parameter is expressed in seconds. For more information, see
     *                      &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param requestId     Provides a client-generated, opaque value with a 1 KB character limit that is recorded in
     *                      the analytics logs when storage analytics logging is enabled.
     * @return A response object containing a list of blobs.
     */
    public ContainersListBlobFlatSegmentResponse getBlobsInPageWithRestResponse(String pageId,
                                                                                String containerName,
                                                                                String prefix,
                                                                                Integer maxResults,
                                                                                List<ListBlobsIncludeItem> include,
                                                                                Integer timeout,
                                                                                String requestId) {
        return this.storageBlobServiceClient.getBlobsInPageWithRestResponse(pageId,
            containerName,
            prefix,
            maxResults,
            include,
            timeout,
            requestId);
    }

    /**
     * Gets a list of blobs identified by a page id in a given container.
     *
     * @param pageId        Identifies the portion of the list to be returned.
     * @param containerName The container name.
     * @param prefix        Filters the results to return only blobs whose name begins with the specified prefix.
     * @param maxResults    Specifies the maximum number of blobs to return.
     * @param include       Include this parameter to specify one or more datasets to include in the response.
     * @param timeout       The timeout parameter is expressed in seconds. For more information, see
     *                      &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param requestId     Provides a client-generated, opaque value with a 1 KB character limit that is recorded in
     *                      the analytics logs when storage analytics logging is enabled.
     * @param callback      Callback that receives the response.
     */
    public ServiceCall getBlobsInPageWithRestResponse(String pageId,
                                                      String containerName,
                                                      String prefix,
                                                      Integer maxResults,
                                                      List<ListBlobsIncludeItem> include,
                                                      Integer timeout,
                                                      String requestId,
                                                      Callback<ContainersListBlobFlatSegmentResponse> callback) {
        return this.storageBlobServiceClient.getBlobsInPageWithRestResponse(pageId,
            containerName,
            prefix,
            maxResults,
            include,
            timeout,
            requestId,
            callback);
    }

    /**
     * Reads the blob's metadata & properties.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     */
    public BlobGetPropertiesHeaders getBlobProperties(String containerName,
                                                      String blobName) {
        return storageBlobServiceClient.getBlobProperties(containerName,  blobName);
    }

    /**
     * Reads the blob's metadata & properties.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param callback      Callback that receives the response.
     */
    public ServiceCall getBlobProperties(String containerName,
                                         String blobName,
                                         Callback<BlobGetPropertiesHeaders> callback) {
        return storageBlobServiceClient.getBlobProperties(containerName,
            blobName,
            callback);
    }

    /**
     * Reads a blob's metadata & properties.
     *
     * @param containerName         The container name.
     * @param blobName              The blob name.
     * @param snapshot              The snapshot parameter is an opaque DateTime value that, when present, specifies
     *                              the blob snapshot to retrieve. For more information on working with blob snapshots,
     *                              see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/creating-a-snapshot-of-a-blob"&gt;Creating a Snapshot of a Blob.&lt;/a&gt;.
     * @param timeout               The timeout parameter is expressed in seconds. For more information, see
     *                              &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param version               Specifies the version of the operation to use for this request.
     * @param blobRequestConditions Object that contains values which will restrict the successful operation of a
     *                              variety of requests to the conditions present. These conditions are entirely
     *                              optional.
     * @param requestId             Provides a client-generated, opaque value with a 1 KB character limit that is
     *                              recorded in the analytics logs when storage analytics logging is enabled.
     * @param cpkInfo               Additional parameters for the operation.
     * @return The response information returned from the server when downloading a blob.
     */
    public BlobGetPropertiesResponse getBlobPropertiesWithRestResponse(String containerName,
                                                                       String blobName,
                                                                       String snapshot,
                                                                       Integer timeout,
                                                                       String version,
                                                                       BlobRequestConditions blobRequestConditions,
                                                                       String requestId,
                                                                       CpkInfo cpkInfo) {
        blobRequestConditions = blobRequestConditions == null ? new BlobRequestConditions() : blobRequestConditions;

        return storageBlobServiceClient.getBlobPropertiesWithRestResponse(containerName,
            blobName,
            snapshot,
            timeout,
            version,
            blobRequestConditions.getLeaseId(),
            requestId,
            cpkInfo);
    }

    /**
     * Reads a blob's metadata & properties.
     *
     * @param containerName         The container name.
     * @param blobName              The blob name.
     * @param snapshot              The snapshot parameter is an opaque DateTime value that, when present, specifies
     *                              the blob snapshot to retrieve. For more information on working with blob snapshots,
     *                              see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/creating-a-snapshot-of-a-blob"&gt;Creating a Snapshot of a Blob.&lt;/a&gt;.
     * @param timeout               The timeout parameter is expressed in seconds. For more information, see
     *                              &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param version               Specifies the version of the operation to use for this request.
     * @param blobRequestConditions Object that contains values which will restrict the successful operation of a
     *                              variety of requests to the conditions present. These conditions are entirely
     *                              optional.
     * @param requestId             Provides a client-generated, opaque value with a 1 KB character limit that is
     *                              recorded in the analytics logs when storage analytics logging is enabled.
     * @param cpkInfo               Additional parameters for the operation.
     */
    public ServiceCall getBlobPropertiesWithRestResponse(String containerName,
                                                         String blobName,
                                                         String snapshot,
                                                         Integer timeout,
                                                         String version,
                                                         BlobRequestConditions blobRequestConditions,
                                                         String requestId,
                                                         CpkInfo cpkInfo,
                                                         Callback<BlobGetPropertiesResponse> callback) {
        blobRequestConditions = blobRequestConditions == null ? new BlobRequestConditions() : blobRequestConditions;

        return storageBlobServiceClient.getBlobPropertiesWithRestResponse(containerName,
            blobName,
            snapshot,
            timeout,
            version,
            blobRequestConditions.getLeaseId(),
            requestId,
            cpkInfo,
            callback);
    }

    /**
     * Reads the entire blob.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     */
    public ResponseBody download(String containerName,
                                 String blobName) {
        return storageBlobServiceClient.download(containerName,
            blobName);
    }

    /**
     * Reads the entire blob.
     *
     * @param containerName The container name.
     * @param blobName      The blob name.
     * @param callback      Callback that receives the response.
     */
    public ServiceCall download(String containerName,
                                String blobName,
                                Callback<ResponseBody> callback) {
        return storageBlobServiceClient.download(containerName,
            blobName,
            callback);
    }

    /**
     * Reads a range of bytes from a blob.
     *
     * @param containerName         The container name.
     * @param blobName              The blob name.
     * @param snapshot              The snapshot parameter is an opaque DateTime value that, when present, specifies
     *                              the blob snapshot to retrieve. For more information on working with blob snapshots,
     *                              see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/creating-a-snapshot-of-a-blob"&gt;Creating a Snapshot of a Blob.&lt;/a&gt;.
     * @param timeout               The timeout parameter is expressed in seconds. For more information, see
     *                              &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param range                 Return only the bytes of the blob in the specified range.
     * @param blobRequestConditions Object that contains values which will restrict the successful operation of a
     *                              variety of requests to the conditions present. These conditions are entirely
     *                              optional.
     * @param getRangeContentMd5    When set to true and specified together with the Range, the service returns the
     *                              MD5 hash for the range, as long as the range is less than or equal to 4 MB in size.
     * @param getRangeContentCrc64  When set to true and specified together with the Range, the service returns the
     *                              CRC64 hash for the range, as long as the range is less than or equal to 4 MB in size.
     * @param version               Specifies the version of the operation to use for this request.
     * @param requestId             Provides a client-generated, opaque value with a 1 KB character limit that is
     *                              recorded in the analytics logs when storage analytics logging is enabled.
     * @param cpkInfo               Additional parameters for the operation.
     * @return The response information returned from the server when downloading a blob.
     */
    public BlobDownloadAsyncResponse downloadWithRestResponse(String containerName,
                                                              String blobName,
                                                              String snapshot,
                                                              Integer timeout,
                                                              BlobRange range,
                                                              BlobRequestConditions blobRequestConditions,
                                                              Boolean getRangeContentMd5,
                                                              Boolean getRangeContentCrc64,
                                                              String version,
                                                              String requestId,
                                                              CpkInfo cpkInfo) {
        range = range == null ? new BlobRange(0) : range;
        blobRequestConditions = blobRequestConditions == null ? new BlobRequestConditions() : blobRequestConditions;

        return storageBlobServiceClient.downloadWithRestResponse(containerName,
            blobName,
            snapshot,
            timeout,
            range.toHeaderValue(),
            blobRequestConditions.getLeaseId(),
            getRangeContentMd5,
            getRangeContentCrc64,
            blobRequestConditions.getIfModifiedSince(),
            blobRequestConditions.getIfUnmodifiedSince(),
            blobRequestConditions.getIfMatch(),
            blobRequestConditions.getIfNoneMatch(),
            version,
            requestId,
            cpkInfo);
    }

    /**
     * Reads a range of bytes from a blob.
     *
     * @param containerName         The container name.
     * @param blobName              The blob name.
     * @param snapshot              The snapshot parameter is an opaque DateTime value that, when present, specifies
     *                              the blob snapshot to retrieve. For more information on working with blob snapshots,
     *                              see &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/creating-a-snapshot-of-a-blob"&gt;Creating a Snapshot of a Blob.&lt;/a&gt;.
     * @param timeout               The timeout parameter is expressed in seconds. For more information, see
     *                              &lt;a href="https://docs.microsoft.com/en-us/rest/api/storageservices/setting-timeouts-for-blob-service-operations"&gt;Setting Timeouts for Blob Service Operations.&lt;/a&gt;.
     * @param range                 Return only the bytes of the blob in the specified range.
     * @param blobRequestConditions Object that contains values which will restrict the successful operation of a
     *                              variety of requests to the conditions present. These conditions are entirely optional.
     * @param getRangeContentMd5    When set to true and specified together with the Range, the service returns the
     *                              MD5 hash for the range, as long as the range is less than or equal to 4 MB in size.
     * @param getRangeContentCrc64  When set to true and specified together with the Range, the service returns the
     *                              CRC64 hash for the range, as long as the range is less than or equal to 4 MB in size.
     * @param version               Specifies the version of the operation to use for this request.
     * @param requestId             Provides a client-generated, opaque value with a 1 KB character limit that is
     *                              recorded in the analytics logs when storage analytics logging is enabled.
     * @param cpkInfo               Additional parameters for the operation.
     */
    public ServiceCall downloadWithRestResponse(String containerName,
                                                String blobName,
                                                String snapshot,
                                                Integer timeout,
                                                BlobRange range,
                                                BlobRequestConditions blobRequestConditions,
                                                Boolean getRangeContentMd5,
                                                Boolean getRangeContentCrc64,
                                                String version,
                                                String requestId,
                                                CpkInfo cpkInfo,
                                                Callback<BlobDownloadAsyncResponse> callback) {
        range = range == null ? new BlobRange(0) : range;
        blobRequestConditions = blobRequestConditions == null ? new BlobRequestConditions() : blobRequestConditions;

        return storageBlobServiceClient.downloadWithRestResponse(containerName,
            blobName,
            snapshot,
            timeout,
            range.toHeaderValue(),
            blobRequestConditions.getLeaseId(),
            getRangeContentMd5,
            getRangeContentCrc64,
            blobRequestConditions.getIfModifiedSince(),
            blobRequestConditions.getIfUnmodifiedSince(),
            blobRequestConditions.getIfMatch(),
            blobRequestConditions.getIfNoneMatch(),
            version,
            requestId,
            cpkInfo,
            callback);
    }

    public Void stageBlock(String containerName,
                           String blobName,
                           String base64BlockId,
                           byte[] blockContent,
                           byte[] contentMd5) {
        return this.storageBlobServiceClient.stageBlock(containerName,
            blobName,
            base64BlockId,
            blockContent,
            contentMd5);
    }

    public ServiceCall stageBlock(String containerName,
                                  String blobName,
                                  String base64BlockId,
                                  byte[] blockContent,
                                  byte[] contentMd5,
                                  Callback<Void> callback) {
        return this.storageBlobServiceClient.stageBlock(containerName,
            blobName,
            base64BlockId,
            blockContent,
            contentMd5,
            callback);
    }

    public BlockBlobsStageBlockResponse stageBlockWithRestResponse(String containerName,
                                                                   String blobName,
                                                                   String base64BlockId,
                                                                   byte[] body,
                                                                   byte[] transactionalContentMD5,
                                                                   byte[] transactionalContentCrc64,
                                                                   Integer timeout,
                                                                   String leaseId,
                                                                   String requestId,
                                                                   CpkInfo cpkInfo) {
        return this.storageBlobServiceClient.stageBlockWithRestResponse(containerName,
            blobName,
            base64BlockId,
            body,
            transactionalContentMD5,
            transactionalContentCrc64,
            timeout,
            leaseId,
            requestId,
            cpkInfo);
    }

    public ServiceCall stageBlockWithRestResponse(String containerName,
                                                  String blobName,
                                                  String base64BlockId,
                                                  byte[] body,
                                                  byte[] transactionalContentMD5,
                                                  byte[] transactionalContentCrc64,
                                                  Integer timeout,
                                                  String leaseId,
                                                  String requestId,
                                                  CpkInfo cpkInfo,
                                                  Callback<BlockBlobsStageBlockResponse> callback) {
        return this.storageBlobServiceClient.stageBlockWithRestResponse(containerName,
            blobName,
            base64BlockId,
            body,
            transactionalContentMD5,
            transactionalContentCrc64,
            timeout,
            leaseId,
            requestId,
            cpkInfo,
            callback);
    }

    public BlockBlobItem commitBlockList(String containerName,
                                         String blobName,
                                         List<String> base64BlockIds,
                                         boolean overwrite) {
        return this.storageBlobServiceClient.commitBlockList(containerName,
            blobName,
            base64BlockIds,
            overwrite);
    }

    public ServiceCall commitBlockList(String containerName,
                                       String blobName,
                                       List<String> base64BlockIds,
                                       boolean overwrite,
                                       Callback<BlockBlobItem> callBack) {
        return this.storageBlobServiceClient.commitBlockList(containerName,
            blobName,
            base64BlockIds,
            overwrite,
            callBack);
    }


    public BlockBlobsCommitBlockListResponse commitBlockListWithRestResponse(String containerName,
                                                                             String blobName,
                                                                             List<String> base64BlockIds,
                                                                             byte[] transactionalContentMD5,
                                                                             byte[] transactionalContentCrc64,
                                                                             Integer timeout,
                                                                             BlobHttpHeaders blobHttpHeaders,
                                                                             Map<String, String> metadata,
                                                                             BlobRequestConditions requestConditions,
                                                                             String requestId,
                                                                             CpkInfo cpkInfo,
                                                                             AccessTier tier) {
        return this.storageBlobServiceClient.commitBlockListWithRestResponse(containerName,
            blobName,
            base64BlockIds,
            transactionalContentMD5,
            transactionalContentCrc64,
            timeout,
            blobHttpHeaders,
            metadata,
            requestConditions,
            requestId,
            cpkInfo,
            tier);
    }

    public ServiceCall commitBlockListWithRestResponse(String containerName,
                                                       String blobName,
                                                       List<String> base64BlockIds,
                                                       byte[] transactionalContentMD5,
                                                       byte[] transactionalContentCrc64,
                                                       Integer timeout,
                                                       BlobHttpHeaders blobHttpHeaders,
                                                       Map<String, String> metadata,
                                                       BlobRequestConditions requestConditions,
                                                       String requestId,
                                                       CpkInfo cpkInfo,
                                                       AccessTier tier,
                                                       Callback<BlockBlobsCommitBlockListResponse> callback) {
        return this.storageBlobServiceClient.commitBlockListWithRestResponse(containerName,
            blobName,
            base64BlockIds,
            transactionalContentMD5,
            transactionalContentCrc64,
            timeout,
            blobHttpHeaders,
            metadata,
            requestConditions,
            requestId,
            cpkInfo,
            tier, callback);
    }

    /**
     * Builder for {@link StorageBlobClient}.
     */
    public static class Builder {
        private final String storageBlobClientId;
        private final ServiceClient.Builder serviceClientBuilder;
        private final Constraints.Builder transferConstraintsBuilder;
        private static final StorageBlobClientMap STORAGE_BLOB_CLIENTS;

        static {
            STORAGE_BLOB_CLIENTS = StorageBlobClientMap.getInstance();
        }

        /**
         * Creates a {@link Builder}.
         *
         * @param storageBlobClientId The unique ID for the {@link StorageBlobClient} this builder builds. This
         *                            identifier is used to associate this {@link StorageBlobClient} with the upload and
         *                            download transfers it initiates. When a transfer is reloaded from disk (e.g.
         *                            after an application crash), it can only be resumed once a client with the same
         *                            storageBlobClientId has been initialized.
         */
        public Builder(String storageBlobClientId) {
            this(storageBlobClientId, new ServiceClient.Builder());
            this.serviceClientBuilder
                .addInterceptor(new AddDateInterceptor())
                .setSerializationFormat(SerializerFormat.XML);
        }

        /**
         * Creates a {@link Builder} that uses the provided {@link com.azure.android.core.http.ServiceClient.Builder}
         * to build a {@link ServiceClient} for the {@link StorageBlobClient}.
         *
         * @param storageBlobClientId  The unique ID for the {@link StorageBlobClient} this builder builds.
         * @param serviceClientBuilder The {@link com.azure.android.core.http.ServiceClient.Builder}.
         */
        public Builder(String storageBlobClientId, ServiceClient.Builder serviceClientBuilder) {
            this(storageBlobClientId, serviceClientBuilder, new Constraints.Builder());
            this.transferConstraintsBuilder
                .setRequiredNetworkType(NetworkType.CONNECTED);
        }

        private Builder(String storageBlobClientId,
                        ServiceClient.Builder serviceClientBuilder,
                        Constraints.Builder transferConstraintsBuilder) {
            if (CoreUtil.isNullOrEmpty(storageBlobClientId)) {
                throw new IllegalArgumentException("'storageBlobClientId' cannot be null or empty.");
            }
            if (Builder.STORAGE_BLOB_CLIENTS.contains(storageBlobClientId)) {
                throw new IllegalArgumentException("A StorageBlobClient with id '" + storageBlobClientId + "' already exists.");
            }
            this.storageBlobClientId = storageBlobClientId;
            this.serviceClientBuilder
                = Objects.requireNonNull(serviceClientBuilder, "serviceClientBuilder cannot be null.");
            this.transferConstraintsBuilder
                = Objects.requireNonNull(transferConstraintsBuilder, "transferConstraintsBuilder cannot be null.");
        }

        /**
         * Sets the base URL for the {@link StorageBlobClient}.
         *
         * @param blobServiceUrl The blob service base URL.
         * @return An updated {@link Builder} with the provided blob service URL set.
         */
        public Builder setBlobServiceUrl(String blobServiceUrl) {
            Objects.requireNonNull(blobServiceUrl, "blobServiceUrl cannot be null.");
            this.serviceClientBuilder.setBaseUrl(blobServiceUrl);
            return this;
        }

        /**
         * Sets an interceptor used to authenticate the blob service request.
         *
         * @param credentialInterceptor The credential interceptor.
         * @return An updated {@link Builder} with the provided credentials interceptor set.
         */
        public Builder setCredentialInterceptor(Interceptor credentialInterceptor) {
            this.serviceClientBuilder.setCredentialsInterceptor(credentialInterceptor);
            return this;
        }

        /**
         * Sets whether device should be charging for running the transfers. The default value is {@code false}.
         *
         * @param requiresCharging {@code true} if the device must be charging for the transfer to run.
         * @return An updated {@link Builder} with the provided charging requirement set.
         */
        public Builder setRequiresCharging(boolean requiresCharging) {
            this.transferConstraintsBuilder.setRequiresCharging(requiresCharging);
            return this;
        }

        /**
         * Sets whether device should be idle for running the transfers. The default value is {@code false}.
         *
         * @param requiresDeviceIdle {@code true} if the device must be idle for transfers to run.
         * @return An updated {@link Builder} with the provided setting set.
         */
        @RequiresApi(23)
        public Builder setRequiresDeviceIdle(boolean requiresDeviceIdle) {
            if (Build.VERSION.SDK_INT >= 23) {
                this.transferConstraintsBuilder.setRequiresDeviceIdle(requiresDeviceIdle);
            }
            return this;
        }

        /**
         * Sets the particular {@link NetworkType} the device should be in for running the transfers.
         * <p>
         * The default network type that {@link TransferClient} uses is {@link NetworkType#CONNECTED}.
         *
         * @param networkType The type of network required for transfers to run.
         * @return An updated {@link Builder} with the provided network type set.
         */
        public Builder setRequiredNetworkType(@NonNull NetworkType networkType) {
            Objects.requireNonNull(networkType, "'networkType' cannot be null.");
            if (networkType == NetworkType.NOT_REQUIRED) {
                throw new IllegalArgumentException(
                    "The network type NOT_REQUIRED is not a valid transfer configuration.");
            }
            this.transferConstraintsBuilder.setRequiredNetworkType(networkType);
            return this;
        }

        /**
         * Sets whether device battery should be at an acceptable level for running the transfers. The default value
         * is {@code false}.
         *
         * @param requiresBatteryNotLow {@code true} if the battery should be at an acceptable level for the
         *                                          transfers to run.
         * @return An updated {@link Builder} with the provided battery requirement set.
         */
        public Builder setRequiresBatteryNotLow(boolean requiresBatteryNotLow) {
            this.transferConstraintsBuilder.setRequiresBatteryNotLow(requiresBatteryNotLow);
            return this;
        }

        /**
         * Sets whether the device's available storage should be at an acceptable level for running
         * the transfers. The default value is {@code false}.
         *
         * @param requiresStorageNotLow {@code true} if the available storage should not be below a
         *                              a critical threshold for the transfer to run.
         * @return An updated {@link Builder} with the provided storage requirement set.
         */
        public Builder setRequiresStorageNotLow(boolean requiresStorageNotLow) {
            this.transferConstraintsBuilder.setRequiresStorageNotLow(requiresStorageNotLow);
            return this;
        }

        /**
         * Builds a {@link StorageBlobClient} based on this {@link Builder}'s configuration.
         *
         * @return A {@link StorageBlobClient}.
         */
        public StorageBlobClient build() {
            Constraints transferConstraints = this.transferConstraintsBuilder.build();
            NetworkType networkType = transferConstraints.getRequiredNetworkType();
            if (networkType == null || networkType == NetworkType.NOT_REQUIRED) {
                throw new IllegalArgumentException(
                    "The null or NOT_REQUIRED NetworkType is not a valid transfer configuration.");
            }
            StorageBlobClient client = new StorageBlobClient(this.storageBlobClientId,
                this.serviceClientBuilder.build(),
                transferConstraints);
            Builder.STORAGE_BLOB_CLIENTS.add(storageBlobClientId, client);
            return client;
        }

        private Builder(String storageBlobClientId, final StorageBlobClient storageBlobClient) {
            this(storageBlobClientId,
                storageBlobClient.serviceClient.newBuilder(),
                newBuilder(storageBlobClient.transferConstraints));
        }

        private static androidx.work.Constraints.Builder newBuilder(androidx.work.Constraints constraints) {
            Constraints.Builder builder = new Constraints.Builder();
            builder.setRequiresCharging(constraints.requiresCharging());
            if (Build.VERSION.SDK_INT >= 23) {
                builder.setRequiresDeviceIdle(constraints.requiresDeviceIdle());
            }
            builder.setRequiredNetworkType(constraints.getRequiredNetworkType());
            builder.setRequiresBatteryNotLow(constraints.requiresBatteryNotLow());
            builder.setRequiresStorageNotLow(constraints.requiresStorageNotLow());
            return builder;
        }
    }
}
