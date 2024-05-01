package ru.artemov.tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    @Test
    void testEquals() {
        Epic epic1 = new Epic("Epic1", "descrEpic1");
        Epic epic2 = new Epic("Epic1", "descrEpic1");

        epic1.setId(1);
        epic2.setId(1);
        assertEquals(epic1, epic2, "Задачи не совпадают.");

    }


}