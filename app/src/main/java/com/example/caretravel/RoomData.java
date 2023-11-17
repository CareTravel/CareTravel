package com.example.caretravel;
import java.util.HashMap;
import java.util.Map;

public class RoomData {
    private String name;
    private String password;
    private String location;
    private String startDate;
    private String endDate;
    private String member;
    private String memo;

    public RoomData() {
        // Default constructor required for calls to DataSnapshot.getValue(RoomData.class)
    }

    public RoomData(String name, String password, String location, String startDate, String endDate, String member, String memo) {
        this.name = name;
        this.password = password;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.member = member;
        this.memo = memo;
    }

    // Getter and setter methods (생략)

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("password", password);
        map.put("location", location);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        map.put("member", member);
        map.put("memo", memo);
        return map;
    }

    public String getName() { return name; }
    public void setName(String name) { name = name; }
}
