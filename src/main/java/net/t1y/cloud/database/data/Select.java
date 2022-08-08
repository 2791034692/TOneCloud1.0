package net.t1y.cloud.database.data;

public class Select {
    private String table;
    private String column;
    private String where;
    private String sort;
    private String sort_column;
    private int timestamp;

    public String getColumn() {
        return column;
    }

    public String getTable() {
        return table;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public String getWhere() {
        return where;
    }

    public String getSort() {
        return sort;
    }

    public String getSort_column() {
        return sort_column;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public void setSort_column(String sort_column) {
        this.sort_column = sort_column;
    }
}
