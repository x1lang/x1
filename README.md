# X1

X1 is a syntax for expressing simple code in a target language-agnostic way. Code written X1 are converted into the target language (e.g. Java, JavaScript, Typescript) before compilation.

- Expressions
- Conditionals
- Loops
- Functions
- Variables
- Arrays
- Types (e.g. objects or structs)

An example program:

```x1
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