sum(v, n) {
  for (let  i = 0; i < n; i = i + 1) {
    if (i % 2 === 0) {
      v = v + i;
    }
  }
  let  y = [1, 2];
  for (let x of y) {
    v = v + x;
  }
  return v;
}
