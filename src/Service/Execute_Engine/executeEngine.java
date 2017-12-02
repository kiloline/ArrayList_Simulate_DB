package Service.Execute_Engine;

import Data.Vessel.ExecutePlan_Package;
import Data.Vessel.Word;
import Data.classes.Table;
import Utils.AOP.BeforeFunction;
import Utils.AOP.BeforeFunctionImpl;

import java.util.LinkedList;

public class executeEngine {
    private boolean join,union,child;
    private ExecutePlan_Package thisPackage;
    private LinkedList<Table> JoinTables,UnionTables,ChildTables;
    public executeEngine getExecPackage(ExecutePlan_Package packages)
    {
        this.thisPackage=packages;
        join=thisPackage.checkjoin();
        union=thisPackage.checkunion();
        child=thisPackage.checkchild();
        return this;
    }

    @BeforeFunction
    private void getJoinTables()
    {
        JoinTables=new LinkedList<>();
        if(join){
            for(ExecutePlan_Package join:thisPackage.getjoinEPP())
            {
                JoinTables.add(new executeEngine().getExecPackage(join).start_domain());
            }
        }
    }
    @BeforeFunction
    private void getUnionTables()
    {
        UnionTables=new LinkedList<>();
        if(union){
            for(ExecutePlan_Package union:thisPackage.getunionEPP())
            {
                UnionTables.add(new executeEngine().getExecPackage(union).start_domain());
            }
        }
    }
    @BeforeFunction
    private void getChildTables()
    {
        ChildTables=new LinkedList<>();
        if(child){
            for(ExecutePlan_Package child:thisPackage.getchildEPP())
            {
                ChildTables.add(new executeEngine().getExecPackage(child).start_domain());
            }
        }
    }

    public Table start_domain()
    {
        BeforeFunctionImpl.before();

return null;
    }
}
