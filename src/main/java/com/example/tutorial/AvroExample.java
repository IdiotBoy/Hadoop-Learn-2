package com.example.tutorial;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;


public class AvroExample {

    public static void main(String[] args) {
        PhoneNumber phoneNumber1 = PhoneNumber.newBuilder()
            .setNumber("12345678910")
            .setType(0)
            .build();
        PhoneNumber phoneNumber2 = PhoneNumber.newBuilder()
                .setNumber("98765432110")
                .setType(1)
                .build();
        List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>();
        phoneNumbers.add(phoneNumber1);
        phoneNumbers.add(phoneNumber2);
        
        PersonAvro person = PersonAvro.newBuilder()
            .setName("xin xu")
            .setEmail("xin.x@hulu.com")
            .setId(132)
            .setPhone(phoneNumbers)
            .build();
        
        File file = new File("person.txt");
        try {
            DatumWriter<PersonAvro> personDatumWriter = new SpecificDatumWriter<PersonAvro>(PersonAvro.class);
            DataFileWriter<PersonAvro> dataFileWriter = new DataFileWriter<PersonAvro>(personDatumWriter);
            dataFileWriter.create(person.getSchema(), file);
            dataFileWriter.append(person);
            dataFileWriter.close();
        } catch (Exception e) {
            System.out.println("Write Error:" + e);
        }
        try {
            DatumReader<PersonAvro> userDatumReader = new SpecificDatumReader<PersonAvro>(PersonAvro.class);
            DataFileReader<PersonAvro> dataFileReader = new DataFileReader<PersonAvro>(file, userDatumReader);
            person = null;
            while (dataFileReader.hasNext()) {
                person = dataFileReader.next(person);
                System.out.println(person);
            }
        } catch (Exception e) {
            System.out.println("Read Error:" + e);
        }

    }

}
