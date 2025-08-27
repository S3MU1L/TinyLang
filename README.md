******# TinyLang Specification

TinyLang is a minimal, educational programming language designed for writing a small interpreter. The goal is to keep the language small but expressive enough to support variables, arithmetic, control flow, and functions.

---

## Lexical Structure

### Identifiers

* Consist of letters (`a-z`, `A-Z`), digits (`0-9`), and underscores (`_`)
* Must not start with a digit

### Literals

* **Integer literals**: sequences of digits (e.g., `42`, `0`, `1234`)
* **Boolean literals**: `true`, `false`

### Keywords

```
let  fn  return  if  else  while  true  false
```

### Operators

```
+  -  *  /  ==  !=  <  <=  >  >=  &&  ||  =
```

### Comments

* Single line: `// comment`

---

## Grammar

(EBNF-style)

```
program      ::= statement* EOF

statement    ::= letStmt
               | exprStmt
               | ifStmt
               | whileStmt
               | returnStmt
               | block

letStmt      ::= "let" IDENT "=" expression ";"
exprStmt     ::= expression ";"
ifStmt       ::= "if" "(" expression ")" statement ("else" statement)?
whileStmt    ::= "while" "(" expression ")" statement
returnStmt   ::= "return" expression? ";"
block        ::= "{" statement* "}"

expression   ::= assignment
assignment   ::= IDENT "=" assignment
               | logic_or

logic_or     ::= logic_and ("||" logic_and)*
logic_and    ::= equality ("&&" equality)*
equality     ::= comparison (("==" | "!=") comparison)*
comparison   ::= term (("<" | "<=" | ">" | ">=") term)*
term         ::= factor (("+" | "-") factor)*
factor       ::= unary (("*" | "/") unary)*
unary        ::= ("-" | "!") unary | primary
primary      ::= INTEGER
               | BOOLEAN
               | IDENT
               | "(" expression ")"
               | function

function     ::= "fn" "(" parameters? ")" block
parameters   ::= IDENT ("," IDENT)*
arguments    ::= expression ("," expression)*
```

---

## Semantics

### Variables

* Declared with `let`
* Example:

  ```
  let x = 10;
  let y = x + 5;
  ```

### Functions

* Declared inline using `fn`
* Functions are **first-class values**
* Example:

  ```
  let add = fn(a, b) {
    return a + b;
  };
  let result = add(3, 4);
  ```

### Control Flow

* **If/Else**:

  ```
  if (x < 10) {
    x = x + 1;
  } else {
    x = 0;
  }
  ```

* **While**:

  ```
  while (x > 0) {
    x = x - 1;
  }
  ```

### Expressions

* Support standard arithmetic and boolean operators
* Example:

  ```
  let flag = (x > 0) && (y < 10);
  ```

---

## Example Program

```
// Compute factorial
let fact = fn(n) {
  if (n == 0) {
    return 1;
  } else {
    return n * fact(n - 1);
  }
};

let result = fact(5);
```

---