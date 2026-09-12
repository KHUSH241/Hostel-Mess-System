package com.hms.model;

public class Student extends User {
    private final String hostelBlock;
    private final String roomNumber;

    public Student(String userId, String username, String password, String fullName,
                    String hostelBlock, String roomNumber) {
        super(userId, username, password, fullName);
        this.hostelBlock = hostelBlock;
        this.roomNumber = roomNumber;
    }

    public String getRoomNumber() { return roomNumber; }
    public String getHostelBlock() { return hostelBlock; }

    @Override
    public String getRole() { return "STUDENT"; }

    @Override
    public String toCsvLine() {
        return String.join("|", "STUDENT", getUserId(), getUsername(), getRawPassword(),
                getFullName(), hostelBlock, roomNumber);
    }
}
