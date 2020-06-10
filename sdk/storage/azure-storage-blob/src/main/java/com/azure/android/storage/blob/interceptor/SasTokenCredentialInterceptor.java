// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.android.storage.blob.interceptor;

import com.azure.android.storage.blob.credentials.SasTokenCredential;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static com.azure.android.core.util.CoreUtil.isNullOrEmpty;

/**
 *  Interceptor that append SAS token to the request Uri.
 */
public class SasTokenCredentialInterceptor implements Interceptor {
    private final SasTokenCredential credential;

    public SasTokenCredentialInterceptor(SasTokenCredential credential) {
        this.credential = credential;
    }

    /**
     * Intercept the current request in the pipeline and apply the SAS token.
     *
     * @param chain provide access to the request to apply the SAS token
     *
     * @return response from the next interceptor in the pipeline
     * @throws IOException if an IO error occurs while processing the request and response
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        HttpUrl requestURL = chain.request().url();

        String sasToken = this.credential.getSasToken();
        sasToken = sasToken.startsWith("?")
            ? sasToken.substring(1)
            : sasToken;

        // SAS token is already encoded so its safe to append it to the encoded query from source request.
        String encodedQuery = requestURL.encodedQuery();

        if (isNullOrEmpty(encodedQuery)) {
            encodedQuery = sasToken;
        } else {
            encodedQuery += "&" + sasToken;
        }

        HttpUrl newURL = requestURL
            .newBuilder()
            .encodedQuery(encodedQuery)
            .build();

        Request newRequest = chain.request()
                .newBuilder()
                .url(newURL)
                .build();
        return chain.proceed(newRequest);
    }
}
