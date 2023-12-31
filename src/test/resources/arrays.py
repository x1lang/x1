class Person:
  def __init__(self, names):
    self.names = names

def main():
  names = ["John", "Doe"]
  p = Person(names = names)
  return p.names


