import os

#Gets list of all files
files = []
for (dirpath, dirnames, filenames) in os.walk('.'):
    files.extend(filenames)
    break

#Removes running script from file
if __file__ in files:
    files.remove(__file__)

#Gets number of lines from list of all files
for name in files:
    lines = 0
    with open(name, 'r') as f:
        for l in f.readlines():
            lines = lines + 1
        print("Lines in " + str(name) + ": " + str(lines))
