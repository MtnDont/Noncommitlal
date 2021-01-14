import math

x = int(input("X: "))
z = int(input("Z: "))

regionX = str(math.floor(math.floor(x / 16) / 32))
regionZ = str(math.floor(math.floor(z / 16) / 32))

print("Region X: " + regionX + "\nRegion Z: " + regionZ)
