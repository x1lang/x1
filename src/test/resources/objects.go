package main

type Artist struct {
  Age int
}

func (this Artist) String() string {
  return "What's up?"
}
