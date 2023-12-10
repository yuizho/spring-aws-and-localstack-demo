package dev.yuizho.springawsandlocalstackdemo;


import io.awspring.cloud.s3.S3Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class S3TemplateTest {
    @Autowired
    S3Template s3Template;

    @BeforeEach
    void cleanUp() {
        s3Template.deleteObject("test-bucket", "test-bucket/person.json");
    }

    @Test
    void uploadFileToS3_And_downloadTheFile() {
        // given
        // upload a json file
        s3Template.store(
                "test-bucket",
                "test-bucket/person.json",
                new Person("Ito", "Yui")
        );

        // when
        // download the json file
        var downloaded = s3Template.read("test-bucket", "test-bucket/person.json", Person.class);

        // then
        assertThat(downloaded.lastName).isEqualTo("Ito");
        assertThat(downloaded.firstName).isEqualTo("Yui");
    }

    static class Person {
        public String lastName;
        public String firstName;

        public Person(String lastName, String firstName) {
            this.lastName = lastName;
            this.firstName = firstName;
        }
    }
}
