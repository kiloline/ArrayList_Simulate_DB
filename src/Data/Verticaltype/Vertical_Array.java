package Data.Verticaltype;

import java.util.*;

import com.sun.istack.internal.NotNull;
import m_Exception.runtime.insertException;

public class Vertical_Array<E> extends Vertical_Column<E> {
    /**
     *
     * @param vertical_type:E.classname
     */
    public Vertical_Array(String vertical_name,String vertical_type) {
        super(vertical_name,vertical_type);
    }
    /**
     *
     * @param vertical_type:E.classname
     */
    public Vertical_Array(LinkedList<Vertical_Node> newelement, String vertical_name,String vertical_type, boolean index) throws insertException {
        super(newelement, vertical_name,vertical_type, index);
        //realloc(newelement.length);
        if(isIndex)
            initIndex();
    }

    @Override
    public Vertical_Node<E> getindex_element(int index) {
        if(index>Size)
            return null;
        return elements[index];
    }

    @Override
    public LinkedList<Vertical_Node> getindex_elements(Integer... index) {
        LinkedList Return=new LinkedList<>();
        for(int loop=0;loop<index.length;loop++)
            Return.add(this.elements[index[loop]]);
        return Return;
    }

    @Override
    public LinkedList<Vertical_Node> getAll() {
        LinkedList Return=new LinkedList<>();
        for(int loop=0;loop<elements.length;loop++)
            Return.add(this.elements[loop]);
        return Return;
    }

    @Override
    protected Vertical_Node<E> insert(E element) throws insertException {
        Vertical_Node<E> insert=new Vertical_Node<E>(element,Size,null);
        elements[Size]=insert;
        Size++;
        if(isIndex){
            refreshIndex(element,Size);
        }
        return insert;
    }

    @Override
    public LinkedList<Vertical_Node<E>> insert(E... element) throws insertException {
        LinkedList<Vertical_Node<E>> Return=new LinkedList<>();
        for(int loop=0;loop<element.length;loop++)
            Return.add(insert(element[loop]));
        return Return;
    }

    @Override
    public LinkedList<Vertical_Node<E>> delete(Integer... line) {
        LinkedList<Vertical_Node<E>> Return=new LinkedList<Vertical_Node<E>>();
        int loop,loopc;
        LinkedList<Vertical_Node<E>> toReInit=new LinkedList<>();
        for(loop=0,loopc=0;loop<this.Size;loop++)
        {
            if(loopc<line.length)
                if(loop==line[loopc]){
                    loopc++;
                    Return.add(elements[loop]);
                    continue;
                }
            toReInit.add(elements[loop]);
        }
        //for(;loop<Size;loop++)
        this.elements=toReInit.toArray(elements);
        this.Size=toReInit.size();
        if(isIndex){//最简单省力的方法莫过于直接重建索引，而且花时间去标记索引当中所有位置并且重新计算位置的操作也未必速度就快
            index=new HashMap<>();
            initIndex();
        }
        return Return ;
    }

    @Override
    public LinkedList<Vertical_Node> update(E element, Integer... line) {
        LinkedList<Vertical_Node> Return=new LinkedList<>();
        for(int loop=0;loop<line.length;loop++)
        {
            Vertical_Node<E> toUpdate=this.elements[line[loop]];
            if(isIndex){
                index.get(toUpdate.getelement()).remove(line[loop]);
                refreshIndex(element,line[loop]);
            }
            Return.add(toUpdate.CopyNode());
            toUpdate.updateelement(element);
        }
        return Return;
    }

