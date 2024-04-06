package com.optimagrowth.organization.events.publisher;

import com.optimagrowth.organization.events.model.OrganizationChangeModel;
import com.optimagrowth.organization.utils.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrganizationChangePublisher {
    @Autowired
    private org.springframework.kafka.core.KafkaTemplate<String, OrganizationChangeModel> kafkaTemplate;

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

