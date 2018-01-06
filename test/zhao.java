//package speedtest;

public class zhao {
    String name;
    int age;
    int weight;

    public zhao() {
        name = "john";
        age = 19;
        weight = 45;
    }

    public zhao(String str, int num1, int num2) {
        name = str;
        age = num1;
        weight = num2;
    }

    public void isY() {
        if (age > 18 && age < 30) System.out.println(name + "是一个青年人");
        if (age > 30 && age < 45) System.out.println(name + "是一个中年人");
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        zhao p1 = new zhao();
        p1.isY();
        System.out.println("体重：" + p1.weight);
        zhao p2 = new zhao("Rose", 38, 75);
        p2.isY();
        System.out.println("体重：" + p2.weight);
    }
}

//    public static void main(String ar[]) throws InvocationTargetException, IllegalAccessException {
//        zhao z=new zhao();
//
//                long start = System.currentTimeMillis();
//                // 执行该方法
//                z.zhy();
//                long end = System.currentTimeMillis();
//                System.out.println(String.valueOf(end - start));
//
//
//    }


