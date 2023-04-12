# Bean实例化策略InstantiationStrategy

现在bean是在AbstractAutowireCapableBeanFactory.doCreateBean方法中用beanClass.newInstance()来实例化，仅适用于bean有无参构造函数的情况。

类图如下:
![InstantiationStrategy实现类图.png](img%2FInstantiationStrategy%E5%AE%9E%E7%8E%B0%E7%B1%BB%E5%9B%BE.png)

针对bean的实例化，抽象出一个实例化策略的接口InstantiationStrategy，有两个实现类：

- SimpleInstantiationStrategy，使用bean的构造函数来实例化
- CglibSubclassingInstantiationStrategy，使用CGLIB动态生成子类