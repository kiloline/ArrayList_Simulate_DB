package Data.Verticaltype;

import Data.judgement.change_direction;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import m_Exception.runtime.insertException;

/**
 *
 * @author rkppo
 * set:删除的时候能够自动的从数组中清除并且不用复杂的重新整理数组
 * hashmap中的integer记录element所在的数组位置
 * elements数组负责内存分配功能和主存储
 * elements中的node用index_label存储自身的插入顺序
 * node之间整理一条list，使得auto_changed_indexlocal可以调用
 * empty_array_local记录数组中的空位置，这样在realloc的时候就不用对现有的node分布做调整
 * 
 * 由这个结构，所以所有传入的位置值都应该是链表的位置值，而不是数组的下标值，这之间必须经过一次转换
 */
public class Vertical_Set<E> extends Vertical_Column<E>{
    Vertical_Node<E> head,bottom;
    LinkedList<Integer> empty_array_local;
    HashMap<E,Integer> singal; //element,array_local
    HashMap<Integer,Integer> Addr_mapping; //insert_local,array_local ，问题是这个结构很难做到实时更新
    

    public Vertical_Set(String Vertical_name, String vertical_type) {
        super(Vertical_name, vertical_type);
        head=null;
        bottom=null;
        singal=new HashMap<>();
        empty_array_local=new LinkedList<>();
        empty_array_local.add(0);
    }
    protected Vertical_Set(LinkedList<E> newelement, String Vertical_name, String vertical_type, boolean index) {//尚未完成的初始化方法
        this(Vertical_name, vertical_type);
        for(int loop=0;loop<mem;loop++)
        {
            empty_array_local.add(loop);
        }
    }

    @Override
    public E getindex_element(int index) {
        return elements[this.linklocal2arraylocal(index)].getelement();
    }

    @Override
    public LinkedList<E> getindex_elements(Integer... index) {
        //Arrays.sort(index); //应该所有传入的次序都是由小到大的，这个排序暂时不启用
        Vertical_Node<E> node=head;
        LinkedList<E> Return = new LinkedList<>();
        int loopi=0;
        for(int loop=0;loop<index.length;loop++)
        {
            Return.add(getindex_element(index[loop]));
        }
        return Return;
    }

    @Override
    public LinkedList<E> getAll() {
        Vertical_Node<E> node=head;
        LinkedList<E> Return = new LinkedList<>();
         while(node.getNextNode()!=null)
         {
             Return.add(node.getelement());
             node=node.getNextNode();
         }
         return Return;
    }

    @Override
    protected Vertical_Node<E> insert(E element) throws insertException {
        int local=this.empty_array_local.removeFirst();
        Vertical_Node<E> insert = null;
        try{
            if(singal.get(element)!=null)
                throw new Exception();
            singal.put(element,local);
            insert=new Vertical_Node<>(element,Size,null);
            elements[local]=insert;
            bottom.setNextNode(insert);
            insert.setpreviousNode(bottom);
            
            bottom=insert;
        }
        catch(NullPointerException e)
        {
            if(bottom==null)
            {
                head=insert;
                bottom=head;
            }
        }
        catch(Exception e)
        {
            this.empty_array_local.addFirst(local);
            throw new insertException("违反了唯一约束");
        }
        Size++;
        return insert;
    }

    @Override
    public LinkedList<Vertical_Node<E>> insert(E... element) throws insertException {
        LinkedList<Vertical_Node<E>> Return =new LinkedList<>();
        for(int loop=0;loop<element.length;loop++)
        {
            Return.add(insert(element[loop]));
        }
        return Return;
    }

    @Override
    public LinkedList<Vertical_Node<E>> delete(Integer... line) {
        LinkedList<Vertical_Node<E>> Return=new LinkedList<Vertical_Node<E>>();
        for(int loop=0;loop<line.length;loop++)
        {
            int local=this.linklocal2arraylocal(line[loop]);
            this.elements[local].auto_changed_indexlocal(change_direction.del);//将队列中被删除元素后方的元素位置减一
            try {
                this.elements[local].getpreviousNode().setNextNode(this.elements[local].getNextNode());
            } catch (Exception e) {}//节点位于队列头会捕捉到空指针异常
            try {
                this.elements[local].getNextNode().setpreviousNode(this.elements[local].getpreviousNode());
            } catch (Exception e) {}//节点位于队列尾会捕捉到空指针异常
            this.empty_array_local.add(local);
            Return.addLast(this.elements[local]);
        }
        return Return;
    }

    @Override
    public LinkedList<Vertical_Node> update(E element, Integer... line) throws insertException {
        LinkedList<Vertical_Node> Return=new LinkedList<>();
        E oldelement;
        int loop;
        try{
            if(singal.get(element)!=null||line.length!=1)
                throw new Exception();
            for(loop=0;loop<line.length;loop++)
            {
                singal.put(element,line[loop]);
                Return.add(this.elements[line[loop]].CopyNode());
                oldelement=this.elements[line[loop]].getelement();
                this.elements[line[loop]].updateelement(element);
                singal.remove(oldelement);
            }
        }
        catch(Exception e)
        {
            throw new insertException("违反了唯一约束");
        }
        return Return;
    }

    @Override
    public LinkedList<Integer> Pick_Condition(String conditionSymbol, E conditionValue) throws Exception {
        LinkedList<Integer> Return=new LinkedList<>();
        Iterator<E> temp=this.singal.keySet().iterator();
        Vertical_Node<E> tnode=new Vertical_Node(conditionValue,null,null);
        switch(conditionSymbol)
        {
            case "is":case "=":
            {
                int linklocal=elements[singal.get(conditionValue)].get_indexlabel();
                Return.add(linklocal);
                break;
            }
            case "isnot":case "!=":
            {
                while(temp.hasNext())
                {
                    E telement=temp.next();
                    if(tnode.compare(telement)!=0)
                        continue;
                    int linklocal=elements[singal.get(telement)].get_indexlabel();
                    Return.add(linklocal);
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
                    int linklocal=elements[singal.get(telement)].get_indexlabel();
                    Return.add(linklocal);
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
                    int linklocal=elements[singal.get(telement)].get_indexlabel();
                    Return.add(linklocal);
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
                    int linklocal=elements[singal.get(telement)].get_indexlabel();
                    Return.add(linklocal);
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
                    int linklocal=elements[singal.get(telement)].get_indexlabel();
                    Return.add(linklocal);
                }
                break;
            }
        }
        if(Return==null)
            Return=new LinkedList<>();
        return Return;
    }

    @Override
    public Vertical_Column<E> checkout(Integer... p_c) {
        Object[] o=new Object[p_c.length];
        Vertical_Array<E> Return;
        try {
            Return=new Vertical_Array<>(getindex_elements(p_c),Vertical_name,Vertical_type,false);
        } catch (insertException ex) {
            Return=new Vertical_Array<>(Vertical_name,this.Vertical_type);
        }
        return Return;
    }

    @Override
    public void initIndex() {}
    @Override
    public void dropIndex() {}

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    private int linklocal2arraylocal(int linklocal)
    {
        Vertical_Node n=head;
        for(int loop=0;loop<linklocal;loop++)
        {
            n=n.getNextNode();
        }
        return this.singal.get(n.getelement());
    }
}
