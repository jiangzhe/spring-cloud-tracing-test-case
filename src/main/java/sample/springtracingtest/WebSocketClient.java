package sample.springtracingtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Scanner;

public class WebSocketClient {

    public static void main(String[] args) {
        org.springframework.web.socket.client.WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setTaskScheduler(new ConcurrentTaskScheduler());
        stompClient.setMessageConverter(new SimpleMessageConverter());
        String url = "ws://127.0.0.1:8080/ws/connect";
        StompSessionHandler sessionHandler = new SessionHandlerLogging();
        stompClient.connect(url, sessionHandler);

        new Scanner(System.in).nextLine();
    }

    private static class SessionHandlerLogging extends StompSessionHandlerAdapter {
        private static final Logger logger = LoggerFactory.getLogger(SessionHandlerLogging.class);
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            logger.info("Connected");
            session.subscribe("/ws/server", this);
            session.send("/app/client", "client call!".getBytes());
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return byte[].class;
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            logger.error("Error found in websocket", exception);
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            String result = null;
            try {
                result = new String((byte[])payload, "UTF-8");
            } catch (UnsupportedEncodingException uee) {
                /* ignored */
            }
            logger.info("Received: {}", result);
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            logger.error("TransportError", exception);
        }
    }
}
