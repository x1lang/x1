def sum(v, n):
  i = 0
  while i < n:
    if i % 2 == 0:
      v = v + i

    i = i + 1
  y = [1, 2]
  for x in y:
    v = v + x

  return v

