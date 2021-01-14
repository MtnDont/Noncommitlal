def Partition(A, p, r):
    x = A[r - 1]
    i = p - 1
    for j in range(p, r - 1):
        if A[j - 1] <= x:
            i = i + 1
            temp = A[i - 1]
            A[i - 1] = A[j - 1]
            A[j - 1] = temp
    temp = A[i]
    A[i] = A[r - 1]
    A[r - 1] = temp
    print("Partitioned A: ")
    print(A)
    return i

A = [11, 17, 23, 59, 37, 47, 11, 31, 7, 31]
p = 1
r = 10
Partition(A, p, r)
