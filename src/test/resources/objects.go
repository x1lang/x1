package main

type Artist struct {
	Age int
}

func (this Artist) string() string {
	return "What's up?"
}
