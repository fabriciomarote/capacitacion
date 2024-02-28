package com.capacitacion.domain.application.impl;

import com.capacitacion.domain.application.SendEmailService;
import org.springframework.stereotype.Service;

@Service
public class SendEmailServiceImpl implements SendEmailService {
    @Override
    public void sendEmail() {
        System.out.println("Enviando correo...");
    }
}
