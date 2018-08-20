package org.attendantsoffice.eventmanager;

import java.util.Properties;

import javax.sql.DataSource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EventManagerApplicationTest.MockDataSourceConfiguration.class)
public class EventManagerApplicationTest {

    @Test
    public void contextLoads() {
    }

    @Configuration
    public static class MockDataSourceConfiguration {

        @Bean
        public DataSource dataSource() {
            Properties properties = new Properties();
            properties.setProperty("url", "jdbc:h2:mem:lock");
            properties.setProperty("user", "sa");
            properties.setProperty("password", "");

            HikariConfig config = new HikariConfig();
            config.setPoolName("dataSource");
            config.setDataSourceClassName(JdbcDataSource.class.getName());
            config.setDataSourceProperties(properties);

            HikariDataSource dataSource = new HikariDataSource(config);

            return dataSource;
        }
    }
}
