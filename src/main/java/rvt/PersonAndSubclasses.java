package rvt;

import java.util.ArrayList;

public class PersonAndSubclasses {
    // PART 1
    public static class Person {
        private String name;
        private String address;

        public Person(String name, String address) {
            this.name = name;
            this.address = address;
        }
        public String toString() {
            return this.name + "\n " + this.address;
        }
    }
    // PART 2
    public static class Student extends Person {
        private int credits;
        public Student(String name, String address) {
            super(name, address);
            this.credits = 0;
        }
        public void study() {
            this.credits++;
        }
        public int credits() {
            return this.credits;
        }
        public String toString() {
            return super.toString() + "\n credits " + this.credits;
        }
    }
    // PART 3
    public static class  Teacher extends Person {
        private int salary;
        public Teacher(String name, String address, int salary) {
            super(name, address);
            this.salary = salary;
        }
        public String toString() {
            return super.toString() + "\n salary " + this.salary + "euro/month";
        }
    }
    // PART 4 and 5
    public static void printPersons(ArrayList<Person> persons) {
        for (Person person : persons) {
            System.out.println(person);
        }
    }
    public static void main(String[] args) {
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(new Person("Ada Lovelace", "24 Maddox St. London W1S 2QN"));
        Student student = new Student("Ollie", "6381 Hollywood Blvd. Los Angeles 90028");
        student.study();
        student.study();
        persons.add(student);
        persons.add(new Teacher("Esko Ukkonen", "Mannerheimintie 15 00100 Helsinki", 5400));
        printPersons(persons);
    }
}

