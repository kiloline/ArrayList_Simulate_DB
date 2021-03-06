package Data.Vessel;

/**
 *
 * @author rkppo
 * 该类由分词器调用
 * 该类用于包装从分词器上分离的单词，把他们包装成一定的语法单位，
 * 比如说对象名（表名、列名、表空间名等等）\\、计算符号（+-*÷）\\、比较符号（=<>）\\、
 * 赋值符号（set: =）\\、分隔符号（括号、引号、,.）、\\
 * 类型单词（int、double、string）\\、对象单词（sequence、table……）\\、
 * 计算单词（and、or、in、is、not）、
 * 常量（数、字符串）\\、null\\……，这些定义写在分词器当中
 * 主要目的是从大量单词当中区分出对象名和常量，剩下的语素问题不大。
 */
public class Word 
{
    String type;//单词的语法分类
    String name;//单词的实际名称，如果是常量的话就写常量的类型名称
    String substance;//单词的实际内容，只对常量、对象名称有效
    int stayline;//单词所在的行
    int staynum;//单词所在行的第n个位置
    boolean isMark;//简单表示这是一个符号而不是单词
    public Word(String c_name,String c_substance)
    {
        this(c_name,c_substance,0,0,false);
    }

    /**
     * @param c_name
     * @param c_substance
     * @param line
     * @param list
     * @param ismark
     */
    public Word(String c_name,String c_substance,int line,int list,boolean ismark)
    {
        name=c_name;
        substance=c_substance;
        stayline=line;
        staynum=list;
        isMark=ismark;
    }

    public void setType(String type) { this.type=type; }
    public void setName(String name){ this.name=name; }
    public void setSubstance(String substance){ this.substance=substance; }
    public String getType() { return type; }
    public String getName() { return name; }
    public String getSubstance() { return this.substance; }
    public int[] getLocal()
    {
        int[] sr={stayline,staynum};
        return sr;
    }
    public boolean isMark()
    {
        return this.isMark;
    }

    @Override
    public String toString()
    {
        if(substance!=null)
            return substance;
        else
            return name;
    }
}
