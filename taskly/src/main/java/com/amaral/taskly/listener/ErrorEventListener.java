package com.amaral.taskly.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.amaral.taskly.event.ErrorEvent;
import com.amaral.taskly.service.ServiceSendEmail;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorEventListener {

    private final ServiceSendEmail serviceSendEmail;

    @EventListener
    public void handleErrorEvent(ErrorEvent event) {
        try {
            log.error("Sending error email: {}", event.getSubject());
            serviceSendEmail.sendEmailHtml(
                    event.getSubject(),
                    event.getDetails(),
                    "amaral_adm@hotmail.com"
            );
        } catch (Exception e) {
            log.error("Failed to send error email", e);
        }
    }
}
