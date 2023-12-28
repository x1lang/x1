public int sum(int v, int n) {
  for (int i = 0; i < n; i = i + 1) {
    if (i % 2 == 0) {
      v = v + i;
    }
  }
  int[] y = new int[]{1, 2};
  for (int x : y) {
    v = v + x;
  }
  return v;
}