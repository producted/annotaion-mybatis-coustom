package com.zhangpk.generic;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By zhangpk On 2019/4/14
 **/
//关于获取泛型的练习及详解 -这里不做健壮性操作，只是注解学习
public class GenericTest {

    private Map<String, Integer> map = new HashMap<String, Integer>();

    //获取类成员变量类型的泛型参数
    public static Type getClassMemberVariable() throws NoSuchFieldException {
        //反射中的方法有 getDeclaredField getDeclaredFields(数组)
        //getField getFields 只返回public修饰的成员变量
        Field field = GenericTest.class.getDeclaredField("map");
        //我们并不能对类的私有字段进行操作，利用反射也不例外，但有的时候，例如要序列化的时候，我们又必须有能力去处理这些字段，这时候，我们就需要调用AccessibleObject上的setAccessible()方法来允许这种访问，而由于反射类中的Field，Method和Constructor继承自AccessibleObject，因此，通过在这些类上调用setAccessible()方法，我们可以实现对这些字段的操作。
        field.setAccessible(true);
        //获取泛型类型
        Type genericType = field.getGenericType();
        //这里需要了解ParameterizedType是什么
        //Type 的四个直接子接口 ParameterizedType，GenericArrayType，TypeVariable和WildcardType四种类型的接口
        //ParameterizedType:表示一种参数化的类型，比如Collection
        //GenericArrayType:表示一种元素类型是参数化类型或者类型变量的数组类型
        //TypeVariable: 是各种类型变量的公共父接口
        //WildcardType: 代表一种通配符类型表达式，比如?, ? extends Number, ? super Integer【wildcard是一个单词：就是“通配符”】

        //ParameterizedType中取得到了所有的泛型类型。ParameterizedType也就是参数化的type
        if (genericType instanceof ParameterizedType) {//当然这里是拿接口为对象来比较，你也可以用实现类ParameterizedTypeImpl

            //我们要获得的泛型类型相当于type的参数，有几个泛型类型，则getActualTypeArguments()会返回几个Type参数。
            //当然 如果有嵌套的泛型，当取到actualTypeArgument之后再使用一次getActualTypeArguments方法就可以获取到，有多少泛型都以此类推
            //那么这里是map，两个泛型，0就是string 1就是Integer
            Type actualTypeArgument = ((ParameterizedType) (genericType)).getActualTypeArguments()[1];
            return actualTypeArgument;
        }
        return null;
    }

    //类成员方法返回值的泛型参数。
    public static Type getClassMethodReturnType() throws NoSuchMethodException {
        Method method = GenericTest.class.getDeclaredMethod("test", List.class);
        Class<?> returnType = method.getReturnType();
        //class1.isAssignableFrom(class2) 判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口。如果是则返回 true；否则返回 false。如果该 Class 表示一个基本类型，且指定的 Class 参数正是该 Class 对象，则该方法返回 true；否则返回 false。
        if (List.class.isAssignableFrom(returnType)) {
            Type genericReturnType = method.getGenericReturnType();
            if (genericReturnType instanceof ParameterizedTypeImpl) {
                //这里需要提一嘴的是 Type可以强转为class的，具体可见我DaoOpretorFactory类中的针对查询返回List时的泛型转换
                return ((ParameterizedType) (genericReturnType)).getActualTypeArguments()[0];
            }
        }
        //或者也可以这样写 当然这样写是相当相当的再已知条件下直接操作
        /*Method method1 = GenericTest.class.getDeclaredMethod("test", List.class);
        Type genericReturnType = method1.getGenericReturnType();
        {...}*/
        return null;
    }

    //类成员方法参数类型的泛型参数。
    public static Type getClassMethodParameterType() throws NoSuchMethodException {
        Method method = GenericTest.class.getDeclaredMethod("test",List.class);
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (List.class.isAssignableFrom(parameterType)) {
                Type[] genericParameterTypes = method.getGenericParameterTypes();
                for (int j = 0; j < genericParameterTypes.length; j++) {
                    Type genericParameterType = genericParameterTypes[i];
                    if (genericParameterType instanceof ParameterizedType) {
                        return ((ParameterizedType)(genericParameterType)).getActualTypeArguments()[0];
                    }
                }
            }
        }
        //当然 你也可以直接使用method.getGenericParameterTypes(); 同上理
        return null;
    }

    //类构造函数的参数类型的泛型参数。
    public static Type getClassGenerateType() throws NoSuchMethodException {
        //和获取方法一样  只不过换了个方法来获取有参构造
        Constructor<GenericTest> constructor = GenericTest.class.getConstructor(List.class);
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            if (List.class.isAssignableFrom(parameterType)) {
                Type[] genericParameterTypes = constructor.getGenericParameterTypes();
                for (int j = 0; j < genericParameterTypes.length; j++) {
                    Type genericParameterType = genericParameterTypes[i];
                    if (genericParameterType instanceof ParameterizedType) {
                        return ((ParameterizedType)(genericParameterType)).getActualTypeArguments()[0];
                    }
                }
            }
        }
        return null;
    }



    public GenericTest(List<Double> list){

    }

    public List<Integer> test(List<String> t){
        return new ArrayList<Integer>();
    }
    public static void main(String[] args) throws NoSuchFieldException, NoSuchMethodException {
        System.out.println(getClassMemberVariable());
        System.out.println(getClassMethodReturnType());
        System.out.println(getClassMethodParameterType());
        System.out.println(getClassGenerateType());
    }
}

//通过extends在子类指定父类中泛型变量具体类型的，可获取父类泛型的具体类型
class Father<T>{
    public Father(){
        Type genericSuperclass = getClass().getGenericSuperclass();
        Type actualTypeArgument = ((ParameterizedType) (genericSuperclass)).getActualTypeArguments()[0];
    }
}
class Children extends Father<Children>{}
