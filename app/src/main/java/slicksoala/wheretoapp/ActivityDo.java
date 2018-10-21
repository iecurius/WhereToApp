package slicksoala.wheretoapp;

public enum ActivityDo {
    PLACESTOGO("Sightseeing"),
    STUFFTOEAT("Food"),
    THINGSTODO("What To Do");

    private String val;

    ActivityDo(String val) { this.val = val;}
    public String toString() { return this.val;}
}
