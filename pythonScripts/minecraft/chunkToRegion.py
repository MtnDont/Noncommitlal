import math

x = int(input("X: "))
z = int(input("Z: "))

regionX = str(math.floor(x / 32))
regionZ = str(math.floor(z / 32))

print("Region X: " + regionX + "\nRegion Z: " + regionZ)
