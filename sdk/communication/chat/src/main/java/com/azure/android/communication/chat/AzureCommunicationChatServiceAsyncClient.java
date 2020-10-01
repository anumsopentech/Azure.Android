// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.android.communication.chat;

import com.azure.android.communication.chat.implementation.AzureCommunicationChatServiceImpl;
import com.azure.android.communication.chat.models.AddChatThreadMembersRequest;
import com.azure.android.communication.chat.models.ChatMessage;
import com.azure.android.communication.chat.models.ChatThread;
import com.azure.android.communication.chat.models.ChatThreadMember;
import com.azure.android.communication.chat.models.CreateChatThreadRequest;
import com.azure.android.communication.chat.models.CreateChatThreadResult;
import com.azure.android.communication.chat.models.ListChatMessagesResult;
import com.azure.android.communication.chat.models.ListChatThreadsResult;
import com.azure.android.communication.chat.models.ReadReceipt;
import com.azure.android.communication.chat.models.SendChatMessageRequest;
import com.azure.android.communication.chat.models.SendChatMessageResult;
import com.azure.android.communication.chat.models.SendReadReceiptRequest;
import com.azure.android.communication.chat.models.UpdateChatMessageRequest;
import com.azure.android.communication.chat.models.UpdateChatThreadRequest;
import com.azure.android.core.http.Callback;
import com.azure.android.core.http.Response;
import com.azure.android.core.http.ServiceClient;
import com.azure.android.core.http.exception.HttpResponseException;
import com.azure.android.core.http.responsepaging.AsyncPagedDataRetriever;
import com.azure.android.core.http.responsepaging.PagedDataResponseRetriever;
import com.azure.android.core.util.paging.PagedDataRetriever;
import java.util.List;
import okhttp3.Interceptor;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.threeten.bp.OffsetDateTime;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Initializes a new instance of the asynchronous AzureCommunicationChatService type.
 */
public final class AzureCommunicationChatServiceAsyncClient {
    private AzureCommunicationChatServiceImpl serviceClient;

