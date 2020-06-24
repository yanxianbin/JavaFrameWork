package mode;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class ConfigurationMetaData {

    public static void main(String[] args) {
        BeanDefinitionBuilder builder=BeanDefinitionBuilder.genericBeanDefinition(UserInfo.class);
        builder.addPropertyValue("name","yanxianibn");
        BeanDefinition beanDefinition=builder.getBeanDefinition();
        beanDefinition.setAttribute("test","shenzhen");
        DefaultListableBeanFactory beanFactory=new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("userInfo",beanDefinition);
        UserInfo userInfo=beanFactory.getBean("userInfo",UserInfo.class);
        System.out.println(userInfo);
    }
}
