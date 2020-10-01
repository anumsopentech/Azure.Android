// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
// Code generated by Microsoft (R) AutoRest Code Generator.

package com.azure.android.communication.chat;

import com.azure.android.communication.chat.implementation.AzureCommunicationChatServiceImpl;
import com.azure.android.communication.chat.models.AddChatThreadMembersOptions;
import com.azure.android.communication.chat.models.ChatMessage;
import com.azure.android.communication.chat.models.ChatMessagesCollection;
import com.azure.android.communication.chat.models.ChatThread;
import com.azure.android.communication.chat.models.ChatThreadInfo;
import com.azure.android.communication.chat.models.ChatThreadMember;
import com.azure.android.communication.chat.models.ChatThreadMembersCollection;
import com.azure.android.communication.chat.models.ChatThreadsInfoCollection;
import com.azure.android.communication.chat.models.CreateChatThreadOptions;
import com.azure.android.communication.chat.models.ErrorException;
import com.azure.android.communication.chat.models.MultiStatusResponse;
import com.azure.android.communication.chat.models.ReadReceipt;
import com.azure.android.communication.chat.models.ReadReceiptsCollection;
import com.azure.android.communication.chat.models.SendChatMessageOptions;
import com.azure.android.communication.chat.models.SendChatMessageResult;
import com.azure.android.communication.chat.models.SendReadReceiptRequest;
import com.azure.android.communication.chat.models.UpdateChatMessageOptions;
import com.azure.android.communication.chat.models.UpdateChatThreadOptions;
import com.azure.android.core.http.Callback;
import com.azure.android.core.http.Response;
import com.azure.android.core.http.ServiceClient;
import com.azure.android.core.http.exception.HttpResponseException;
import com.azure.android.core.http.responsepaging.AsyncPagedDataCollection;
import com.azure.android.core.http.responsepaging.AsyncPagedDataRetriever;
import com.azure.android.core.http.responsepaging.PagedDataResponseCollection;
import com.azure.android.core.http.responsepaging.PagedDataResponseRetriever;
import com.azure.android.core.util.paging.Page;
import com.azure.android.core.util.paging.PagedDataCollection;
import com.azure.android.core.util.paging.PagedDataRetriever;
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
 * Initializes a new instance of the synchronous AzureCommunicationChatService type.
 */
public final class AzureCommunicationChatServiceClient {
    private AzureCommunicationChatServiceImpl serviceClient;

    /**
     * Initializes an instance of AzureCommunicationChatService client.
     */
    AzureCommunicationChatServiceClient(AzureCommunicationChatServiceImpl serviceClient) {
        this.serviceClient = serviceClient;
    }

    /**
     * Gets read receipts for a thread.
     * 
     * @param chatThreadId Thread id to get the read receipts for.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return read receipts for a thread.
     */
    public Response<Page<ReadReceipt>> listChatReadReceiptsWithRestResponse(String chatThreadId) {
        return this.serviceClient.listChatReadReceiptsWithRestResponse(chatThreadId);
    }

    /**
     * Gets read receipts for a thread.
     * 
     * @param chatThreadId Thread id to get the read receipts for.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return read receipts for a thread.
     */
    public PagedDataResponseCollection<ReadReceipt, Page<ReadReceipt>> listChatReadReceiptsWithPageResponse(String chatThreadId) {
        return this.serviceClient.listChatReadReceiptsWithPageResponse(chatThreadId);
    }

    /**
     * Gets read receipts for a thread.
     * 
     * @param chatThreadId Thread id to get the read receipts for.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return read receipts for a thread.
     */
    public PagedDataCollection<ReadReceipt, Page<ReadReceipt>> listChatReadReceiptsWithPage(String chatThreadId) {
        return this.serviceClient.listChatReadReceiptsWithPage(chatThreadId);
    }

    /**
     * Sends a read receipt event to a thread, on behalf of a user.
     * 
     * @param chatThreadId Thread id to send the read receipt event to.
     * @param body Request payload for sending a read receipt.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<Void> sendChatReadReceiptWithRestResponse(String chatThreadId, SendReadReceiptRequest body) {
        return this.serviceClient.sendChatReadReceiptWithRestResponse(chatThreadId, body);
    }

    /**
     * Sends a message to a thread.
     * 
     * @param chatThreadId The thread id to send the message to.
     * @param body Details of the message to send.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return result of the send message operation.
     */
    public Response<SendChatMessageResult> sendChatMessageWithRestResponse(String chatThreadId, SendChatMessageOptions body) {
        return this.serviceClient.sendChatMessageWithRestResponse(chatThreadId, body);
    }

    /**
     * Gets a list of messages from a thread.
     * 
     * @param chatThreadId The thread id of the message.
     * @param maxPageSize The maximum number of messages to be returned per page.
     * @param startTime The earliest point in time to get messages up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a list of messages from a thread.
     */
    public Response<Page<ChatMessage>> listChatMessagesWithRestResponse(String chatThreadId, Integer maxPageSize, OffsetDateTime startTime) {
        return this.serviceClient.listChatMessagesWithRestResponse(chatThreadId, maxPageSize, startTime);
    }

