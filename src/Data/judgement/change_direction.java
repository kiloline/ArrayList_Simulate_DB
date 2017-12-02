/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data.judgement;

/**
 *
 * @author rkppo
 */
public enum change_direction {
    add(1),
    del(-1);
        change_direction(int i)
        {
            this.i=i;
        }
        int i;
        public int geti()
        {
            return i;
        }
}
