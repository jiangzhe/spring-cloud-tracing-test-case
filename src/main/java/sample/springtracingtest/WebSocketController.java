package sample.springtracingtest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @MessageMapping("/client")
    @SendTo("/ws/server")
    public byte[] receiveMessageFromClient(String message) {
        logger.info("Received message from client");
        return ("I hear you, your message is: " + message).getBytes();
    }

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;


    @Scheduled(fixedRate = 5000)
    public void timing() {
        logger.info("Periodically send timing to client");
        simpMessagingTemplate.convertAndSend("/ws/server",
                LocalDateTime.now().format(dateFormatter).getBytes());
    }
}
