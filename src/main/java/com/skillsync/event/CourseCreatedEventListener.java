package com.skillsync.event;

import com.skillsync.service.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CourseCreatedEventListener {

	private final EmailService emailService;

	public CourseCreatedEventListener(EmailService emailService) {
		this.emailService = emailService;
	}

	@Async
	@EventListener
	public void handleCourseCreated(CourseCreatedEvent event) {
		if (event == null || event.adminEmails() == null) {
			return;
		}
		event.adminEmails().forEach(email ->
				emailService.sendCourseCreatedAdmin(email, event.title(), event.instructor()));
	}
}
