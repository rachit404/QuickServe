package com.quickserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * ╔═══════════════════════════════════════════════════════════════════════════════╗
 * ║                     QUICKSERVE APPLICATION ENTRY POINT                         ║
 * ╠═══════════════════════════════════════════════════════════════════════════════╣
 * ║  This is where your Spring Boot application starts. When you run this class,  ║
 * ║  Spring Boot does the following automatically:                                 ║
 * ║                                                                                ║
 * ║  1. Scans all packages under com.quickserve for Spring components             ║
 * ║  2. Auto-configures beans based on dependencies in pom.xml                    ║
 * ║  3. Starts an embedded Tomcat server                                          ║
 * ║  4. Makes your REST APIs available to the world                               ║
 * ╚═══════════════════════════════════════════════════════════════════════════════╝
 *
 * UNDERSTANDING @SpringBootApplication:
 * This single annotation is actually a combination of three annotations:
 * 
 * 1. @Configuration
 *    - Marks this class as a source of bean definitions
 *    - You can define @Bean methods here if needed
 * 
 * 2. @EnableAutoConfiguration
 *    - Spring Boot's magic! It looks at your classpath (the JARs you have)
 *    - Automatically configures beans based on what it finds
 *    - Example: If it sees spring-boot-starter-web, it configures a web server
 *    - Example: If it sees PostgreSQL driver, it configures the datasource
 * 
 * 3. @ComponentScan
 *    - Scans the current package and all sub-packages
 *    - Finds all @Component, @Service, @Repository, @Controller classes
 *    - Registers them as Spring beans (managed objects)
 *
 * UNDERSTANDING @EnableCaching:
 * This annotation enables Spring's caching abstraction. It allows us to use
 * @Cacheable, @CacheEvict annotations on methods to cache their results in Redis.
 * 
 * Example flow:
 * - First call to getServiceProviders() → Queries DB → Stores in cache → Returns
 * - Second call to getServiceProviders() → Returns from cache (no DB query!)
 * 
 * WHY THIS FILE IS IN com.quickserve:
 * Spring Boot scans from this package downward. All our code is in sub-packages
 * like com.quickserve.controller, com.quickserve.service, etc. This ensures
 * everything gets discovered automatically.
 */
@SpringBootApplication
@EnableCaching // Enable Redis caching for faster responses
public class QuickServeApplication {

    /**
     * The main method - the entry point of any Java application.
     * 
     * SpringApplication.run() does all the heavy lifting:
     * - Creates the Spring Application Context (the container for all beans)
     * - Triggers auto-configuration
     * - Starts the embedded web server
     * - Registers shutdown hooks for graceful termination
     * 
     * @param args Command line arguments (rarely used in Spring Boot apps)
     */
    public static void main(String[] args) {
        // This single line starts your entire application!
        // Spring Boot handles all the complexity behind the scenes.
        SpringApplication.run(QuickServeApplication.class, args);
        
        // After this line executes, your server is running and accepting requests.
        // You'll see in the console: "Started QuickServeApplication in X seconds"
    }
}
