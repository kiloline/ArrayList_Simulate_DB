package Data.Vessel;

/**
 * <br>应对SQL当中需要整合表空间、表二者的名称的时候。</br>
 * <br>未来也可能扩展成表空间、sequence名的形式。</br>
 */
public class TablespaceTable_name extends Word {
    private String space,table;
    public TablespaceTable_name(Word first) {
        super("TablespaceTable_name", "", first.getLocal()[0], first.getLocal()[1], false);
    }

    public TablespaceTable_name setAll(Word space,Word table)
    {
        if(space!=null) {
            this.space = space.getSubstance();
            this.substance=this.space+'.';
        }
        this.table=table.getSubstance();
        this.substance=substance+this.table;

        return this;
    }

    public String getTable() {
        return table;
    }

    public String getSpace() {
        return space;
    }

    @Override
    public String toString()
    {
        return space+'.'+table;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj.getClass().getName().equals(this.getClass().getName())) {
            if(((TablespaceTable_name)obj).getSpace().equals(space) &&
                    ((TablespaceTable_name)obj).getTable().equals(table))
                return true;
            else return false;
        }
        return false;
    }
}
