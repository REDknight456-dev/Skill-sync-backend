package com.skillsync.event;

import java.util.List;

/**
 * Application event published when a new course is created.
 */
public record CourseCreatedEvent(String title, String instructor, List<String> adminEmails) {
}
