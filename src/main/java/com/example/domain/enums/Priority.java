package com.example.domain.enums;

/**
 * Enum приоритет задачи
 */
public enum Priority {
    LOW("низкий"),
    MIDDLE("средний"),
    HIGH("высокий");

    private String description;
    Priority(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
