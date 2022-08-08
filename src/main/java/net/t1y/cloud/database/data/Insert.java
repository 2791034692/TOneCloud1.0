package net.t1y.cloud.database.data;

public class Insert {
    private String table;
    private String column;
    private String value;
    private int timestamp;

    public int getTimestamp() {
        return timestamp;
    }

    public String getTable() {
        return table;
    }

    public String getValue() {
        return value;
    }

    public String getColumn() {
        return column;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
