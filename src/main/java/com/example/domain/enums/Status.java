package com.example.domain.enums;

/**
 * Enum статус задачи
 */
public enum Status {
    PENDING("в ожидании"),
    PROGRESS("в процессе"),
    COMPLETED("завершено");

    private String description;
    Status(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
