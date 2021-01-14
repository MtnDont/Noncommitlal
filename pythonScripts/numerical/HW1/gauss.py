import numpy as np

def gauss_elim(A, b):
    augMatrix = np.hstack([A, b.reshape(-1, 1)])
    n = len(b)

    for i in range(n):
        for j in range(i + 1, n):
            b = augMatrix[j]
            m = augMatrix[i, i] / b[i]
            augMatrix[j] = augMatrix[i] - m * b
        
    return back_sub(augMatrix, n)

def back_sub(Ab, n):
    for i in range(n - 1, -1, -1):
        Ab[i] = Ab[i] / Ab[i, i]
        for j in range(i - 1, -1, -1):
            b = Ab[j]
            m = Ab[i, i] / b[i]
            Ab[j] = Ab[i] - m * b
        
    return Ab[:, 3]

A1 = np.array([[3, 2, -4],
              [-4, 5, -1],
              [2, -3, 5]], dtype='float')
A2 = np.array([[3, 2, -4],
              [2, -3, 5],
              [-4, 5, -1]], dtype='float')
A3 = np.array([[1, 2, 3],
              [4, 5, 6],
              [7, 8, 9]], dtype='float')
b13 = np.array([-5, 3, 11])
b2 = np.array([-5, 11, 3])

print(gauss_elim(A1, b13))
print(gauss_elim(A2, b2))
print(gauss_elim(A3, b13))