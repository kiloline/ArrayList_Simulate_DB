package m_Exception;

import Data.Vessel.Word;

import java.util.LinkedList;

/**
 *
 * @author BFD-501
 */
public class bracket_matching_error extends Exception
{
    public bracket_matching_error(String word) {

        super(word);
    }
}
