package Data.Verticaltype;

import java.util.HashMap;
import java.util.Iterator;
import m_Exception.runtime.insertException;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public Vertical_Array(LinkedList<E> newelement, String vertical_name,String vertical_type, boolean index) throws insertException {
        super(newelement, vertical_name,vertical_type, index);
        //realloc(newelement.length);
        if(isIndex)
            initIndex();
    }

    @Override
    public E getindex_element(int index) {
        return elements[index].getelement();
    }

    @Override
    public LinkedList<E> getindex_elements(Integer... index) {
        LinkedList<E> Return=new LinkedList<>();
        for(int loop=0;loop<index.length;loop++)
            Return.add(this.elements[index[loop]].getelement());
        return Return;
    }

    @Override
    public LinkedList<E> getAll() {
        LinkedList<E> Return=new LinkedList<>();
        for(int loop=0;loop<elements.length;loop++)
            Return.add(this.elements[loop].getelement());
        return Return;
    }

    @Override
    protected Vertical_Node<E> insert(E element) throws insertException {
        Vertical_Node<E> insert=new Vertical_Node<E>(element,Size,null);
        elements[Size]=insert;
        Size++;
        if(isIndex)
        {
            if(index.get(element)==null)
                index.put(element,new LinkedList<Integer>());
            index.get(element).add(Size);
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
        return Return ;
    }

    @Override
    public LinkedList<Vertical_Node> update(E element, Integer... line) {
        LinkedList<Vertical_Node> Return=new LinkedList<>();
        for(int loop=0;loop<line.length;loop++)
        {
            Return.add(this.elements[line[loop]].CopyNode());
            this.elements[line[loop]].updateelement(element);
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
    public Vertical_Array<E> checkout(Integer... p_c) {
        Vertical_Array<E> Return;
        try {
            Return=new Vertical_Array<E>(this.getindex_elements(p_c),Vertical_name,Vertical_type,false);
        } catch (insertException ex) {
            Return=new Vertical_Array<E>(Vertical_name,this.Vertical_type);
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
            ll=new LinkedList<Integer>();
            ll.add(indexnum);
            index.put(element, ll);
        }
        else
            ll.add(indexnum);
        return true;
    }
}
