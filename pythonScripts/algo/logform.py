x1 = False
x2 = False
x3 = True
print((x1 or (not x2)) and (x1 or x2 or x3) and ((not x1) or x2 or (not x3)) and ((not x1) or (not x2) or x3))