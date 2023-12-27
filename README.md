# X1

X1 is a syntax for expressing simple programs in a language-agnostic way. Because it must be able to specify
code in a way that can be turned into other programming languages.

- Expressions
- Conditionals
- Loops
- Functions
- Variables
- Arrays
- Types (e.g. objects or structs)

An example program:

```x1
package x
// This is a comment
function sum(v, n) {
  for (i = 0; i < n; i = i + 1) {
    if (i % 2 == 0) {
      v = v + i
    }
  }
  return v
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
package x;

// This is a comment
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
  for i in range(0, n):
    if i % 2 == 0:
      v = v + i
  return v
```

Go:

```go
// This is a comment
package x

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
// This is a comment
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
// This is a comment
fn sum(v: i32, n: i32) -> i32 {
    for i in 0..n {
        if i % 2 == 0 {
            v = v + i
        }
    }
    return v
}
```