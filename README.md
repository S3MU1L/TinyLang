******# TinyLang Specification

TinyLang is a minimal, educational programming language designed for writing a small interpreter. The goal is to keep the language small but expressive enough to support variables, arithmetic, control flow, and functions.

---

## Lexical Structure

### Identifiers

* Consist of letters (`a-z`, `A-Z`), digits (`0-9`), and underscores (`_`)
* Must not start with a digit

### Literals

*   **Number literals**: sequences of digits, optionally with a decimal part (e.g., `42`, `123.45`)
*   **String literals**: characters enclosed in double quotes (e.g., `"hello"`)
*   **Boolean literals**: `true`, `false`
*   **Nil literal**: `nil`

### Keywords

```
let fn return if else for while true false class print super this and or nil
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
program       ::= declaration* EOF

declaration   ::= classDecl 
                | funDecl 
                | varDecl 
                | statement

classDecl     ::= "class" IDENT "{" function* "}" 

funDecl       ::= "fn" function 

varDecl       ::= "let" IDENT ("=" expression)? ";"

statement     ::= exprStmt 
                | forStmt 
                | ifStmt 
                | printStmt 
                | returnStmt 
                | whileStmt 
                | block

exprStmt      ::= expression ";" 

forStmt       ::= "for" "(" ( varDecl | exprStmt | ";" ) expression? ";" expression? ")" statement 

ifStmt        ::= "if" "(" expression ")" statement ( "else" statement )? 

printStmt     ::= "print" expression ";" 

returnStmt    ::= "return" expression? ";" 

whileStmt     ::= "while" "(" expression ")" statement 

block         ::= "{" declaration* "}"

expression    ::= assignment 

assignment    ::= ( call "." )? IDENT "=" assignment 
                | logic_or 

logic_or      ::= logic_and ( "or" logic_and )* 

logic_and     ::= equality ( "and" equality )* 

equality      ::= comparison ( ( "!=" | "==" ) comparison )* 

comparison    ::= term ( ( ">" | ">=" | "<" | "<=" ) term )*
 
term          ::= factor ( ( "-" | "+" ) factor )* 
 
factor        ::= unary ( ( "/" | "*" | "%" ) unary )*
 
unary         ::= ( "!" | "-" ) unary 
                | call 

call          ::= primary ( "(" arguments? ")" | "." IDENT )* 

primary       ::= "true" 
                | "false" 
                | "nil" 
                | "this" 
                | NUMBER 
                | STRING 
                | IDENT 
                | "(" expression ")" 
                | "super" "." IDENT

function      ::= IDENT "(" parameters? ")" block 

parameters    ::= IDENT ( "," IDENT )* 

arguments     ::= expression ( "," expression )*

```

---

## Semantics

### Variables

*   Declared with `let`
*   Example:

    ```
    let x = 10;
    let y = x + 5;
    ```

### Functions

*   Declared with `fn`
*   Functions are **first-class values**
*   Example:

    ```
    let add = fn(a, b) {
      return a + b;
    };
    let result = add(3, 4);
    ```

### Control Flow

*   **If/Else**:

    ```
    if (x < 10) {
      x = x + 1;
    } else {
      x = 0;
    }
    ```

*   **While**:

    ```
    while (x > 0) {
      x = x - 1;
    }
    ```

### Expressions

*   Support standard arithmetic and boolean operators
*   Example:

    ```
    let flag = (x > 0) and (y < 10);
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