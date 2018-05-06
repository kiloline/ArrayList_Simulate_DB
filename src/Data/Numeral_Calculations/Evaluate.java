package Data.Numeral_Calculations;

import Data.Verticaltype.Vertical_Node;
import Data.Vessel.Word;
import Utils.Math.DoubleNumber;
import Utils.Math.MathFunction_Calculation;
import Utils.Math.Simple_Calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
public class Evaluate {
    Map.Entry e;
    Number n;
    Stack<Number> ops=new Stack<>();
    Stack<String> mark=new Stack<>();
    StringBuilder evaString=new StringBuilder();

    public void pushElement(Word word)
    {
        switch(word.getType())
        {
            case "Var": {
                switch(word.getName())
                {
                    case "Double":
                        ops.push(Double.parseDouble(word.getSubstance()));
                        break;
                    case "Integer":
                        ops.push(Integer.parseInt(word.getSubstance()));
                        break;
                    case "String"://目前不对String直接转换 更正：String试图转换成双精度浮点型
                        evaString.append(word.getSubstance());
                        break;
                }
            }
            case "("://最高优先级，所以不进行优先级判断直接入栈
                mark.push(word.getName());
                break;
            case ")":
                Q_step_calculation();
                break;
            case "calculation_Mark_Link":
            case "MathFunction":
                if(!mark.empty()) {
                    while (getPriority(this.mark.peek(), word.getName())) {
                        Single_step_calculation();
                    }
                }
                mark.push(word.getName());
                break;

        }
    }
    public void pushElement(Vertical_Node node)//按照数据表中列的类型压入数据，不接受外来类型
    {
        switch(node.getTrueType())
        {
            case "java.lang.Double":
                ops.push((Double)node.getelement());
                break;
            case "java.lang.Integer":
                ops.push((Integer)node.getelement());
                break;
            case "java.lang.String":
                evaString.append((String)node.getelement());
                break;
        }
    }
    private void Single_step_calculation()
    {
        Number rightnum,leftnum,result;
        String mark;
        rightnum=ops.pop();
        //leftnum=ops.pop();
        mark=this.mark.pop();

        switch(mark)
        {
            case "+":
//                result=Math.addExact(leftnum,rightnum);//leftnum,rightnum;
                leftnum=ops.pop();
                result=Simple_Calculation.add(leftnum,rightnum);
                break;
            case "-":
                leftnum=ops.pop();
                result=Simple_Calculation.minus(leftnum,rightnum);
                break;
            case "*":
                leftnum=ops.pop();
                result=Simple_Calculation.multi(leftnum,rightnum);
                break;
            case "/":
                leftnum=ops.pop();
                result=Simple_Calculation.div(leftnum,rightnum);
                break;
            case "%":
                leftnum=ops.pop();
                result=Simple_Calculation.moer(leftnum,rightnum);
                break;
            case "^":
                leftnum=ops.pop();
                result=Simple_Calculation.mi(leftnum,rightnum);
                break;
            case "sqrt":
                result= MathFunction_Calculation.sqrt(rightnum);
                break;
            default:
                result=rightnum;
        }

        ops.push(result);
    }
    private void Q_step_calculation()//用于计算含多个算符的括号
    {
        while(!mark.peek().equals("("))
            Single_step_calculation();
        mark.pop();//最后弹出左括号
    }

    public Object getthisEvaluate() throws Exception {
        while(!mark.empty()){
            Single_step_calculation();
        }

        if(evaString.length()!=0)
            return evaString.toString();

        if(ops.size()!=1)
            throw new Exception();
        else
            return ops.pop();
    }

    public String getRPN_toString()//最初计划输出语法树
    {
        StringBuilder sb=new StringBuilder();
        Stack<String> s=new Stack<>();
        for(int l=0;l<ops.size();l++)
        {
            s.push(ops.peek().toString());
        }
        for(int l=0;l<s.size();l++)
        {
            sb.append(s.pop());
        }
        s= (Stack<String>) mark.clone();
        for(int l=0;l<s.size();l++)
        {
            sb.append(s.pop());
        }
        return sb.toString();
    }

    private boolean getPriority(String mark_instack,String mark_toadd) {//获取符号优先级
        //如果待加入的算符优先级更高返回false，否则返回true.
        //意为，新算符优先级高则不进行单步计算。
        int is=getPriority(mark_instack),ta=getPriority(mark_toadd);
        if(is==6)
            return false;
        else
            return ta<is;
    }
    private int getPriority(String mark)
    {
        switch(mark)
        {
            case "(":
                return 6;
            case "+": case "-":
                return 1;
            case "*": case "/":
                return 2;
            case "^":
                return 3;
            case "%":
                return 4;
            default:
                return 5;
        }
    }
}