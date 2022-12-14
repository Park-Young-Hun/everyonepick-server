package soma.everyonepick.api.album.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import soma.everyonepick.api.core.kafka.service.FaceSwapProducer;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableAsync
public class FaceSwapRequestEventListener {
    private final FaceSwapProducer faceSwapProducer;

    /**
     * 합성요청 이벤트가 들어오면 Kafka Broker 에 데이터를 보낸다.
     * @param faceSwapRequestEvent 합성요청 이벤트
     */
    @Async
    @EventListener
    public void send(FaceSwapRequestEvent faceSwapRequestEvent) {
        faceSwapProducer.sendMessage(faceSwapRequestEvent.getFaceSwapRequestDto());
        log.info("message send");
    }
}
