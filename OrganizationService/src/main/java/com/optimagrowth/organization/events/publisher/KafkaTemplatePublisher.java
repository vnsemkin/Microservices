package com.optimagrowth.organization.events.publisher;

import com.optimagrowth.organization.events.model.OrganizationChangeModel;
import com.optimagrowth.organization.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Qualifier("kafkaTemplate")
public class KafkaTemplatePublisher implements Publisher {
    @Autowired
    private KafkaTemplate<String, OrganizationChangeModel> kafkaTemplate;

    public void publishOrganizationChange(String action, String organizationId) {
        log.debug("Sending Kafka message {} for Organization Id: {}", action, organizationId);
        OrganizationChangeModel change = new OrganizationChangeModel(
            OrganizationChangeModel.class.getTypeName(),
            action,
            organizationId,
            UserContext.getCorrelationId());
        kafkaTemplate.send("orgChangeTopic", change);
    }
}