    /**
     * Gets a list of messages from a thread.
     * 
     * @param chatThreadId The thread id of the message.
     * @param maxPageSize The maximum number of messages to be returned per page.
     * @param startTime The earliest point in time to get messages up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a list of messages from a thread.
     */
    public PagedDataResponseCollection<ChatMessage, Page<ChatMessage>> listChatMessagesWithPageResponse(String chatThreadId, Integer maxPageSize, OffsetDateTime startTime) {
        return this.serviceClient.listChatMessagesWithPageResponse(chatThreadId, maxPageSize, startTime);
    }

    /**
     * Gets a list of messages from a thread.
     * 
     * @param chatThreadId The thread id of the message.
     * @param maxPageSize The maximum number of messages to be returned per page.
     * @param startTime The earliest point in time to get messages up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a list of messages from a thread.
     */
    public PagedDataCollection<ChatMessage, Page<ChatMessage>> listChatMessagesWithPage(String chatThreadId, Integer maxPageSize, OffsetDateTime startTime) {
        return this.serviceClient.listChatMessagesWithPage(chatThreadId, maxPageSize, startTime);
    }

    /**
     * Gets a list of messages from a thread.
     * 
     * @param chatThreadId The thread id of the message.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a list of messages from a thread.
     */
    public Page<ChatMessage> listChatMessages(String chatThreadId) {
        return this.serviceClient.listChatMessages(chatThreadId);
    }

    /**
     * Gets a message by id.
     * 
     * @param chatThreadId The thread id to which the message was sent.
     * @param chatMessageId The message id.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a message by id.
     */
    public Response<ChatMessage> getChatMessageWithRestResponse(String chatThreadId, String chatMessageId) {
        return this.serviceClient.getChatMessageWithRestResponse(chatThreadId, chatMessageId);
    }

    /**
     * Updates a message.
     * 
     * @param chatThreadId The thread id to which the message was sent.
     * @param chatMessageId The message id.
     * @param body Details of the request to update the message.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<Void> updateChatMessageWithRestResponse(String chatThreadId, String chatMessageId, UpdateChatMessageOptions body) {
        return this.serviceClient.updateChatMessageWithRestResponse(chatThreadId, chatMessageId, body);
    }

    /**
     * Deletes a message.
     * 
     * @param chatThreadId The thread id to which the message was sent.
     * @param chatMessageId The message id.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<Void> deleteChatMessageWithRestResponse(String chatThreadId, String chatMessageId) {
        return this.serviceClient.deleteChatMessageWithRestResponse(chatThreadId, chatMessageId);
    }

    /**
     * Posts a typing event to a thread, on behalf of a user.
     * 
     * @param chatThreadId Id of the thread.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<Void> sendTypingNotificationWithRestResponse(String chatThreadId) {
        return this.serviceClient.sendTypingNotificationWithRestResponse(chatThreadId);
    }

    /**
     * Gets the members of a thread.
     * 
     * @param chatThreadId Thread id to get members for.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the members of a thread.
     */
    public Response<Page<ChatThreadMember>> listChatThreadMembersWithRestResponse(String chatThreadId) {
        return this.serviceClient.listChatThreadMembersWithRestResponse(chatThreadId);
    }

    /**
     * Gets the members of a thread.
     * 
     * @param chatThreadId Thread id to get members for.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the members of a thread.
     */
    public PagedDataResponseCollection<ChatThreadMember, Page<ChatThreadMember>> listChatThreadMembersWithPageResponse(String chatThreadId) {
        return this.serviceClient.listChatThreadMembersWithPageResponse(chatThreadId);
    }

    /**
     * Gets the members of a thread.
     * 
     * @param chatThreadId Thread id to get members for.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the members of a thread.
     */
    public PagedDataCollection<ChatThreadMember, Page<ChatThreadMember>> listChatThreadMembersWithPage(String chatThreadId) {
        return this.serviceClient.listChatThreadMembersWithPage(chatThreadId);
    }

