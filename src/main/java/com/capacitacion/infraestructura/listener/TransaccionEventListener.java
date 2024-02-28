package com.capacitacion.infraestructura.listener;

import com.capacitacion.domain.application.SendEmailService;
import com.capacitacion.domain.model.TransaccionEvent;
import org.springframework.kafka.annotation.KafkaListener;

public class TransaccionEventListener {
    private final SendEmailService sendEmailService;

    public TransaccionEventListener(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }

    @KafkaListener(topics = "transaccion-topic", groupId = "your-group-id")
    public void handleTransaccionEvent(TransaccionEvent transaccionEvent) {
        System.out.println("Evento de Transacción recibido. ID de Transacción: " + transaccionEvent.getTransaccionId());

        // Llamada al servicio sendEmail (que en realidad imprime por consola)
        sendEmailService.sendEmail();
    }
}
