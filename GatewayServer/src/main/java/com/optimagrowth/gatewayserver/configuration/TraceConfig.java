package com.optimagrowth.gatewayserver.configuration;

import brave.Tracer;
import brave.context.slf4j.MDCScopeDecorator;
import brave.propagation.CurrentTraceContext;
import brave.propagation.ThreadLocalCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveBaggageManager;
import io.micrometer.tracing.brave.bridge.BraveCurrentTraceContext;
import io.micrometer.tracing.brave.bridge.BraveTracer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class TraceConfig {

    @Bean
    public CurrentTraceContext braveCurrentTraceContext() {
        return ThreadLocalCurrentTraceContext.newBuilder()
            .addScopeDecorator(MDCScopeDecorator.get())
            .build();
    }

    @Bean
    public BraveTracer braveTracerImpl(Tracer braveTracer, CurrentTraceContext braveCurrentTraceContext) {
        return new BraveTracer(braveTracer, new BraveCurrentTraceContext(braveCurrentTraceContext),
            new BraveBaggageManager());
    }


    @Bean
    public GlobalFilter postGlobalFilter(BraveTracer tracer) {
        return (exchange, chain) -> chain.filter(exchange)
            .then(Mono.fromRunnable(() -> {
                if (tracer.currentSpan() != null) {
                    String traceId = tracer.currentSpan().context()
                        .traceId();
                    log.info("Adding the correlation id to the outbound headers. {} ", traceId);
                    exchange.getResponse().getHeaders()
                        .add("trace-id", traceId);
                } else {
                    log.info("No span in the current trace");
                }
            }));
    }
}


