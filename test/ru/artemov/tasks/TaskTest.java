package ru.artemov.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testEquals() {
        Task task1 = new Task("task1", "desc1", Status.NEW);
        Task task2 = new Task("task1", "desc1", Status.NEW);
        task1.setId(1);
        task2.setId(1);
        assertEquals(task1, task2, "Задачи не совпадают.");

    }
}