# 01 简单实现Bean容器 & BeanDefinition和BeanDefinitionRegistry

主要类：

- BeanDefinition，顾名思义，用于定义bean信息的类，包含bean的class类型、构造参数、属性值等信息，每个bean对应一个BeanDefinition的实例。简化BeanDefinition仅包含bean的class类型。
- BeanDefinitionRegistry，BeanDefinition注册表接口，定义注册BeanDefinition的方法。
- SingletonBeanRegistry及其实现类DefaultSingletonBeanRegistry，定义添加和获取单例bean的方法。

bean容器作为BeanDefinitionRegistry和SingletonBeanRegistry的实现类，具备两者的能力。向bean容器中注册BeanDefinition后，使用bean时才会实例化。

类图如下:
![DefaultListableBeanFactory类图.png](img%2FDefaultListableBeanFactory%E7%B1%BB%E5%9B%BE.png)

测试方法见：
```java

public class BeanDefinitionAndBeanDefinitionRegistryTest {

	@Test
	public void testBeanFactory() throws Exception {
		//测试简单的容器DefaultListableBeanFactory 实现
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		BeanDefinition beanDefinition = new BeanDefinition(HelloService.class);
		beanFactory.registerBeanDefinition("helloService", beanDefinition);

		HelloService helloService = (HelloService) beanFactory.getBean("helloService");
		helloService.sayHello();
	}
}
```
---- 
# 02 Bean实例化策略InstantiationStrategy

现在bean是在AbstractAutowireCapableBeanFactory.doCreateBean方法中用beanClass.newInstance()来实例化，仅适用于bean有无参构造函数的情况。

类图如下:
![InstantiationStrategy实现类图.png](img%2FInstantiationStrategy%E5%AE%9E%E7%8E%B0%E7%B1%BB%E5%9B%BE.png)

针对bean的实例化，抽象出一个实例化策略的接口InstantiationStrategy，有两个实现类：

- SimpleInstantiationStrategy，使用bean的构造函数来实例化
- CglibSubclassingInstantiationStrategy，使用CGLIB动态生成子类
--- 

# 03 为bean填充属性

