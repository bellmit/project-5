package com.kindminds.drs.web.ctrl;

import org.elasticsearch.indices.flush.SyncedFlushService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
//@Slf4j
public class EmitterService {

    Map<Integer , SseEmitter> emitters = new HashMap<Integer , SseEmitter>();

    public void addEmitter(SseEmitter emitter , int userId) {
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        //if(emitters.get(userId) == null) {
            emitters.put( userId,emitter);
        //}

    }

    public void pushNotification(int userid, String eventName, String message) {
        //log.info("pushing {} notification for user {}", message, username);
        List<SseEmitter> deadEmitters = new ArrayList<>();

        /*
        Notification payload = Notification
                .builder()
                .from(name)
                .message(message)
                .build();

         */

      SseEmitter  emitter = emitters.get(userid);
      if(emitter != null){
        try {

            emitter.send(SseEmitter
                    .event()
                    .name(eventName)
                    .data(message));

        } catch (IOException e) {
            e.printStackTrace();
            deadEmitters.add(emitter);
        }

        //todo arthur
        //emitters.removeAll(deadEmitters);
      }else{

          //todo arthur do we have to ?
        //emitter = new SseEmitter(24 * 60 * 60 * 1000l);
         //this.addEmitter(emitter , userid);
      }

    }
}