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



