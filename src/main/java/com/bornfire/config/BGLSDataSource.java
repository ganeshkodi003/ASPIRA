package com.bornfire.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;

@Configuration
@EnableTransactionManagement
@ConfigurationProperties("datasrc")
@EnableJpaRepositories(basePackages = "com.bornfire.entities", entityManagerFactoryRef = "datasrc", transactionManagerRef = "datasrcTransactionManager")
public class BGLSDataSource {

	@NotNull
	private String username;
	@NotNull
	private String password;
	@NotNull
	private String url;

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	@Bean
    public LocalSessionFactoryBean datasrc() throws SQLException {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(srcdataSource());
        sessionFactory.setPackagesToScan("com.bornfire.entities");
        sessionFactory.setHibernateProperties(hibernateProperties());
 
        return sessionFactory;
    }
	
	private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql", "false");
        return properties;
    }

    @Bean
    public DataSource srcdataSource() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setUser(username);
        System.out.println(username);
        dataSource.setPassword(password);
        dataSource.setURL(url);
        return dataSource;
    }

	@Bean
	public PlatformTransactionManager datasrcTransactionManager() throws SQLException {

		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(datasrc().getObject());
		return transactionManager;
	}
	
}
