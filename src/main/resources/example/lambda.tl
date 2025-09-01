let add = fn(a, b) { return a + b; };
print add(2, 3);

let twice = fn(f, x) { return f(f(x)); };
let inc = fn(n) { return n + 1; };
print twice(inc, 5);
