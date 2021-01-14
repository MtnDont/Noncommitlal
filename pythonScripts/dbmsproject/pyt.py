def funfunstr(boi: str):
    numtostr_dict = {1: '1 Insertion', 2: '2 Insertion', 3: '3 Insertion', 4: '4 Insertion', 5: '5 Insertion', 6: '6 Insertion', 7: '7 Insertion', 8: '8 Insertion', 9: '9 Insertion', 10: '10 Random Search', 11: '11 Range Search', 12: '12 Random Search', 13: '13 Random Search', 14: '14 Range Search', 15: '15 Range Search', 16: '16 Insertion', 17: '17 Deletion'}
    newstr = ""
    boilist = boi.split(", ")
    for item in boilist:
        newstr = newstr + numtostr_dict[int(item)] + ', '
    newstr = newstr[:-2]
    print(newstr)

funfunstr('8, 14')