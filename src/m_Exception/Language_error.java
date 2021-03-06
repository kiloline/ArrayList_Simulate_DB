/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m_Exception;

import Data.Vessel.Word;

/**
 *
 * @author rkppo
 */
public class Language_error extends Exception{
    /**
     * @param word
     * @param errorException
     */
    public Language_error(Word word,String errorException)
    {
        super("Error in line "+word.getLocal()[0]+" list "+word.getLocal()[1]+":"+errorException);
    }

    /**
     * @param line:行
     * @param list:列
     * @param errorException
     */
    public Language_error(int line,int list,String errorException)
    {
        super("Error in line "+line+" list "+list+":"+errorException);
    }

    public Language_error(String error)
    {
        super(error);
    }
}
