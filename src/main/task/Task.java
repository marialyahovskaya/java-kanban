package task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Task implements Comparable<Task> {

    protected Integer id;
    protected String title;
    protected String description;
    protected TaskStatus status;
    protected TaskType type;
    protected int duration;
    protected LocalDate startTime;

    public Task(Integer id, String title, String description, TaskStatus status, int duration, LocalDate startTime) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.type = TaskType.TASK;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Integer getId() {
        return id;
    }

    public TaskType getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return LocalDateTime.of(startTime, LocalTime.of(0, 0)).plusMinutes(duration);
    }

    @Override
    public int compareTo(Task task) {
        if (startTime.isBefore(task.getStartTime()))
            return -1;
        else if (startTime.isAfter(task.getStartTime()))
            return 1;
        else
            return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return Objects.equals(id, task.id)
                && Objects.equals(title, task.title)
                && Objects.equals(description, task.description)
                && Objects.equals(status, task.status)
                && duration == task.duration
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, status, duration, startTime);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }
}
