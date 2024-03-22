package com.nix.ecommerceapi.listener;

import com.nix.ecommerceapi.event.RegisterCustomerEvent;
import com.nix.ecommerceapi.service.MailService;
import com.nix.ecommerceapi.utils.TemplateUtils;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.nix.ecommerceapi.model.constants.AppConstants.MAIL_VERIFY_EXPIRED_TIME_MINUTES;

@Component
@AllArgsConstructor
public class RegisterCustomerListener {
    private final MailService mailService;

    @Async
    @EventListener
    public void sendMail(RegisterCustomerEvent event) {
        mailService.sendMail(
                event.user().getEmail(),
                "Verify your email",
                TemplateUtils.getMailRegisterTemplate(
                        event.user().getEmail(),
                        MAIL_VERIFY_EXPIRED_TIME_MINUTES + " phút",
                        "http://localhost:8080/api/v1/auth/verify-email?token=" + event.user().getToken()
                )
        );
    }

}
