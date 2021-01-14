import numpy as np
from numpy import linalg as lin

# Gaussian Elimination w/o Partial Pivoting
# Forward Substitution
def gauss_elim(A, b):
    augMatrix = np.hstack([A, b.reshape(-1, 1)])
    n = len(b)

    for i in range(n):
        for j in range(i + 1, n):
            b = augMatrix[j]
            m = augMatrix[i, i] / b[i]
            augMatrix[j] = augMatrix[i] - m * b
        
    return back_sub(augMatrix, n)

# w/o Partial Pivot Backward Substitution
def back_sub(Ab, n):
    for i in range(n - 1, -1, -1):
        Ab[i] = Ab[i] / Ab[i][i]
        for j in range(i - 1, -1, -1):
            b = Ab[j]
            m = Ab[i][i] / b[i]
            Ab[j] = Ab[i] - m * b
        
    return Ab[:, 3]

# Gaussian Elimination w/ Partial Pivoting
# Forward Substitution
def gauss_elim_pivot(A, b):
    augMatrix = np.hstack([A, b.reshape(-1, 1)])
    n = len(A)
    for i in range(n):
        pivot(augMatrix, n, i)
        for j in range(i+1, n):
            augMatrix[j] = [augMatrix[j][k] - augMatrix[i][k]*augMatrix[j][i]/augMatrix[i][i] for k in range(n+1)]
    
    return back_sub_pivot(augMatrix, n)

def pivot(Ab, n, i):
    max = -1
    for r in range(i, n):
        if max < abs(Ab[r][i]):
            max_row = r
            max = abs(Ab[r][i])
    Ab[[i, max_row]] = Ab[[max_row, i]]

# w/ Partial Pivot Backward Substitution
def back_sub_pivot(Ab, n):
    x = np.array([0] * n, dtype='float')
    for i in range(n - 1, -1, -1):
        s = sum(Ab[i][j] * x[j] for j in range(i, n))
        x[i] = (Ab[i][n] - s) / Ab[i][i]
    return x

# Gaussian Elimination w/ Partial Pivoting and Row indicating vector
# Forward Substitution
def gauss_elim_pivot_ind(A, b):
    augMatrix = np.hstack([A, b.reshape(-1, 1)])
    n = len(A)
    r = np.array([k for k in range(1, n+1)], dtype='int')

    for i in range(n):
        pivot_ind_vector(augMatrix, n, i, r)
        for j in range(i+1, n):
            augMatrix[r[j]-1] = [augMatrix[r[j]-1][k] - augMatrix[r[i]-1][k]*augMatrix[r[j]-1][i]/augMatrix[r[i]-1][i] for k in range(n+1)]
    
    return back_sub_pivot_ind(augMatrix, n, r)

def pivot_ind_vector(Ab, n, i, r):
    max = -1
    for j in range(i, n):
        if max < abs(Ab[r[j]-1][i]):
            max_row = j
            max = abs(Ab[r[j]-1][i])
    r[[i, max_row]] = r[[max_row, i]]

# w/ and Row indicating vector Partial Pivot Backward Substitution
def back_sub_pivot_ind(Ab, n, r):
    x = np.array([0] * n, dtype='float')
    for i in range(n - 1, -1, -1):
        s = sum(Ab[r[i]-1][j] * x[j] for j in range(i, n))
        x[i] = (Ab[r[i]-1][n] - s) / Ab[r[i]-1][i]
    return x


if __name__ == '__main__':
    A = np.array([[3, 1, 4, -1],
                [2, -2, -1, 2],
                [5, 7, 14, -8],
                [1, 3, 2, 4]], dtype='float')
    b = np.array([7, 1, 20, -4], dtype='float')
    print(gauss_elim(A, b))
    print(gauss_elim_pivot(A, b))
    print(gauss_elim_pivot_ind(A, b))

    Anorm = np.array([[1, 2, 3],
                      [2, 4 - 1.0E-12, 7],
                      [1/5, -3/7, 2/5]], dtype='float')
    xexact = np.array([1, 2, 3], dtype='float')
    b = Anorm.dot(xexact)
    x = gauss_elim(Anorm, b)