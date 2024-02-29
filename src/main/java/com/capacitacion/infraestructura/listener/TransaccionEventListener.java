package com.capacitacion.infraestructura.listener;

import com.capacitacion.domain.application.SendEmailService;
import com.capacitacion.domain.model.TransaccionEvent;
import org.springframework.kafka.annotation.KafkaListener;

public class TransaccionEventListener {
    private final SendEmailService sendEmailService;

    /**
     * Constructor del listener de eventos de transacciones.
     *
     * @param sendEmailService Servicio para enviar correos electrónicos (simulado).
     */
    public TransaccionEventListener(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }

    /**
     * Maneja el evento de transacción recibido desde Kafka.
     *
     * @param transaccionEvent Evento de transacción recibido.
     */
    @KafkaListener(topics = "transaccion-topic", groupId = "your-group-id")
    public void handleTransaccionEvent(TransaccionEvent transaccionEvent) {
        System.out.println("Evento de Transacción recibido. ID de Transacción: " + transaccionEvent.getTransaccionId());

        // Simula el envío de un correo electrónico al servicio de envío de correos.
        sendEmailService.sendEmail();
    }
}
