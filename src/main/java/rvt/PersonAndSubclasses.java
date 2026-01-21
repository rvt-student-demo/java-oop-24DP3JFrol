package rvt;

public class PersonAndSubclasses {
    // PART 1
    public static class Person {
        private String name;
        private String adress;

        public Person(String name, String adress) {
            this.name = name;
            this.adress = adress;
        }
        public String toString() {
            return this.name + "\n " + this.adress;
        }
    }
    // PART 2
}
