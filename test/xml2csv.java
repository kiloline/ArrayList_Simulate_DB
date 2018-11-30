import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.util.List;


public class xml2csv {
    Element root;
    Document document;
    public xml2csv(File file) throws DocumentException {
        document=new SAXReader().read(file);
        root=document.getRootElement();
        document=null;
    }

    public Element getRoot()
    {
        return root;
    }
    public List<Element> getElements(String string,Element element)
    {
        return element.elements(string);
    }

    public List<Attribute> getAttributes(Element element){
        return element.attributes();
    }

    private Element select_ELE(Element e,String e_name)//根据e_name，找到含有e_name节点的父节点
    {
        List<Element> listElement=e.elements();//所有一级子节点的list
        for(Element temp:listElement)
        {//遍历所有一级子节点
            if(temp.getName().equals(e_name))
                return e;
            else
                select_ELE(temp,e_name);//递归
        }
        return null;
    }

    public static void main(String ar[]) throws DocumentException, IOException {
        String savedir="D:\\Work\\广西百色\\dataset";
        String file="D:\\Work\\广西百色\\dataset(to百分点).xml";
        File sdfile=new File(savedir);
        sdfile.mkdirs();
        File savecsv=new File(savedir+"\\ds.csv");
        BufferedOutputStream bos;
        BufferedWriter bw;
        savecsv.createNewFile();
        bos = new BufferedOutputStream(new FileOutputStream(savecsv, false), 524288);
        //暂定512K缓存，一般硬盘测试的中等数据块
        bw = new BufferedWriter(new OutputStreamWriter(bos), 524288);

        StringBuilder sb=new StringBuilder();
        xml2csv xc=new xml2csv(new File(file));
        List<Element> tables=xc.getElements("DataSet",xc.getRoot());

        for(Element temptable:tables){
            List<Attribute> tbabList=xc.getAttributes(temptable);
            StringBuilder attrname=new StringBuilder();
            StringBuilder attrvalue=new StringBuilder();
            for(Attribute tbattr:tbabList){
                attrname.append(tbattr.getName());
                attrvalue.append(tbattr.getValue());
                attrname.append(',');
                attrvalue.append(',');
            }
            sb.append(attrname.deleteCharAt(attrname.length()-1));
            sb.append('\n');
            sb.append(attrvalue.deleteCharAt(attrvalue.length()-1));
            sb.append('\n');

            List<Element> lists=xc.getElements("Field",temptable);
            attrname=new StringBuilder();
            List<Attribute> lablist=lists.get(0).attributes();
            for(Attribute lattr:lablist){
                attrname.append(lattr.getName());
                attrname.append(',');
            }
            sb.append(attrname.deleteCharAt(attrname.length()-1));
            sb.append('\n');

            for(Element templist:lists){
                lablist=templist.attributes();
                for(Attribute lattr:lablist){
                    sb.append(lattr.getValue());
                    sb.append(',');
                }
                sb.deleteCharAt(sb.length()-1);
                sb.append('\n');
            }

            bw.write(sb.toString());
            bw.newLine();
            sb=new StringBuilder();
        }
        bw.flush();
        bw.close();
    }
}
