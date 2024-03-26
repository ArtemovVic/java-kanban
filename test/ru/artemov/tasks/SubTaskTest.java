package ru.artemov.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    @Test
    void testEquals() {
        Epic epic1 = new Epic("Epic1", "descrEpic1");
        SubTask subTask1 = new SubTask("SubTask", "descSub", Status.NEW, epic1);
        SubTask subTask2 = new SubTask("SubTask", "descSub", Status.NEW, epic1);
        subTask1.setId(1);
        subTask2.setId(1);
        assertEquals(subTask1, subTask2, "Задачи не совпадают.");

    }

}