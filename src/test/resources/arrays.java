package main;

public class Pet {
  public String[] names;
}
public void main() {
  String[] names = new String[]{"John", "Doe"};
  Pet p = new Pet() { {this.names = names;
} };
}
