public class Person {
  public int age;
}
public void main() {
  Person p = new Person() { {this.age = 31;
} };
}