在BeanDefinition中增加和bean属性对应的PropertyValues，实例化bean之后，为bean填充属性(AbstractAutowireCapableBeanFactory#applyPropertyValues)。

```java

public class PopulateBeanWithPropertyValuesTest {

    @Test
    public void testPopulateBeanWithPropertyValues() throws Exception{
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name","smile"));
        propertyValues.addPropertyValue(new PropertyValue("age",27));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class,propertyValues);
        beanFactory.registerBeanDefinition("person",beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("smile");
        assertThat(person.getAge()).isEqualTo(27);
    }

}

```
---- 
# 04 为bean注入bean
增加BeanReference类，包装一个bean对另一个bean的引用。实例化beanA后填充属性时，若PropertyValue#value为BeanReference，引用beanB，则先去实例化beanB。 由于不想增加代码的复杂度提高理解难度，暂时不支持循环依赖。

```java

public class PopulateBeanWithPropertyValuesTest {

    @Test
    public void testPopulateBeanWithPropertyValues() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "smile"));
        propertyValues.addPropertyValue(new PropertyValue("age", 27));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValues);
        beanFactory.registerBeanDefinition("person", beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("smile");
        assertThat(person.getAge()).isEqualTo(27);
    }


    @Test
    public void testPopulateBeanWithBean() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //注册car实例
        PropertyValues carPropertyValues = new PropertyValues();
        carPropertyValues.addPropertyValue(new PropertyValue("brand", "奔驰"));
        BeanDefinition carBeanDefinition = new BeanDefinition(Car.class, carPropertyValues);
        beanFactory.registerBeanDefinition("car", carBeanDefinition);

        //注册person实例
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name", "smile"));
        propertyValues.addPropertyValue(new PropertyValue("age", 27));
        propertyValues.addPropertyValue(new PropertyValue("car", new BeanReference("car")));
        BeanDefinition beanDefinition = new BeanDefinition(Person.class, propertyValues);
        beanFactory.registerBeanDefinition("person", beanDefinition);

        Person person = (Person) beanFactory.getBean("person");
        System.out.println(person);
        assertThat(person.getName()).isEqualTo("smile");
        assertThat(person.getAge()).isEqualTo(27);
        Car car = person.getCar();
        assertThat(car).isNotNull();
        assertThat(car.getBrand()).isEqualTo("奔驰");
    }

}

```
--- 

# 05 资源和资源加载器
Resource是资源的抽象和访问接口，简单写了三个实现类

- FileSystemResource，文件系统资源的实现类
- ClassPathResource，classpath下资源的实现类
- UrlResource，对java.net.URL进行资源定位的实现类

Resource类图如下:
![Resource资源加载器类图.png](img%2FResource%E8%B5%84%E6%BA%90%E5%8A%A0%E8%BD%BD%E5%99%A8%E7%B1%BB%E5%9B%BE.png)

ResourceLoader接口则是资源查找定位策略的抽象，DefaultResourceLoader是其默认实现类

测试代码如下
```java

public class ResourceAndResourceLoaderTest {


    @Test
    public void testResourceLoader() throws Exception{
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();

        //加载classpath下的资源
        Resource resource = resourceLoader.getResource("classpath:hello.txt");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        assertThat(content).isEqualTo("hello world");

        //加载文件系统资源
        resource = resourceLoader.getResource("src/test/resources/hello.txt");
        assertThat(resource instanceof FileSystemResource).isTrue();
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
        assertThat(content).isEqualTo("hello world");

        //加载url资源
        resource = resourceLoader.getResource("https://www.baidu.com");
        assertThat(resource instanceof UrlResource).isTrue();
        inputStream = resource.getInputStream();
        content = IoUtil.readUtf8(inputStream);
        System.out.println(content);

    }

}
```
--- 
# 06 在xml文件中定义bean
有了资源加载器，就可以在xml格式配置文件中声明式地定义bean的信息，资源加载器读取xml文件，解析出bean的信息，然后往容器中注册BeanDefinition。

BeanDefinitionReader是读取bean定义信息的抽象接口，XmlBeanDefinitionReader是从xml文件中读取的实现类。BeanDefinitionReader需要有获取资源的能力，且读取bean定义信息后需要往容器中注册BeanDefinition，因此BeanDefinitionReader的抽象实现类AbstractBeanDefinitionReader拥有ResourceLoader和BeanDefinitionRegistry两个属性。

由于从xml文件中读取的内容是String类型，所以属性仅支持String类型和引用其他Bean。后面会讲到类型转换器，实现类型转换。

为了方便后面的讲解和功能实现，并且尽量保持和spring中BeanFactory的继承层次一致，对BeanFactory的继承层次稍微做了调整。


调整后的`DefaultListableBeanFactory` 类结构图如下

![DefaultListableBeanFactory模仿Spring类图.png](img%2FDefaultListableBeanFactory%E6%A8%A1%E4%BB%BFSpring%E7%B1%BB%E5%9B%BE.png)


--- 
# 07 BeanFactoryPostProcessor和BeanPostProcessor

- **BeanFactoryPostProcessor**

> `BeanFactoryPostProcessor`是spring提供的容器扩展机制，允许我们在bean实例化之前修改bean的定义信息即`BeanDefinition`的信息。其重要的实现类有`PropertyPlaceholderConfigurer`和`CustomEditorConfigurer`，`PropertyPlaceholderConfigurer`的作用是用properties文件的配置值替换xml文件中的占位符，`CustomEditorConfigurer`的作用是实现类型转换。`BeanFactoryPostProcessor`的实现比较简单，看单元测试`BeanFactoryPostProcessorAndBeanPostProcessorTest#testBeanFactoryPostProcessor`追下代码。

>其中主要是将`BeanFactory` 传入到自定义的`BeanFactoryPostProcessor`中 直接执行相对应的`postProcessBeanFactory`方法


<br/>

- **BeanPostProcessor**

> `BeanPostProcessor`也是spring提供的容器扩展机制，不同于`BeanFactoryPostProcessor`的是，`BeanPostProcessor`在bean实例化后修改bean或替换bean。`BeanPostProcessor`是后面实现AOP的关键。

> 其中主要在 `AbstractBeanFactory`中加入了一个`List<BeanPostProcessor> beanPostProcessors`，
然后在`addBeanPostProcessor`方法中加入需要的BeanPostProcessor <br/>

> 在`AbstractAutowireCapableBeanFactory.initializeBean`方法中去调用执行所有BeanPostProcessor的前置后置方法


----

# 08 应用上下文 ApplicationContext

应用上下文ApplicationContext是spring中较之于BeanFactory更为先进的IOC容器，ApplicationContext除了拥有BeanFactory的所有功能外，还支持特殊类型bean如上一节中的BeanFactoryPostProcessor和BeanPostProcessor的自动识别、资源加载、容器事件和监听器、国际化支持、单例bean自动初始化等。

BeanFactory是spring的基础设施，面向spring本身；而ApplicationContext面向spring的使用者，应用场合使用ApplicationContext。

具体实现查看AbstractApplicationContext#refresh方法即可。注意BeanFactoryPostProcessor和BeanPostProcessor的自动识别，这样就可以在xml文件中配置二者而不需要像上一节一样手动添加到容器中了。

- ### **Bean**的生命周期图如下

![Bean的生命周期.png](img%2FBean%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.png)

- ### `ClassPathXmlApplicationContext`的类结构图如下:

![CalssPathXmlApplicationContext类图.png](img%2FCalssPathXmlApplicationContext%E7%B1%BB%E5%9B%BE.png)

- 测试代码如下:
```java


public class ApplicationContextTest {

    @Test
    public void testApplicationContext() throws BeansException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");

        Person person = applicationContext.getBean("person", Person.class);
        System.out.println(person);
        //在CustomBeanFactoryPostProcessor改成了jane
        assertThat(person.getName()).isEqualTo("jane");

        Car car = applicationContext.getBean("car", Car.class);
        System.out.println(car);
        //在CustomBeanPostProcessor改成了兰博基尼
        assertThat(car.getBrand()).isEqualTo("兰博基尼");

    }

}

```

----

# 09 bean的初始化和销毁方法

> 代码分支 09-init-and-destroy-method

在spring中，定义bean的初始化和销毁方法有三种方法：

- 在xml文件中制定`init-method`和`destroy-method `
- 继承自`InitializingBean`和`DisposableBean`
- 在方法上加注解`PostConstruct`和`PreDestroy`

第三种通过`BeanPostProcessor`实现，在扩展篇中实现，本节只实现前两种。

针对第一种在xml文件中指定初始化和销毁方法的方式，在`BeanDefinition`中增加属性`initMethodName`和`destroyMethodName`。

初始化方法在`AbstractAutowireCapableBeanFactory#invokeInitMethods`执行。`DefaultSingletonBeanRegistry`中增加属性`disposableBeans`保存拥有销毁方法的bean，拥有销毁方法的bean在`AbstractAutowireCapableBeanFactory#registerDisposableBeanIfNecessary`中注册到`disposableBeans`中。

为了确保销毁方法在虚拟机关闭之前执行，向虚拟机中注册一个钩子方法，查看`AbstractApplicationContext#registerShutdownHook`（非web应用需要手动调用该方法）。当然也可以手动调用`ApplicationContext#close`方法关闭容器。

到此为止，bean的生命周期如下：
![初始化和销毁的Bean的生命周期.png](img%2F%E5%88%9D%E5%A7%8B%E5%8C%96%E5%92%8C%E9%94%80%E6%AF%81%E7%9A%84Bean%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.png)

- 测试代码如下:
```java

public class InitAndDestroyMethodTest {

    @Test
    public void testInitAndDestroyMethod(){
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:init-and-destroy-method.xml");
        applicationContext.registerShutdownHook();  //或者手动关闭 applicationContext.close();
    }
}

```


-----
# 10  Aware接口

> 代码分支 10-aware-interface

**重构了`XmlBeanDefinitionReader#doLoadBeanDefinitions(InputStream inputStream)` 使用dom4j 去解析xml**

Aware是感知、意识的意思

Aware接口是标记性接口，其实现子类能感知容器相关的对象。

常用的Aware接口有`BeanFactoryAware`和`ApplicationContextAware`，分别能让其实现者感知所属的BeanFactory和ApplicationContext。

让实现`BeanFactoryAware`接口的类能感知所属的`BeanFactory`

实现比较简单，查看`AbstractAutowireCapableBeanFactory#initializeBean`前三行。
```java
 if (bean instanceof BeanFactoryAware){
    ((BeanFactoryAware) bean).setBeanFactory(this);
 }
```


实现`ApplicationContextAware`的接口感知`ApplicationContext`， 是通过`BeanPostProcessor`。
由bean的生命周期可知，bean实例化后会经过`BeanPostProcessor`的前置处理和后置处理。
- 1 定义一个`BeanPostProcessor`的实现类`ApplicationContextAwareProcessor`
- 2 在`AbstractApplicationContext#refresh`方法中加入到BeanFactory中
```java
//添加ApplicationContextAwareProcessor 让继承ApplicationContextAware的bean能感知ApplicationContext应用上下文
beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
```
- 3 在前置处理中为bean设置所属的`ApplicationContext`。
```java
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
        return bean;
    }
```

至止，bean的生命周期如下：
![加入Aware接口相关的Bean的生命周期.png](img%2F%E5%8A%A0%E5%85%A5Aware%E6%8E%A5%E5%8F%A3%E7%9B%B8%E5%85%B3%E7%9A%84Bean%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.png)

- 测试代码如下:
```java

public class AwareInterfaceTest {

    @Test
    public void test() throws Exception{
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        HelloService helloService = applicationContext.getBean("helloService", HelloService.class);
        assertThat(helloService.getApplicationContext()).isNotNull();
        assertThat(helloService.getBeanFactory()).isNotNull();
    }

}

```


----

# 11  Prototype作用域

> 代码分支 11-prototype-bean
>

每次向容器获取prototype作用域bean时，容器都会创建一个新的实例。在`BeanDefinition`中增加描述bean的作用域的字段`scope/singleton/prototype`，创建prototype作用域bean时（`AbstractAutowireCapableBeanFactory#doCreateBean`），不往`singletonObjects`中增加该bean。prototype作用域bean不执行销毁方法，查看`AbstractAutowireCapableBeanFactory#registerDisposableBeanIfNecessary`方法。


主要是三步
- 1 在解析xml文件的是塞进去一个`scope`变量
```java
    public static final String SCOPE_ATTRIBUTE = "scope";

    private static String SCOPE_SINGLETON = "singleton";
    
    private static String SCOPE_PROTOTYPE = "prototype";

    String beanScope = bean.attributeValue(SCOPE_ATTRIBUTE);

    //加入prototype作用域的处理逻辑
    if (StrUtil.isNotEmpty(beanScope)){
        beanDefinition.setScope(beanScope);
    }

    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    public boolean isSingleton() {
        return singleton;
    }
```

- 2 创建prototype作用域bean时（`AbstractAutowireCapableBeanFactory#doCreateBean`），不往`singletonObjects`中增加该bean
```java
//加入prototype作用域的判断
if (beanDefinition.isSingleton()){
    addSingleton(beanName, bean);
}

```

- 3 prototype作用域bean不执行销毁方法，查看`AbstractAutowireCapableBeanFactory#registerDisposableBeanIfNecessary`方法
```java
 //只有singleton类型bean会执行销毁方法
if (beanDefinition.isSingleton()) {
    if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
        registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition));
    }
}

```


至止，bean的生命周期如下：

![prototype-bean.png](img%2Fprototype-bean.png)
