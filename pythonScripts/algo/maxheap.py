def BuildMaxHeap(A):
    heapSize = len(A)
    for i in range(int(len(A)/2), -1, -1):
        A = MaxHeapify(A, i+1)
    print(A)

def MaxHeapify(A, i):
    largest = -1
    l = 2 * i
    r = 2 * i + 1
    if l <= len(A) and A[l - 1] > A[i - 1]:
        largest = l
    else:
        largest = i
    if r <= len(A) and A[r - 1] > A[largest - 1]:
        largest = r
    if largest != i:
        temp = A[i - 1]
        A[i - 1] = A[largest - 1]
        A[largest - 1] = temp
        A = MaxHeapify(A, largest)
    return A

A = [11, 17, 23, 59, 37, 47, 11, 31, 7, 31]
BuildMaxHeap(A)
