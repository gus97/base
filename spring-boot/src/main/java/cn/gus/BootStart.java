package cn.gus;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = { "cn.gus.service" })

public class BootStart implements EmbeddedServletContainerCustomizer {

    protected final static Logger logger = LoggerFactory.getLogger(BootStart.class);

    public static void main(String[] args) {


        SpringApplication.run(BootStart.class, args);
        logger.info("启动成功 ...");
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {

        TomcatEmbeddedServletContainerFactory tomcatFactory = (TomcatEmbeddedServletContainerFactory) container;
        tomcatFactory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();

            protocol.setMaxConnections(2000);
            protocol.setMaxThreads(2000);
            protocol.setConnectionTimeout(30000);
        });
    }
}