    /**
     * Initializes an instance of AzureCommunicationChatService client.
     */
    AzureCommunicationChatServiceAsyncClient(AzureCommunicationChatServiceImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * Gets read receipts for a thread.
     * 
     * @param chatThreadId Thread id to get the read receipts for.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatReadReceipts(String chatThreadId, final Callback<List<ReadReceipt>> callback) {
        this.serviceClient.listChatReadReceipts(chatThreadId, callback);
    }

    /**
     * Sends a read receipt event to a thread, on behalf of a user.
     * 
     * @param chatThreadId Thread id to send the read receipt event to.
     * @param body Request payload for sending a read receipt.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void sendChatReadReceipt(String chatThreadId, SendReadReceiptRequest body, final Callback<Void> callback) {
        this.serviceClient.sendChatReadReceipt(chatThreadId, body, callback);
    }

    /**
     * Sends a message to a thread.
     * 
     * @param chatThreadId The thread id to send the message to.
     * @param body Details of the message to send.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void sendChatMessage(String chatThreadId, SendChatMessageRequest body, final Callback<SendChatMessageResult> callback) {
        this.serviceClient.sendChatMessage(chatThreadId, body, callback);
    }

    /**
     * Gets a list of messages from a thread.
     * 
     * @param chatThreadId The thread id of the message.
     * @param pageSize The number of messages being requested.
     * @param startTime The earliest point in time to get messages up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @param syncState The continuation token that previous request obtained. This is used for paging.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatMessages(String chatThreadId, Integer pageSize, OffsetDateTime startTime, String syncState, final Callback<ListChatMessagesResult> callback) {
        this.serviceClient.listChatMessages(chatThreadId, pageSize, startTime, syncState, callback);
    }

    /**
     * Gets a list of messages from a thread.
     * 
     * @param chatThreadId The thread id of the message.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatMessages(String chatThreadId, final Callback<ListChatMessagesResult> callback) {
        this.serviceClient.listChatMessages(chatThreadId, callback);
    }

    /**
     * Gets a message by id.
     * 
     * @param chatThreadId The thread id to which the message was sent.
     * @param chatMessageId The message id.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void getChatMessage(String chatThreadId, String chatMessageId, final Callback<ChatMessage> callback) {
        this.serviceClient.getChatMessage(chatThreadId, chatMessageId, callback);
    }

    /**
     * Updates a message.
     * 
     * @param chatThreadId The thread id to which the message was sent.
     * @param chatMessageId The message id.
     * @param body Details of the request to update the message.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void updateChatMessage(String chatThreadId, String chatMessageId, UpdateChatMessageRequest body, final Callback<Void> callback) {
        this.serviceClient.updateChatMessage(chatThreadId, chatMessageId, body, callback);
    }

    /**
     * Deletes a message.
     * 
     * @param chatThreadId The thread id to which the message was sent.
     * @param chatMessageId The message id.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void deleteChatMessage(String chatThreadId, String chatMessageId, final Callback<Void> callback) {
        this.serviceClient.deleteChatMessage(chatThreadId, chatMessageId, callback);
    }

    /**
     * Posts a typing event to a thread, on behalf of a user.
     * 
     * @param chatThreadId Id of the thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void sendTypingNotification(String chatThreadId, final Callback<Void> callback) {
        this.serviceClient.sendTypingNotification(chatThreadId, callback);
    }

    /**
     * Gets the members of a thread.
     * 
     * @param chatThreadId Thread id to get members for.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreadMembers(String chatThreadId, final Callback<List<ChatThreadMember>> callback) {
        this.serviceClient.listChatThreadMembers(chatThreadId, callback);
    }

    /**
     * Adds thread members to a thread. If members already exist, no change occurs.
     * 
     * @param chatThreadId Id of the thread to add members to.
     * @param body Thread members to be added to the thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void addChatThreadMembers(String chatThreadId, AddChatThreadMembersRequest body, final Callback<Void> callback) {
        this.serviceClient.addChatThreadMembers(chatThreadId, body, callback);
    }

    /**
     * Remove a member from a thread.
     * 
     * @param chatThreadId Thread id to remove the member from.
     * @param chatMemberId Id of the thread member to remove from the thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void removeChatThreadMember(String chatThreadId, String chatMemberId, final Callback<Void> callback) {
        this.serviceClient.removeChatThreadMember(chatThreadId, chatMemberId, callback);
    }

    /**
     * Creates a chat thread.
     * 
     * @param body Request payload for creating a chat thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void createChatThread(CreateChatThreadRequest body, final Callback<CreateChatThreadResult> callback) {
        this.serviceClient.createChatThread(body, callback);
    }

    /**
     * Gets the list of chat threads of a user.
     * 
     * @param pageSize The number of threads being requested.
     * @param syncState The continuation token that previous request obtained. This is used for paging.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreads(Integer pageSize, String syncState, final Callback<ListChatThreadsResult> callback) {
        this.serviceClient.listChatThreads(pageSize, syncState, callback);
    }

    /**
     * Gets the list of chat threads of a user.
     * 
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void listChatThreads(final Callback<ListChatThreadsResult> callback) {
        this.serviceClient.listChatThreads(callback);
    }

    /**
     * Updates a thread's properties.
     * 
     * @param chatThreadId The id of the thread to update.
     * @param body Request payload for updating a chat thread.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void updateChatThread(String chatThreadId, UpdateChatThreadRequest body, final Callback<Void> callback) {
        this.serviceClient.updateChatThread(chatThreadId, body, callback);
    }

    /**
     * Gets a chat thread.
     * 
     * @param chatThreadId Thread id to get.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void getChatThread(String chatThreadId, final Callback<ChatThread> callback) {
        this.serviceClient.getChatThread(chatThreadId, callback);
    }

    /**
     * Deletes a thread.
     * 
     * @param chatThreadId Thread id to delete.
     * @param callback the Callback that receives the response.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws HttpResponseException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     */
    public void deleteChatThread(String chatThreadId, final Callback<Void> callback) {
        this.serviceClient.deleteChatThread(chatThreadId, callback);
    }

    /**
     * A builder for creating a new instance of the AzureCommunicationChatServiceAsyncClient type.
     */
    public static final class Builder {
        /*
         * The endpoint of the Azure Communication resource.
         */
        private String endpoint;

        /**
         * Sets The endpoint of the Azure Communication resource.
         * 
         * @param endpoint the endpoint value.
         * @return the Builder.
         */
        public Builder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }

        /*
         * The Azure Core generic ServiceClient Builder.
         */
        private ServiceClient.Builder serviceClientBuilder;

        /**
         * Sets The Azure Core generic ServiceClient Builder.
         * 
         * @param serviceClientBuilder the serviceClientBuilder value.
         * @return the Builder.
         */
        public Builder serviceClientBuilder(ServiceClient.Builder serviceClientBuilder) {
            this.serviceClientBuilder = serviceClientBuilder;
            return this;
        }

        /*
         * The Interceptor to set intercept request and set credentials.
         */
        private Interceptor credentialInterceptor;

        /**
         * Sets The Interceptor to set intercept request and set credentials.
         * 
         * @param credentialInterceptor the credentialInterceptor value.
         * @return the Builder.
         */
        public Builder credentialInterceptor(Interceptor credentialInterceptor) {
            this.credentialInterceptor = credentialInterceptor;
            return this;
        }

        /**
         * Builds an instance of AzureCommunicationChatServiceAsyncClient with the provided parameters.
         * 
         * @return an instance of AzureCommunicationChatServiceAsyncClient.
         */
        public AzureCommunicationChatServiceAsyncClient build() {
            if (serviceClientBuilder == null) {
                if (endpoint == null) {
                    throw new IllegalArgumentException("Missing required parameters 'endpoint'.");
                }
                this.serviceClientBuilder = new ServiceClient.Builder();
            }
            if (endpoint != null) {
                final String retrofitBaseUrl = endpoint;
                serviceClientBuilder.setBaseUrl(retrofitBaseUrl);
            }
            if (credentialInterceptor != null) {
                serviceClientBuilder.setCredentialsInterceptor(credentialInterceptor);
            }
            AzureCommunicationChatServiceImpl internalClient = new AzureCommunicationChatServiceImpl(serviceClientBuilder.build(), endpoint);
            return new AzureCommunicationChatServiceAsyncClient(internalClient);
        }
    }
}
