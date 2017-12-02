package Data.Verticaltype;

import Data.judgement.change_direction;
import m_Exception.type.Type_conversion;

import java.io.Serializable;

/**
 *
 * @author BFD-501 node有两种初始化方式，分别是链表方式和数组集合方式。 这两种方式对应了list集合和map/set集合，
 * 这样以后就可以用比较小的代价在node的基础上开发出linkmap或者linkset。
 * 但是即使是数组集合的方式，每个node里仍然可以记录下插入顺序，不至于完全缺乏参考。
 */
public class Vertical_Node<E> implements Comparable<Vertical_Node>,Serializable {

    private Integer index_label;//插入顺序
    private Integer nodehash;//当node被插入树结构中，该位置需要记录rehash之后的hash值
    private E element;
    private Vertical_Node<E> next;
    private Vertical_Node<E> previous;
    private Vertical_Node<E> leftSon;
    private Vertical_Node<E> rightSon;

    public <E> Integer compare(E conditionValue) {
        if (conditionValue.hashCode() > element.hashCode()) {
            return 1;
        } else if (conditionValue.equals(element)) {
            return 0;
        } else //not equals
        {
            return -1;
        }
    }

    public E getelement() {
        return this.element;
    }
    public String getTrueType() { return this.element.getClass().getTypeName(); }

    public void updateelement(E newelement) {
        this.element = newelement;
    }

    public void change_indexlocal(Integer newindex) {
        this.index_label = newindex;
    }

    public int get_indexlabel() {
        return this.index_label;
    }

    public Integer intValue() throws Type_conversion {
        switch(getTrueType())
        {
            case "java.lang.Double":return ((Double)element).intValue();
            case "java.lang.Integer":return (Integer)element;
            default:
                throw new Type_conversion(getTrueType().substring(getTrueType().lastIndexOf(".")),"Integer");
        }
    }
    public Double doubleValue() throws Type_conversion {
        switch(getTrueType())
        {
            case "java.lang.Double":return ((Double)element);
            case "java.lang.Integer":return ((Integer)element).doubleValue();
            default:
                throw new Type_conversion(getTrueType().substring(getTrueType().lastIndexOf(".")),"Double");
        }
    }
    public String stringValue() { return toString(); }

    @Override
    public int compareTo(Vertical_Node o) {
        if (o.getelement().hashCode() > this.getelement().hashCode()) {
            return 1;
        } else if (o.getelement().hashCode() == this.getelement().hashCode()) {
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public String toString() {
        return this.element.toString();
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    //-------------------like List-------------------//
    public Vertical_Node(Vertical_Node<E> previous) {
        this.next = next;
        this.previous = previous;
    }

    public Vertical_Node<E> getNextNode() {
        return this.next;
    }

    public Vertical_Node<E> getpreviousNode() {
        return this.previous;
    }

    public void setNextNode(Vertical_Node<E> node) {
        this.next = node;
    }

    public void setpreviousNode(Vertical_Node<E> node) {
        this.previous = node;
    }

    //addNode
    /**
     * 该方法是为了自动的在各种情况下方便的调整indexlabel顺序的函数，只要所有的
     * node由小到大串成一条链表，该方法可以由任何一个node处切入，将此后所有node 的index都会变化
     */
    public void auto_changed_indexlocal(change_direction dt) {
        index_label=index_label+dt.geti();
        try{
            next.auto_changed_indexlocal(dt);
        }
        catch(NullPointerException e){}
    }

    //-------------------like Set-------------------//
    public Vertical_Node(E element, Integer index, Integer nodehash) {
        this.element = element;
        if (index == null) {
            this.index_label = 31;
        } else {
            this.index_label = index;
        }
        this.nodehash = nodehash;
    }

    public Integer getNodehash() {
        return nodehash;
    }

    public Vertical_Node<E> getLeftSon() {
        return leftSon;
    }

    public void setLeftSon(Vertical_Node<E> leftSon) {
        this.leftSon = leftSon;
    }

    public Vertical_Node<E> getRightSon() {
        return rightSon;
    }

    public void setRightSon(Vertical_Node<E> rightSon) {
        this.rightSon = rightSon;
    }

    public boolean hasSon() {
        return !((rightSon == null) && (leftSon == null));
    }

    //-------------------for handling-------------------//
    private Vertical_Node(Integer index_label,
            Integer nodehash,
            E element,
            Vertical_Node<E> next,
            Vertical_Node<E> previous,
            Vertical_Node<E> leftSon,
            Vertical_Node<E> rightSon) {
        this.element = element;
        this.index_label = index_label;
        this.leftSon = leftSon;
        this.next = next;
        this.nodehash = nodehash;
        this.previous = previous;
        this.rightSon = rightSon;
    }

    public Vertical_Node CopyNode() {
        return new Vertical_Node(index_label, nodehash, element, next, previous, leftSon, rightSon);
    }
}