    /**
     * Adds thread members to a thread. If members already exist, no change occurs.
     * 
     * @param chatThreadId Id of the thread to add members to.
     * @param body Thread members to be added to the thread.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<Void> addChatThreadMembersWithRestResponse(String chatThreadId, AddChatThreadMembersOptions body) {
        return this.serviceClient.addChatThreadMembersWithRestResponse(chatThreadId, body);
    }

    /**
     * Remove a member from a thread.
     * 
     * @param chatThreadId Thread id to remove the member from.
     * @param chatMemberId Id of the thread member to remove from the thread.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<Void> removeChatThreadMemberWithRestResponse(String chatThreadId, String chatMemberId) {
        return this.serviceClient.removeChatThreadMemberWithRestResponse(chatThreadId, chatMemberId);
    }

    /**
     * Creates a chat thread.
     * 
     * @param body Request payload for creating a chat thread.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<MultiStatusResponse> createChatThreadWithRestResponse(CreateChatThreadOptions body) {
        return this.serviceClient.createChatThreadWithRestResponse(body);
    }

    /**
     * Gets the list of chat threads of a user.
     * 
     * @param maxPageSize The maximum number of chat threads returned per page.
     * @param startTime The earliest point in time to get chat threads up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the list of chat threads of a user.
     */
    public Response<Page<ChatThreadInfo>> listChatThreadsWithRestResponse(Integer maxPageSize, OffsetDateTime startTime) {
        return this.serviceClient.listChatThreadsWithRestResponse(maxPageSize, startTime);
    }

    /**
     * Gets the list of chat threads of a user.
     * 
     * @param maxPageSize The maximum number of chat threads returned per page.
     * @param startTime The earliest point in time to get chat threads up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the list of chat threads of a user.
     */
    public PagedDataResponseCollection<ChatThreadInfo, Page<ChatThreadInfo>> listChatThreadsWithPageResponse(Integer maxPageSize, OffsetDateTime startTime) {
        return this.serviceClient.listChatThreadsWithPageResponse(maxPageSize, startTime);
    }

    /**
     * Gets the list of chat threads of a user.
     * 
     * @param maxPageSize The maximum number of chat threads returned per page.
     * @param startTime The earliest point in time to get chat threads up to. The timestamp should be in ISO8601 format: `yyyy-MM-ddTHH:mm:ssZ`.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the list of chat threads of a user.
     */
    public PagedDataCollection<ChatThreadInfo, Page<ChatThreadInfo>> listChatThreadsWithPage(Integer maxPageSize, OffsetDateTime startTime) {
        return this.serviceClient.listChatThreadsWithPage(maxPageSize, startTime);
    }

    /**
     * Gets the list of chat threads of a user.
     * 
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the list of chat threads of a user.
     */
    public Page<ChatThreadInfo> listChatThreads() {
        return this.serviceClient.listChatThreads();
    }

    /**
     * Updates a thread's properties.
     * 
     * @param chatThreadId The id of the thread to update.
     * @param body Request payload for updating a chat thread.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<Void> updateChatThreadWithRestResponse(String chatThreadId, UpdateChatThreadOptions body) {
        return this.serviceClient.updateChatThreadWithRestResponse(chatThreadId, body);
    }

    /**
     * Gets a chat thread.
     * 
     * @param chatThreadId Thread id to get.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return a chat thread.
     */
    public Response<ChatThread> getChatThreadWithRestResponse(String chatThreadId) {
        return this.serviceClient.getChatThreadWithRestResponse(chatThreadId);
    }

    /**
     * Deletes a thread.
     * 
     * @param chatThreadId Thread id to delete.
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<Void> deleteChatThreadWithRestResponse(String chatThreadId) {
        return this.serviceClient.deleteChatThreadWithRestResponse(chatThreadId);
    }

    /**
     * Get the next page of items.
     * 
     * @param nextLink null
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return the response.
     */
    public Response<Page<ReadReceipt>> listChatReadReceiptsNextWithRestResponse(String nextLink) {
        return this.serviceClient.listChatReadReceiptsNextWithRestResponse(nextLink);
    }

    /**
     * Get the next page of items.
     * 
     * @param nextLink null
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return collection of chat messages for a particular chat thread.
     */
    public Response<Page<ChatMessage>> listChatMessagesNextWithRestResponse(String nextLink) {
        return this.serviceClient.listChatMessagesNextWithRestResponse(nextLink);
    }

    /**
     * Get the next page of items.
     * 
     * @param nextLink null
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return collection of thread members belong to a particular thread.
     */
    public Response<Page<ChatThreadMember>> listChatThreadMembersNextWithRestResponse(String nextLink) {
        return this.serviceClient.listChatThreadMembersNextWithRestResponse(nextLink);
    }

    /**
     * Get the next page of items.
     * 
     * @param nextLink null
     * @throws IllegalArgumentException thrown if parameters fail the validation.
     * @throws ErrorException thrown if the request is rejected by server.
     * @throws RuntimeException all other wrapped checked exceptions if the request fails to be sent.
     * @return collection of chat threads.
     */
    public Response<Page<ChatThreadInfo>> listChatThreadsNextWithRestResponse(String nextLink) {
        return this.serviceClient.listChatThreadsNextWithRestResponse(nextLink);
    }

    /**
     * A builder for creating a new instance of the AzureCommunicationChatServiceClient type.
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
         * Builds an instance of AzureCommunicationChatServiceClient with the provided parameters.
         * 
         * @return an instance of AzureCommunicationChatServiceClient.
         */
        public AzureCommunicationChatServiceClient build() {
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
            return new AzureCommunicationChatServiceClient(internalClient);
        }
    }
}