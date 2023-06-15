public enum TableStatus {
    Y("1", true), N("0",false);

    private String table1Val;
    private boolean table2Val;

    TableStatus(String table1Val, boolean table2Val) {
        this.table1Val = table1Val;
        this.table2Val = table2Val;
    }

    public String getTable1Value() {
        return table1Val;
    }

    public boolean isTable2Value() {
        return table2Val;
    }


}

class Exam22 {
    
    
}
