package be.thomasmore.dogchecker.worker.converter;

import be.thomasmore.dogchecker.worker.dto.WorkerRequestDTO;
import io.smallrye.reactive.messaging.MessageConverter;
import io.smallrye.reactive.messaging.amqp.IncomingAmqpMetadata;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Message;
import java.lang.reflect.Type;

@ApplicationScoped
public class WorkerRequestConverter implements MessageConverter {

    @Override
    public boolean canConvert(Message<?> message, Type target) {
        // Only convert messages from AMQP and where target type is WorkerRequestDTO
        return message.getMetadata(IncomingAmqpMetadata.class).isPresent()
                && target.equals(WorkerRequestDTO.class);
    }

    @Override
    public Message<?> convert(Message<?> message, Type target) {
        Object payload = message.getPayload();
        WorkerRequestDTO dto;
        if (payload instanceof JsonObject json) {
            dto = json.mapTo(WorkerRequestDTO.class);
        } else if (payload instanceof String str) {
            dto = new JsonObject(str).mapTo(WorkerRequestDTO.class);
        } else {
            throw new IllegalArgumentException("Unexpected payload: " + payload.getClass());
        }
        return message.withPayload(dto);
    }
}
