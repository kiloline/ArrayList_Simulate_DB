package Service.Language_ExecutePlan_builder;

import Data.Vessel.ExecutePlan_Package;
import Data.Vessel.Word;
import Service.Service;

import java.util.*;

import m_Exception.FileSystem.ClassNotFound;
import m_Exception.Language_error;

/**
 *
 * @author rkppo
 */
public class ExecutePlan_builder 
{
    private Service service;
    //LinkedList<Word> words;
    private String default_tbs="public";
    private ExecutePlan_Package EPP;
    ArrayList<String> clause_stop;
    public ExecutePlan_builder(Service backstage,ArrayList<String> clause_stop)
    {
        this.service=backstage;
        //this.words=words;
        this.clause_stop=clause_stop;
    }
    public ExecutePlan_Package make_ExecutePlan(LinkedList<Word> words) throws ClassNotFound, Language_error
    {
        LinkedList<LinkedList<Word>> Division=this.Pretreatment_Division_clause(words);
        Word_Classifier WC=new Word_Classifier(this,words);
        //EPP=WC.Classify();
        EPP=WC.Classify();
        return EPP;
    }

    public LinkedList<LinkedList<Word>> Pretreatment_Division_clause(LinkedList<Word> words)
    {
        LinkedList<Word> toDiv=(LinkedList<Word>) words.clone();
        return toFind_clause_start(toDiv);
    }
    private LinkedList<LinkedList<Word>> toFind_clause_start(LinkedList<Word> toDiv)
    {
        boolean in=false;
        LinkedList<LinkedList<Word>> Return=new LinkedList<>();
        LinkedList<Word> lw=new LinkedList<>();
        for(;!toDiv.isEmpty();)
        {
            //toDiv.get(0).getName();
            if(clause_stop.indexOf(toDiv.get(0).getName())!=-1)
            {
                in=!in;
            }
            if(in)
            {
                lw.add(toDiv.remove());
            }
            else
            {
                Return.add(lw);
                lw=new LinkedList<>();
            }
        }
        Return.add(lw);
        return Return;
    }
    
    public void CheckClasses(String Lttbs,String Lttb) throws ClassNotFound
    {//用于检查记录的表名和表空间名是否可用
     // 针对create的检查应当由自动机通过封闭路径的方式完成，这里就可以极大的简化逻辑
        if(Lttbs==null)
            Lttbs=default_tbs;
        if(service.check_File_exist(Lttbs, Lttb))
                ;
        else
            throw new ClassNotFound(Lttbs+'.'+Lttb);
    }
    
    public List<String> callTablecheckselectlist(String tbs,String tb,LinkedList<String> s_vertical)
    {//为了让分类器中能够进行动态的列名检查而设计的
        return service.call_FileSystem().getTable(tbs, tb).checkselectlist(s_vertical);
    }

    public ArrayList<String> getCstop()
    {
        return clause_stop;
    }
}