    @Override
    public List<Integer> Pick_Condition(String conditionSymbol, E conditionValue) throws Exception {
        List<Integer> Return = new LinkedList<>();
        if(isIndex)
        {
            Vertical_Node<E> tnode=new Vertical_Node(conditionValue,null,null);
            Iterator<E> temp=this.index.keySet().iterator();
            switch(conditionSymbol)
            {
                case "is":case "=":
                    Return=this.index.get(conditionValue);
                    break;
                case "isnot":case "!=":
                {
                    while(temp.hasNext())
                    {
                        E telement=temp.next();
                        if(tnode.compare(telement)!=0)
                            continue;
                        Return.addAll(index.get(telement));
                    }
                    break;
                }
                case ">":
                {
                    while(temp.hasNext())
                    {
                        E telement=temp.next();
                        if(tnode.compare(telement)!=1)
                            continue;
                        Return.addAll(index.get(telement));
                    }
                    break;
                }
                case "<":
                {
                    while(temp.hasNext())
                    {
                        E telement=temp.next();
                        if(tnode.compare(telement)!=-1)
                            continue;
                        Return.addAll(index.get(telement));
                    }
                    break;
                }
                case ">=":
                {
                    while(temp.hasNext())
                    {
                        E telement=temp.next();
                        if(tnode.compare(telement)==-1)
                            continue;
                        Return.addAll(index.get(telement));
                    }
                    break;
                }
                case "<=":
                {
                    while(temp.hasNext())
                    {
                        E telement=temp.next();
                        if(tnode.compare(telement)==1)
                            continue;
                        Return.addAll(index.get(telement));
                    }
                    break;
                }
            }
        }
        else
        {
            Vertical_Node toFind;
            Vertical_Node<E> tnode=new Vertical_Node(conditionValue,null,null);
            switch(conditionSymbol)
            {
            case "is":case "=":
                for(int loop=0;loop<this.Size;loop++) 
                {
                    toFind=this.elements[loop];
                    if(toFind.getelement()==null);
                    else if(tnode.compare(toFind.getelement())==0)
                        Return.add(loop);
                }
                break;
            case "isnot":case "!=":
                for(int loop=0;loop<this.Size;loop++) 
                {
                    toFind=this.elements[loop];
                    if(conditionValue==null)
                    {
                        if(toFind.getelement()!=null)
                            Return.add(loop);
                    }
                    else if(toFind.getelement()==null);
                    else if(tnode.compare(toFind.getelement())!=0)
                        Return.add(loop);
                }
                break;
            case ">=":
                for(int loop=0;loop<this.Size;loop++)  
                {
                    toFind=this.elements[loop];
                    if(toFind.getelement()==null);
                    else if(tnode.compare(toFind.getelement())!=-1)
                        Return.add(loop);
                }
                break;
            case "<=":
                for(int loop=0;loop<this.Size;loop++) 
                {
                    toFind=this.elements[loop];
                    if(toFind.getelement()==null);
                    else if(tnode.compare(toFind.getelement())!=1) 
                        Return.add(loop);
                }
                break;
            case ">":
                for(int loop=0;loop<this.Size;loop++) 
                {
                    toFind=this.elements[loop];
                    if(toFind.getelement()==null);
                    else if(tnode.compare(toFind.getelement())==1)
                        Return.add(loop);
                }
                break;
            case "<":
                for(int loop=0;loop<this.Size;loop++) 
                {
                    toFind=this.elements[loop];
                    if(toFind.getelement()==null);
                    else if(tnode.compare(toFind.getelement())==-1)
                        Return.add(loop);
                }
                break;
            }
            if(Return==null)
                Return=new LinkedList<>();
        }
        return Return;
    }

    @Override
    public Vertical_Array<E> checkout(@NotNull String newVerticalName, Integer... p_c) {
        Vertical_Array<E> Return;
        try {
            Return=new Vertical_Array<E>(this.getindex_elements(p_c),newVerticalName,Vertical_type,false);
        } catch (insertException ex) {
            Return=new Vertical_Array<E>(newVerticalName,this.Vertical_type);
        }
        return Return;
    }

    @Override
    public void initIndex() {
        this.isIndex=true;
        index=new HashMap<>();
        Vertical_Node<E> toIndex;
        
        for(int loop=0;loop<Size;loop++)
        {
            toIndex=this.elements[loop];
            refreshIndex(toIndex.getelement(),loop);
        }
    }

    @Override
    public void dropIndex() {
        this.isIndex=false;
        index=null;
    }
    
    @Override
    public void run() {

    }
    
    private boolean refreshIndex(E element,Integer indexnum)
    {
        List<Integer> ll=index.get(element);
        if(ll==null)
        {
            ll=new ArrayList<Integer>();
            index.put(element, ll);
        }

        ll.add(indexnum);
        return true;
    }
}
