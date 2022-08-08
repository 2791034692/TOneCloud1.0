package net.t1y.cloud.database.data;

public class Delete {
    private String table;
    private String where;
    private int timestamp;

    public String getWhere() {
        return where;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
