def merge(A, p, q, r):
    n1 = q - p + 1
    n2 = r - q
    L = [None] * n1
    R = [None] * n2
    for i in range(0, n1):
        L[i] = A[p + i - 1]
    for j in range(0, n2):
        R[j] = A[q + j]
    i = 1
    j = 1
    for k in range(p, r):
        if L[i-1] <= R[j-1]:
            A[k-1] = L[i-1]
            i = i + 1
        else:
            A[k-1] = R[j-1]
            j = j + 1
    print(A)

A = [11, 17, 23, 37, 59, 11, 7, 31, 47, 31]
q = 5
merge(A, 1, q, len(A))
