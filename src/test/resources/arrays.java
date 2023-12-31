public class Person {
  public String[] names;
}
public String[] main() {
  String[] names = new String[]{"John", "Doe"};
  Person p = new Person() { {this.names = names;
} };
  return p.names;
}
