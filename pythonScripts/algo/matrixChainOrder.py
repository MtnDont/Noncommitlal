import sys
import numpy as np

def MatrixChainOrder(p, i, j): 
  
    if i == j: 
        return 0
  
    _min = sys.maxsize 
      
    # place parenthesis at different places  
    # between first and last matrix,  
    # recursively calculate count of 
    # multiplications for each parenthesis 
    # placement and return the minimum count 
    for k in range(i, j): 
      
        count = (MatrixChainOrder(p, i, k)  
             + MatrixChainOrder(p, k + 1, j) 
                   + p[i-1] * p[k] * p[j]) 
  
        if count < _min: 
            _min = count; 
      
  
    # Return minimum count 
    return _min; 

def matrixChainOrder(p):
    n = len(p) - 1
    m = [[0 for x in range(0, n)] for y in range(0, n)]
    s = [[0 for x in range(0, n)] for y in range(0, n)]
    for i in range(n):
        m[i][i] = 0
    for l in range(2, n+1):
        for i in range(0, n - l + 1):
            j = i + l - 1
            if i < j: # skip the i > j cases since we must multiply in order
                # cost values
                c = [m[i][k] + m[k+1][j] + p[i] * p[k+1] * p[j+1] for k in range(i, j)]

                # get minimum index and value from costs
                (s[i][j], m[i][j]) = min(enumerate(c), key=lambda x: x[1])

                print (i, j, [x for x in enumerate(c)])

                s[i][j] = s[i][j] + i + 1
    print("M ")
    print(np.array(m, np.int32))
    print("S ")
    print(np.array(s, np.int32))
    printOptimalParens(s, 0, len(s) - 1)
    print()
    for i in range(n):
        for j in range(n):
            print("M[" + str(i+1) + "," + str(j+1) + "] = " + str(m[i][j]))

def printOptimalParens(s, i, j):
    if i == j:
        print("A" + str(i+1), end='')
    else:
        print("(", end='')
        #print("DEBUG: " + str(i) + " " + str(j))
        printOptimalParens(s, i, s[i][j] - 1)
        printOptimalParens(s, s[i][j], j)
        print(")", end='')

#arr = [5,10,3,12,5,50,6]
arr = [5,2,3,10,4,6,7,8]
arr2 = np.array(arr)
print(MatrixChainOrder(arr, 1, len(arr)-1))
matrixChainOrder(arr)