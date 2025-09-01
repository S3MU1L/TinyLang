class Calculator {
    class add( a, b )
    {
        return a + b;
    }

    class sub( a, b )
    {
        return a - b;
    }

    class mul( a, b )
    {
        return a * b;
    }
}

print Calculator.add( 1, 2 );
print Calculator.sub( 5, 3 );
print Calculator.mul( 4, 2 );

let calc = Calculator();
print calc.add( 10, 20 );
print calc.sub( 50, 30 );
print calc.mul( 40, 20 );