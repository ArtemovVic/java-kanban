package ru.artemov.tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private Epic epic;

    public SubTask(String title, String description, Status status, Epic epic) {
        super(title, description, status, TypeOfTasks.SUBTASK);
        this.epic = epic;
    }

    public SubTask(String title, String description, Status status, Epic epic, LocalDateTime startTime, Duration duration) {
        super(title, description, status, TypeOfTasks.SUBTASK);
        this.epic = epic;
        this.startTime = startTime;
        this.duration = duration;
    }

    public SubTask(String title, String description, Status status, Epic epic, int id) {
        super(title, description, status, TypeOfTasks.SUBTASK);
        this.epic = epic;
        this.id = id;
    }

    public SubTask(String title, String description, Status status, Epic epic, int id, LocalDateTime startTime, Duration duration) {
        super(title, description, status, TypeOfTasks.SUBTASK);
        this.epic = epic;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public SubTask(SubTask subTask) {
        super(subTask);
        this.epic = subTask.getEpic();
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epic=" + epic.title +
                ", title='" + title + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
