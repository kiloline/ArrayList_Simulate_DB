package Data.Vessel;

public class Where_Subcondition {
    private TablespaceTableList_name left;
    private Word middle;
    private Evaluate_Word_List right;
    private TablespaceTable_name belongTable;

    public TablespaceTable_name getBelongTable() {
        return belongTable;
    }

    public Where_Subcondition setBelongTable(TablespaceTable_name belongTable) {
        this.belongTable = belongTable;
        return this;
    }

    public Where_Subcondition SetConditions(TablespaceTableList_name left,Word middle,Evaluate_Word_List right){
        this.left=left;this.right=right;this.middle=middle;
        return this;
    }

}
