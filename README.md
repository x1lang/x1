# X1

X1 is a syntax for expressing code in a target language-agnostic way. Code written X1 are converted into the source code
of one or more target languages (e.g. Java, JavaScript, Typescript) before compilation.

It is not a fully fledged programming language, instead it is useful for generating code for other code generator tools
to mix into the generated code.

X1 is intentionally C-like language, to make it familiar to most people.

X1 is statically typed. This is to make it possible to generate code for statically typed languages. Typed are removed for dynamic languages.

Example:

```x1

function sum(v:Int, n:Int) {
  for (var i:Int = 0; i < n; i = i + 1) {
    if (i % 2 == 0) {
      v = v + i
    }
  }
  return v
}
```

A complete example:

```x1
type Person {
  name: String
  age: Int
}

function averageAge(persons:Person[]):Int {
  var sum:Int = 0
  foreach (person:Person; persons) {
    sum = + sum person.age
  }
  return / sum persons.length
}
```

Javascript:

```javascript
function sum(v, n) {
    for (let i = 0; i < n; i = i + 1) {
        if (i % 2 == 0) {
            v = v + i;
        }
    }
    return v;
}

```

Typescript:

```typescript
function sum(v: number, n: number): number {
    for (let i = 0; i < n; i = i + 1) {
        if (i % 2 == 0) {
            v = v + i;
        }
    }
    return v;
}
```

Java:

```java


public int sum(int v, int n) {
  for (int i = 0; i < n; i = i + 1) {
    if (i % 2 == 0) {
      v = v + i;
    }
  }
  return v;
}
```

Python:

```python
# This is a comment
def sum(v, n):
    i = 0
    while i < n:
        if i % 2 == 0:
            v = v + i
        i = i + 1
    return v
```

Go:

```go

func Sum(v int, n int) int {
for i := 0; i < n; i = i + 1 {
if i%2 == 0 {
v = v + i
}
}
return v
}
```

Kotlin:

```kotlin

fun sum(v: Int, n: Int): Int {
    for (i in 0 until n) {
        if (i % 2 == 0) {
            v = v + i
        }
    }
    return v
}
```

Rust:

```rust

fn sum(v: i32, n: i32) -> i32 {
    for i in 0..n {
        if i % 2 == 0 {
            v = v + i
        }
    }
    return v
}
```