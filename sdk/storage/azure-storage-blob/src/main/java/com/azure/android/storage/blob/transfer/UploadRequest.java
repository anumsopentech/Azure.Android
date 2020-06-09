// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.storage.blob.transfer;

import android.content.Context;
import android.net.Uri;

import androidx.work.Constraints;

import com.azure.android.core.util.CoreUtil;

import java.io.File;
import java.util.Objects;

/**
 * A type specifying parameters for upload transfer that should be enqueued in {@link TransferClient}.
 */
public final class UploadRequest {
    private final String storageClientId;
    private final String containerName;
    private final String blobName;
    private final ReadableContent readableContent;
    private final Constraints constraints;

    /**
     * Create UploadRequest.
     *
     * @param storageClientId identifies the the blob storage client for the upload
     * @param containerName the name of the container to upload the content to
     * @param blobName  the name of the target blob holding uploaded content
     * @param readableContent the object describing the content in the device that needs to be uploaded
     * @param constraints the constraints to be satisfied to execute the upload
     */
    private UploadRequest(String storageClientId,
                  String containerName,
                  String blobName,
                  ReadableContent readableContent,
                  Constraints constraints) {
        this.storageClientId = storageClientId;
        this.containerName = containerName;
        this.blobName = blobName;
        this.readableContent = readableContent;
        this.constraints = constraints;
    }

    /**
     * Get the unique identifier of the blob storage client to be used for the upload.
     *
     * @return the unique identifier of the blob storage client
     */
    String getStorageClientId() {
        return this.storageClientId;
    }

    /**
     * Get the name of the container to upload the file to.
     *
     * @return the container name
     */
    String getContainerName() {
        return this.containerName;
    }

    /**
     * Get the name of the target blob holding uploaded file.
     *
     * @return the blob name
     */
    String getBlobName() {
        return this.blobName;
    }

    /**
     * Get the object describing the content in the device that needs to be uploaded.
     *
     * @return the content description
     */
    ReadableContent getReadableContent() {
        return this.readableContent;
    }

    /**
     * Get the constraints to be satisfied to execute the upload.
     *
     * @return the constraints
     */
    Constraints getConstraints() {
        return this.constraints;
    }

    /**
     * Builder for {@link UploadRequest}.
     */
    public static final class Builder {
        private String storageClientId;
        private String containerName;
        private String blobName;
        private ReadableContent readableContent;
        private Constraints constraints;

        /**
         * Creates a {@link Builder}.
         */
        public Builder() {
        }

        /**
         * Set the unique identifier of the blob storage client to be used for the upload.
         *
         * @param storageClientId the blob storage client id
         * @return Builder with provided blob storage client id set
         */
        public Builder storageClientId(String storageClientId) {
            this.storageClientId = storageClientId;
            return this;
        }

        /**
         * Set the name of the container to upload the file to.
         *
         * @param containerName the container name
         * @return Builder with provided container name set
         */
        public Builder containerName(String containerName) {
            this.containerName = containerName;
            return this;
        }

        /**
         * Set the name of the target blob holding uploaded file.
         *
         * @param blobName the blob name
         * @return Builder with provided blob name set
         */
        public Builder blobName(String blobName) {
            this.blobName = blobName;
            return this;
        }

        /**
         * Set the file in the device to upload.
         *
         * @param file the file
         * @return Builder with provided file set
         */
        public Builder file(File file) {
            Objects.requireNonNull(file, "'file' cannot be null.");
            if (this.readableContent != null && this.readableContent.isUsingContentResolver()) {
                throw new IllegalArgumentException("Both the contentUri and file cannot be set for the same request.");
            }
            this.readableContent = new ReadableContent(null, Uri.fromFile(file), false);
            return this;
        }

        /**
         * Set the content in the device to upload.
         *
         * @param context the application context
         * @param uri URI to the Content to upload
         * @return Builder with provided content description set
         */
        public Builder contentUri(Context context, Uri uri) {
            Objects.requireNonNull(context, "'context' cannot be null.");
            Objects.requireNonNull(uri, "'uri' cannot be null.");
            if (this.readableContent != null && !this.readableContent.isUsingContentResolver()) {
                throw new IllegalArgumentException("Both the contentUri and file cannot be set for the same request.");
            }
            this.readableContent = new ReadableContent(context, uri, true);
            return this;
        }

        /**
         * Set the constraints to be satisfied to execute the upload.
         *
         * @param constraints the constraints
         * @return Builder with provided constraints set
         */
        public Builder constraints(Constraints constraints) {
            this.constraints = constraints;
            return this;
        }

        /**
         * Builds a {@link UploadRequest} based on this {@link Builder}'s configuration.
         *
         * @return A {@link UploadRequest}.
         */
        public UploadRequest build() {
            if (CoreUtil.isNullOrEmpty(this.storageClientId)) {
                throw new IllegalArgumentException("'storageClientId' is required and cannot be null or empty.");
            }
            if (CoreUtil.isNullOrEmpty(this.containerName)) {
                throw new IllegalArgumentException("'containerName' is required and cannot be null or empty.");
            }
            if (CoreUtil.isNullOrEmpty(this.blobName)) {
                throw new IllegalArgumentException("'blobName' is required and cannot be null or empty.");
            }
            Objects.requireNonNull(this.readableContent, "either 'file' or 'contentUri' must be set.");
            Objects.requireNonNull(this.constraints, "'constraints' cannot be null.");
            return new UploadRequest(this.storageClientId,
                this.containerName,
                this.blobName,
                this.readableContent,
                this.constraints);
        }
    }
}
