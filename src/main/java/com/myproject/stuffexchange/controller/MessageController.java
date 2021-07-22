//package com.myproject.stuffexchange.controller;
//
//
//import com.myproject.stuffexchange.model.Message;
//import com.myproject.stuffexchange.model.OutputMessage;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.DestinationVariable;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.security.Principal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@RestController
//public class MessageController {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("message/{to}")
//    public void sendMessage(@DestinationVariable String to, Message message){
//        messagingTemplate.convertAndSend("/topic/messages" + to, message);
//    }
//
//    @MessageMapping("/secured/room")
//    public  void sendSpecific(@Payload Message message, Principal user, @Header("simpSessionId") String sessionId) throws Exception{
//        OutputMessage outputMessage = new OutputMessage(
//                message.getFrom(),
//                message.getMessage(),
//                new SimpleDateFormat("HH:mm").format(new Date()));
//
//        messagingTemplate.convertAndSendToUser(
//                message.getTo(),
//                "/secured/user/queue/specific-user",
//                outputMessage);
//
//
//    }
//
//
//}
