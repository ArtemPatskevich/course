package main.utils;

import lombok.Data;

@Data
public final class StudentSession {
    private static StudentSession instance;

    private long studentId;
    private long groupId;

    private StudentSession() {
        this.studentId = 0;
        this.groupId = 0;
    }

    public static StudentSession getInstance() {
        if(instance == null) {
            instance = new StudentSession();
        }
        return instance;
    }

    public void setUpFields(long studentId, long groupId) {
        this.studentId = studentId;
        this.groupId = groupId;
    }

    public void cleanUserSession() {
        studentId = 0;
        groupId = 0;
    }
}
