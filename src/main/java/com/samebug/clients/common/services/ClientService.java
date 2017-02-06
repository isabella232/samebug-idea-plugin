package com.samebug.clients.common.services;

import com.intellij.util.messages.MessageBus;
import com.samebug.clients.common.messages.ConnectionStatusListener;
import com.samebug.clients.common.search.api.client.*;
import com.samebug.clients.common.search.api.entities.tracking.TrackEvent;
import com.samebug.clients.common.search.api.exceptions.SamebugClientException;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * NOTE: these services use idea MessageBus only for convenience, any other observer implementation would do.
 */
final public class ClientService {
    final MessageBus messageBus;
    SamebugClient client;
    AtomicBoolean connected;
    AtomicBoolean authenticated;
    AtomicInteger nRequests;
    AtomicReference<String> apiStatus;

    public ClientService(MessageBus messageBus) {
        this.messageBus = messageBus;
    }

    public synchronized void configure(final Config config) {
        this.client = new SamebugClient(config);
        this.connected = new AtomicBoolean(true);
        this.authenticated = new AtomicBoolean(true);
        this.nRequests = new AtomicInteger(0);
        this.apiStatus = new AtomicReference<String>(null);
    }

    public boolean isConnected() {
        return connected.get();
    }

    public boolean isAuthenticated() {
        return authenticated.get();
    }

    public int getNumberOfActiveRequests() {
        return nRequests.get();
    }

    @Nullable
    public String getApiStatus() {
        return apiStatus.get();
    }

    public void trace(final TrackEvent event) throws SamebugClientException {
        // Trace bypasses connection status handling.
        client.trace(event);
    }

    public <T> T execute(ConnectionAwareHttpRequest<T> c) throws SamebugClientException {
        final ClientResponse<T> response;

        c.start();
        nRequests.incrementAndGet();
        messageBus.syncPublisher(ConnectionStatusListener.TOPIC).startRequest();
        response = c.request();

        ConnectionStatus connectionStatus = response.getConnectionStatus();
        if (connectionStatus.attemptToConnect && connected.get() != connectionStatus.successfullyConnected) {
            connected.set(connectionStatus.successfullyConnected);
            messageBus.syncPublisher(ConnectionStatusListener.TOPIC).connectionChange(connectionStatus.successfullyConnected);
        }
        if (connectionStatus.attemptToAuthenticate && authenticated.get() != connectionStatus.successfullyAuthenticated) {
            authenticated.set(connectionStatus.successfullyAuthenticated);
            messageBus.syncPublisher(ConnectionStatusListener.TOPIC).authenticationChange(connectionStatus.successfullyAuthenticated);
        }
        if (connectionStatus.apiStatus != null && !connectionStatus.apiStatus.equals(apiStatus.get())) {
            apiStatus.set(connectionStatus.apiStatus);
            if (ConnectionStatus.API_TO_BE_DEPRECATED.equals(connectionStatus.apiStatus)) {
                messageBus.syncPublisher(ConnectionStatusListener.TOPIC).apiToBeDeprecated();
            } else if (ConnectionStatus.API_DEPRECATED.equals(connectionStatus.apiStatus)) {
                messageBus.syncPublisher(ConnectionStatusListener.TOPIC).apiDeprecated();
            }
        }

        try {
            if (response instanceof Success) {
                T result = ((Success<T>) response).getResponse();
                c.success(result);
                return result;
            } else {
                SamebugClientException exception = ((Failure<T>) response).getException();
                c.fail(exception);
                throw exception;
            }
        } finally {
            nRequests.decrementAndGet();
            messageBus.syncPublisher(ConnectionStatusListener.TOPIC).finishRequest(connectionStatus);
            c.finish(response);
        }

    }

    public static abstract class ConnectionAwareHttpRequest<T> {
        abstract ClientResponse<T> request();

        protected void start() {
        }

        protected void success(T result) {
        }

        protected void fail(SamebugClientException e) {
        }

        protected void finish(ClientResponse<T> response) {
        }
    }

}
