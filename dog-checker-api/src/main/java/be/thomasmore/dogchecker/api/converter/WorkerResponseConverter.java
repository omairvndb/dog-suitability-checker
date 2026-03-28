package be.thomasmore.dogchecker.api.converter;

import be.thomasmore.dogchecker.api.dto.WorkerResponseDTO;
import io.smallrye.reactive.messaging.MessageConverter;
import io.smallrye.reactive.messaging.amqp.IncomingAmqpMetadata;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Message;
import java.lang.reflect.Type;

@ApplicationScoped
public class WorkerResponseConverter implements MessageConverter {

    @Override
    public boolean canConvert(Message<?> message, Type target) {
        // Only convert messages from AMQP and where target type is WorkerResponseDTO
        return message.getMetadata(IncomingAmqpMetadata.class).isPresent()
                && target.equals(WorkerResponseDTO.class);
    }

    @Override
    public Message<?> convert(Message<?> message, Type target) {
        Object payload = message.getPayload();
        WorkerResponseDTO dto;
        if (payload instanceof JsonObject json) {
            dto = json.mapTo(WorkerResponseDTO.class);
        } else if (payload instanceof String str) {
            dto = new JsonObject(str).mapTo(WorkerResponseDTO.class);
        } else {
            throw new IllegalArgumentException("Unexpected payload: " + payload.getClass());
        }
        return message.withPayload(dto);
    }
}
