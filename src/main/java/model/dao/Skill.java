package model.dao;

import lombok.Data;

@Data
public class Skill {
    private long skill_id;
    private String language;
    private  Level level;

    public enum Level {
        junior,
        middle,
        senior
    }
}