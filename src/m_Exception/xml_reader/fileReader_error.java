package m_Exception.xml_reader;

/**
 *
 * @author rkppo
 */
public class fileReader_error extends Exception
{
    public fileReader_error(String message)
    {
        super(message);
    }

    public fileReader_error() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
