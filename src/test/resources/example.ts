function sum(v: number, n: number): number {
  for (let i: number = 0; i < n; i = i + 1) {
    if (i % 2 === 0) {
      v = v + i;
    }
  }
  let y: number[] = [1, 2];
  for (let x of y) {
    v = v + x;
  }
  return v;
}