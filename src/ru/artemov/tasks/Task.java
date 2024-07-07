package ru.artemov.tasks;

import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected Status status;
    protected final TypeOfTasks type;


    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TypeOfTasks.TASK;
    }

    public Task(String title, String description, Status status, int id) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TypeOfTasks.TASK;
        this.id = id;
    }

    public Task(String title, String description, Status status, TypeOfTasks type) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = type;
    }

    public Task(Task task) {
        this.title = task.getTitle();
        this.description = task.getDescription();
        this.id = task.getId();
        this.status = task.getStatus();
        this.type = task.getType();
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TypeOfTasks getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status, type);
    }
}
