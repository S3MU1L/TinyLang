class Person
{
    init( name, age )
    {
        this.name = name;
        this.age = age;
    }

    greet()
    {
        return "Hello, my name is " + this.name + " and I am " + this.age + " years old.";
    }
}

let john = Person("John", 30);
print john.greet();