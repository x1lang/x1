class Person {
  names: String[]
}
function main(): String[] {
  let names: String[] = [John, Doe];
  let p: Person = {names: names};
  return p.names;
}
