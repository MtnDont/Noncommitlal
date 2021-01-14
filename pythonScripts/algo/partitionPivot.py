def Partition(A, p, r, x):
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

A = [3, 13, 17, 199, 263, 2, 7, 37, 53, 331, 5, 11, 47, 59, 509]
p = 1
r = 15
x = 37
Partition(A, p, r, x)
