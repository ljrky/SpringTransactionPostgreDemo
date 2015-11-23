package com.example;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication
public class TransactionPostgreDemoApplication {

    private static final Logger log = LoggerFactory.getLogger(TransactionPostgreDemoApplication.class);

    @Bean
    BookingService bookingService() {
        return new BookingService();
    }

    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://localhost:5433/dvdrental");
        ds.setUsername("postgres");
        ds.setPassword("test");
        return ds;
    }

    @Bean
    JdbcTemplate jdbcTemplate(DataSource dataSource){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        log.info("Creating tables");
        jdbcTemplate.execute("drop table IF EXISTS BOOKINGS");
        jdbcTemplate.execute("create table BOOKINGS("
                + "ID serial, FIRST_NAME varchar(5) NOT NULL)");
        return jdbcTemplate;
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {

        ApplicationContext ctx = SpringApplication.run(TransactionPostgreDemoApplication.class, args);

        BookingService bookingService = ctx.getBean(BookingService.class);
        List<String> results = bookingService.findAllActor();
        for (String result : results) {
            log.info("Actor's first name : " + result);
        }

//        bookingService.book("Alice", "Bob", "Carol");
//        Assert.assertEquals("First booking should work with no problem", 3,
//                bookingService.findAllBookings().size());
//
//        try {
//            bookingService.book("Chris", "Samuel");
//        }
//        catch (RuntimeException e) {
//            log.info("v--- The following exception is expect because 'Samuel' is too big for the DB ---v");
//            log.error(e.getMessage());
//        }
//
//        for (String person : bookingService.findAllBookings()) {
//            log.info("So far, " + person + " is booked.");
//        }
//        log.info("You shouldn't see Chris or Samuel. Samuel violated DB constraints, and Chris was rolled back in the same TX");
//        Assert.assertEquals("'Samuel' should have triggered a rollback", 3,
//                bookingService.findAllBookings().size());
//
//        try {
//            bookingService.book("Buddy", null);
//        }
//        catch (RuntimeException e) {
//            log.info("v--- The following exception is expect because null is not valid for the DB ---v");
//            log.error(e.getMessage());
//        }
//
//        for (String person : bookingService.findAllBookings()) {
//            log.info("So far, " + person + " is booked.");
//        }
//        log.info("You shouldn't see Buddy or null. null violated DB constraints, and Buddy was rolled back in the same TX");
//        Assert.assertEquals("'null' should have triggered a rollback", 3, bookingService
//                .findAllBookings().size());

    }
}
