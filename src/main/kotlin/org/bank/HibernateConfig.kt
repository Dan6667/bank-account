package org.bank

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@ComponentScan
@EnableJpaRepositories
open class HibernateConfig {

    @Autowired
    private lateinit var env: Environment

    @Bean
    open fun entityManagerFactory(dataSource: DataSource): EntityManagerFactory {
        val factory = LocalContainerEntityManagerFactoryBean()
        val properties = HashMap<String, Any?>()
        properties["hibernate.dialect"] = env.getProperty("hibernate.dialect")
        properties["hibernate.hbm2ddl.auto"] = env.getProperty("hibernate.hbm2ddl.auto")

        factory.jpaPropertyMap = properties
        factory.jpaVendorAdapter = HibernateJpaVendorAdapter()
        factory.setPackagesToScan("org.bank.entity")
        factory.dataSource = dataSource
        factory.afterPropertiesSet()
        return factory.getObject() ?: throw AssertionError("EntityManagerFactory is null")
    }

    @Bean
    open fun dataSource(): DataSource? {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"))
        dataSource.url = env.getProperty("jdbc.url")
        dataSource.username = env.getProperty("jdbc.user")
        dataSource.password = env.getProperty("jdbc.pass")
        return dataSource
    }
}
