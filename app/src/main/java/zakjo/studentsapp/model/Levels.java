package zakjo.studentsapp.model;public class Levels {    public int stage_id ;    public String stage_name ;    public Levels(int stage_id, String stage_name) {        this.stage_id = stage_id;        this.stage_name = stage_name;    }    public Levels() {    }    public int getStage_id() {        return stage_id;    }    public void setStage_id(int stage_id) {        this.stage_id = stage_id;    }    public String getStage_name() {        return stage_name;    }    public void setStage_name(String stage_name) {        this.stage_name = stage_name;    }}