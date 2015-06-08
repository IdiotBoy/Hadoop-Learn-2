package com.example.tutorial;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.example.tutorial.PersonProtos.Person;

public class ProtocalBufferExample {

    public static void main(String[] args) {
        Person person0 = Person.newBuilder()
            .setName("XX")
            .setEmail("xin.x@hulu.com")
            .setId(11111)
            .addPhone(Person.PhoneNumber.newBuilder()
                      .setNumber("12345678910")
                      .setType(0))
            .addPhone(Person.PhoneNumber.newBuilder()
                      .setNumber("98765432110")
                      .setType(1))
            .build();

        Person person1 = Person.newBuilder()
                .setName("XXX")
                .setEmail("xin.x@hulu.com")
                .setId(11112)
                .addPhone(Person.PhoneNumber.newBuilder()
                          .setNumber("12345678910")
                          .setType(0))
                .addPhone(Person.PhoneNumber.newBuilder()
                          .setNumber("98765432110")
                          .setType(1))
                .build();

        try {
            FileOutputStream output = new FileOutputStream("example.txt");
            person0.writeTo(output);
            person1.writeTo(output);
            output.close();
        } catch (Exception e) {
            System.out.print("Write error!");
        }
        
        try {
            FileInputStream input = new FileInputStream("example.txt");
            Person person2 = Person.parseFrom(input);
            System.out.println("person2: " + person2);
            //Person person22 = Person.parseFrom(input);
            //System.out.println("person2: " + person22);
        } catch (Exception e) {
            System.out.println("Read error!");
        }

    }

}
