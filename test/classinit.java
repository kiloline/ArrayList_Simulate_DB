class Phone {
    // 声明父类的一个私有变量,手机类型，因为是父类，所以我们将其赋值为public
    private String type = "public";

    // 在无参构造里调用成员方法
    public Phone() {
        System.out.println(this.type);
        this.showType();
        System.out.println(this.getClass());
        System.out.println(this.getWeight());
        this.set("000");
    }

    Integer getWeight() {
        return 0;
    }

    //打印手机类型
    public void showType() {
        System.out.println(type);
    }

    public  void set(Object o)
    {

    }
}

class AndroidPhone extends Phone{
    //定义一个手机类型的实例变量，名字与父类一样，都为type
    private String type = "android";
    private Integer Weight=1000;
    //在构造方法里对type重新赋值
    public AndroidPhone(){
        type = "GoogleAndroid";
//      this.showType();  （2）
    }


    //复写父类的showType方法
    @Override
    public void showType() {
        System.out.println(type);
    }

    @Override
    Integer getWeight() {
        return Weight;
    }

    public void set(String o) {
        type=o;
    }
}

public class classinit {
    public static void main(String[] args) {
        // 创建AndroidPhone实例，结果会输出什么呢？
        new AndroidPhone();
    }
}
