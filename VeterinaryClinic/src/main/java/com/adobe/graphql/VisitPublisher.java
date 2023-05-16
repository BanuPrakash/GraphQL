package com.adobe.graphql;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import com.adobe.dao.VisitRepository;
import com.adobe.entity.Visit;
import com.adobe.service.VisitCreatedEvent;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

@Slf4j
@Component
public class VisitPublisher {


    private final Sinks.Many<Visit> sink;
    private final VisitRepository visitRepository;
    public VisitPublisher(VisitRepository visitRepository) {
    	this.visitRepository = visitRepository;
        this.sink = Sinks.many()
            .multicast()
            .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @TransactionalEventListener
    @Transactional
    public void onNewVisit(VisitCreatedEvent event) {
    	log.info("Application Event  : {} " , event);
        Visit visit = visitRepository.findById(event.visitId()).get();
        
            this.sink.emitNext(visit, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    public Flux<Visit> getPublisher() {
        return this.sink.asFlux();
    }
}
