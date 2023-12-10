package dev.yuizho.springawsandlocalstackdemo;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import dev.yuizho.springawsandlocalstackdemo.csv.Jackson2CsvS3ObjectConverter;
import io.awspring.cloud.s3.S3ObjectConverter;
import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class S3TemplateTest {
    @Autowired
    S3Template s3Template;

    @BeforeEach
    void cleanUp() {
        s3Template.deleteObject("test-bucket", "test-bucket/person.csv");
    }

    @Test
    void uploadFileToS3_And_downloadTheFile() {
        // given
        // upload a json file
        s3Template.store("test-bucket", "test-bucket/person.csv", new Person("Ito", "Yui"));

        // when
        // download the json file
        var downloaded = s3Template.read("test-bucket", "test-bucket/person.csv", Person.class);

        // then
        assertThat(downloaded.lastName).isEqualTo("Ito");
        assertThat(downloaded.firstName).isEqualTo("Yui");
    }

    static class Person {
        public String lastName;
        public String firstName;

        public Person() {
        }

        public Person(String lastName, String firstName) {
            this.lastName = lastName;
            this.firstName = firstName;
        }
    }

    @TestConfiguration
    static class CsvConfig {
        @Bean
        public S3ObjectConverter s3ObjectConverter() {
            return new Jackson2CsvS3ObjectConverter(new CsvMapper());
        }
    }
}
