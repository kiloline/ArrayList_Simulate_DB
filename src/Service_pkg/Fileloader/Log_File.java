/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Service_pkg.Fileloader;

import java.io.File;

/**
 *
 * @author rkppo
 */
public class Log_File extends DB_File_Abstract<Control_File,Object>
{

    public Log_File(Control_File father,String path,String filename,boolean detectHash)
    {
        super(father,path,filename,detectHash);
        fullpath=path+"\\"+filename+".dbl.xml";
    }
    public Log_File(Control_File father,File file,String filename,boolean detectHash)
    {
        super(father,file,filename,detectHash);
        fullpath=father.getPath()+"\\"+filename+".dbl.xml";
    }
    @Override
    /**@deprecated */
    public void addSon(Object add) {}

    @Override
    /**@deprecated */
    public void delSon(String del) { }
    
    @Override
    /**@deprecated */
    public void setSons(Object[] add) {}
}
