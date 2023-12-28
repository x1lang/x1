func sum(v int, n int) int {
  for i := 0; i < n; i = i + 1 {
        if i % 2 == 0 {
      v = v + i;
    }
  }
  y := []int{1, 2};
  for x := range y {
    v = v + x;
  }
  return v;
}