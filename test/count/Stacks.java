/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package count;

import java.util.LinkedList;

/**
 *
 * @author rkppo
 */
public class Stacks {
    private LinkedList list=new LinkedList();     
   int top=-1;     
   public void push(Object value){     
      top++;     
      list.addFirst(value);     
   }     
   public Object pop(){     
      Object temp=list.getFirst();     
      top--;     
      list.removeFirst();     
      return temp;     
   
   }     
   public Object top(){     
   return list.getFirst();     
   }     
}
