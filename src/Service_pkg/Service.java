package Service_pkg;

import Service_pkg.Language_disposer.*;
import Utils.Check.check_StringtoNumber;
import Data.Vessel.ExecutePlan_Package;
import Service_pkg.Fileloader.FileSystem_link_tree;
import Service_pkg.Handling.table_handling;
import Service_pkg.Language_ExecutePlan_builder.ExecutePlan_builder;
import Data.Vessel.Word;
import Windows.Personal_Window;

import java.util.*;

import m_Exception.FileSystem.ClassNotFound;
import m_Exception.Language_error;
import m_Exception.xml_reader.fileReader_error;
import org.dom4j.DocumentException;

public class Service implements Callback
{
    private String rootPath,dbname;
    private table_handling th;
    private InitLanguage_node allNodes;
    private InitGrammer_unit IGu;
    private FileSystem_link_tree FLG;
    private Personal_Window PW;
    private check_StringtoNumber csn;
    private LanguageNode_shifter LNS;
    private Word_Segmentation_Machine WSS;
    private Word_Grammar_unit WGu;
    private ExecutePlan_builder EPB;
    private ExecutePlan_Package EPP;
//    IMonitorService System_info;
    private ArrayList<String> clause_stop;

    private int listsize;
    public Service(String rootpath,String db_name)
    {
        rootPath=rootpath;
        dbname=db_name;
//        System_info=new MonitorService();//初始化系统信息类
        csn =new check_StringtoNumber();
        allNodes=new InitLanguage_node();//初始化单词节点
        IGu=new InitGrammer_unit();
        clause_stop=allNodes.setclause_stop();//初始化子句起始
        EPB=new ExecutePlan_builder(this,clause_stop);
        th = new table_handling(this,csn);//初始化执行引擎，当然现在还没有上升到执行引擎的层次
        WSS=new Word_Segmentation_Machine(allNodes.setWord_Set());//分词器，需要单词列表
        WGu=new Word_Grammar_unit(allNodes.setWord_Set(),InitGrammer_unit.getGrammar_unit());
        LNS=new LanguageNode_shifter(IGu.setWord_Type_Map());//自动机，需要转移地图
        
        Boolean detectHash;
        listsize=Integer.valueOf(env_properties.getEnvironment("listlength"));
        detectHash=Boolean.valueOf(env_properties.getEnvironment("detectHash"));
        th.setlistsize(listsize);
        
        try {//加载数据文件
            FLG=new FileSystem_link_tree(rootPath,dbname,detectHash,th);//此处有问题，尚待改进//已修改
        } catch (DocumentException ex) {
            System.out.println(ex.getMessage());
            return;
        } catch (fileReader_error ex) {
            System.out.println(ex.getMessage());
            return;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        
        PW=new Personal_Window(this);//用户界面放在最后进行初始化
    }
    
    public void running()
    {
        for(;;)
            PW.inputStream();
    }
    
    public void Language_dispose(String SQL) throws ClassNotFound, Exception//由用户界面回调
    {
        LinkedList<Word> word_list;
        try {
            word_list=WSS.Segment(SQL);
            word_list=WGu.set_Word_Grammar_unit(word_list);
            LNS.shifter(word_list);
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }
        create_ExecutePlan(word_list);
    }
    
    public void create_ExecutePlan(LinkedList<Word> word_list) throws ClassNotFound, Language_error, Exception
    {
        EPP=EPB.make_ExecutePlan(word_list);
        Implement_ExecPlan(EPP);
    }
    public boolean check_File_exist(String tablespace,String table) throws ClassNotFound//执行计划生成时检查表级以上对象
    {
        return FLG.checkFile(tablespace, table);
    }

    public void Implement_ExecPlan(ExecutePlan_Package EPP) throws Exception
    {
        th.Implement_Plan(EPP);
    }
    public FileSystem_link_tree call_FileSystem()
    {
        return FLG;
    }
}
