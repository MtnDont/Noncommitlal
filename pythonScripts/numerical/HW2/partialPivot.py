import numpy as np
def gaussian_elimination_with_pivot(m):
  # forward elimination
  n = len(m)
  for i in range(n):
    pivot(m, n, i)
    for j in range(i+1, n):
      m[j] = [m[j][k] - m[i][k]*m[j][i]/m[i][i] for k in range(n+1)]

  if m[n-1][n-1] == 0: raise ValueError('No unique solution')

  # backward substitution
  print(np.array(m, dtype='float'))
  x = [0] * n
  for i in range(n-1, -1, -1):
    s = sum(m[i][j] * x[j] for j in range(i, n))
    x[i] = (m[i][n] - s) / m[i][i]
  return x

def pivot(m, n, i):
  max = -1e100
  for r in range(i, n):
    if max < abs(m[r][i]):
      max_row = r
      max = abs(m[r][i])
  m[i], m[max_row] = m[max_row], m[i]

if __name__ == '__main__':
  #m = [[0,-2,6,-10], [-1,3,-6,5], [4,-12,8,12]]
  #m = [[1,-1,3,2], [3,-3,1,-1], [1,1,0,3]]
  m = [[3,1,4,-1, 7], [2,-2,-1,2,1], [5,7,14,-8,20], [1,3,2,4,-4]]
  print(gaussian_elimination_with_pivot(m))