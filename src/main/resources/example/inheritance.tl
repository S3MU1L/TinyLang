class Person {
    init(name, age) {
        this.name = name;
        this.age = age;
    }

    greet() {
        print "Hello, my name is " + this.name + " and I am " + this.age + " years old.";
    }
}

class Employee extends Person {
    init(name, age, position) {
        super.init(name, age);
        this.position = position;
    }

    work() {
        print "I am working as a " + this.position + ".";
    }
}

let john = Person("John", 30);
john.greet();
let alice = Employee("Alice", 28, "Engineer");
alice.greet();
alice.work();
