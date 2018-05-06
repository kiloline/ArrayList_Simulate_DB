package Data.Vessel;

/**
 * <br>应对SQL当中需要整合表空间、表、列三者的名称的时候。</br>
 */
public class TablespaceTableList_name extends Word {
    private String space,table,list;
    public TablespaceTableList_name(Word first) {
        super("TablespaceTableList_name", "", first.getLocal()[0], first.getLocal()[1], false);
    }

    public TablespaceTableList_name setAll(Word space,Word table,Word list)
    {
        if(space!=null) {
            this.space = space.getSubstance();
            this.substance=this.space+'.';
        }
        if(table!=null) {
            this.table = table.getSubstance();
            this.substance=substance+this.table+'.';
        }

        this.list=list.getSubstance();
        this.substance=substance+this.list;
        return this;
    }

    public String getTable() {
        return table;
    }

    public String getSpace() {
        return space;
    }

    public String getList() {
        return list;
    }
}
