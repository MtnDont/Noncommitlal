import os
import pyodbc
import subprocess
from threading import Thread
from queue import Queue,Empty
import random
import time
from faker import Faker
from datetime import datetime

os.system("javac TaskFive.java")

p = subprocess.Popen(
    [
        'java',
        '-p',
        'mssql14.jar',
        'TaskFive'
    ],
    stdout = subprocess.PIPE, 
    stdin = subprocess.PIPE,
)

fake = Faker('en_US')

def getabit(o,q):
    for c in iter(lambda:o.read(1),b''):
        q.put(c)
    o.close()

def getdata(q):
    r = b''
    while True:
        try:
            c = q.get(False)
        except Empty:
            break
        else:
            r += c
    return r

q = Queue()
t = Thread(target=getabit,args=(p.stdout,q))
t.daemon = True
t.start()

string = "20\n"
outputstr = "f"

server = os.environ['AZURE_HOST']
database = os.environ['AZURE_DB']
username = os.environ['AZURE_USER']
password = os.environ['AZURE_PASS']
driver= '{ODBC Driver 17 for SQL Server}'

def fake_address():
    return fake.address().replace("\n", " ")

def random_item_indb(table: str, item: str):
    teamnames = []
    with pyodbc.connect('DRIVER='+driver+';SERVER='+server+';PORT=1433;DATABASE='+database+';UID='+username+';PWD='+ password) as conn:
        with conn.cursor() as cursor:
            cursor.execute("SELECT " + table + "." + item + " FROM " + table)
            row = cursor.fetchone()
            while row:
                string = ""
                for item in row:
                    #print (str(row[0]) + " " + str(row[1]))
                    teamnames.append(str(item))
                row = cursor.fetchone()
    return random.choice(teamnames)

def createPerson():
    gender = random.choice(['M','F'])
    race = random.choice(['Caucasian', 'African American', 'Asian', 'Native American', 'Other'])
    relation = random.choice(['sibling', 'cousin', 'parent', 'grandparent', 'child', 'guardian', 'spouse'])
    orgaffil = random.choice(['', random_item_indb("Organization", "orgname")])
    query = [fake.ssn(), orgaffil, fake.name(), fake.date(), race, gender, fake.job(), 'Y']
    query = query + [fake_address(), fake.email(), fake.phone_number(), fake.phone_number(), fake.phone_number()]
    query = query + [fake.name(), relation, fake_address(), fake.email(), fake.phone_number(), fake.phone_number(), fake.phone_number(), 'N']
    return query

def createOrg():
    borc = random.choice(['business','church'])
    query = [fake.company(), fake.company_email(), fake.phone_number(), fake.first_name()]
    query = query + [borc]
    if borc == 'business':
        btype = random.choice(['LLC', "Corporation", "S Corp", "Sole Proprietorship"])
        query = query + [btype, str(random.randint(1, 5000)), fake.url()]
    else:
        affiliation = random.choice(['Catholic', 'Protestant'])
        query = query + [affiliation]
    return query

def createDonation():
    campaign = random.choice(['charity', 'sports', 'fundraising', 'holiday', 'gift'])
    anon = random.choice(["Y", "N"])
    checkcredit = random.choice(["check", "credit"])
    query = [str(random.randint(5, 25000)), fake.date(), campaign, anon, checkcredit]
    if checkcredit == "credit":
        query = query + [fake.credit_card_number(), fake.credit_card_provider(), fake.credit_card_expire()]
    else:
        query = query + ['{:04d}'.format(random.randint(1, 9999))]
    return query

f = open("output.txt", "w")
q1 = ["Team"]
q2 = ["Person", "ContactInfo", "EmergencyContact", "Client", "Insurance", "Needs", "CaresFor"]
q3 = ["Person", "ContactInfo", "EmergencyContact", "Volunteer", "Volunteers"]
q4 = ["Volunteers"]
q5 = ["Person", "ContactInfo", "EmergencyContact", "Employee", "Team"]
q6 = ["Expense"]
q7 = ["Organization", "Business", "Church", "Sponsors"]
q8 = ["Person", "ContactInfo", "EmergencyContact", "Donor", "DonorDonation", "DonorCheck", "DonorCredit"]
q9 = ["Organization", "Business", "Church", "OrgDonation", "OrgCheck", "OrgCredit"]
q10 = []
q11 = []
q12 = []
q13 = []
q14 = []
q15 = []
q16 = ["Employee"]
q17 = ["Client", "ContactInfo", "EmergencyContact", "Person", "Needs", "Insurance", "CaresFor"]
q18 = []
q19 = []
q20 = []
qlist = [q1, q2, q3, q4, q5, q6, q7, q8, q9, q10, q11, q12, q13, q14, q15, q16, q17, q18, q19, q20]

#queryList = [query1, query2, query3, query4, query5, query6, query7, query8, query9, query10, query11, query12, query13, query14, query15, query16, query17, query18, query19, query20]]
queryruns = [5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1]

def queryReturn(qnum: int):
    query = []
    if qnum == 1:
        query = ['1', fake.company(), '444-44-4446', '444-44-4445', 'review', fake.date()]
    elif qnum == 2:
        query = ['2'] + createPerson()
        query = query + [fake.name(), fake.phone_number(), fake.name(), fake.phone_number(), fake.date()]
        query = query + ['3',fake_address(),'health', 'Y',
                        '3',fake_address(),'auto', 'N']
        query = query + ['transportation','8','n']
        query = query + ['team1', 'n']
    elif qnum == 3:
        query = ['3'] + createPerson()
        query = query + [fake.date(), fake.date(), fake_address()]
        query = query + ['team2', 'n']
    elif qnum == 4:
        ssn = random_item_indb("Volunteers", "ssn")
        teamn = random_item_indb("Volunteers", "teamname")
        newsal = str(random.randint(10000, 100000))
        print(ssn)
        print(teamn)
        print(str(random.randint(10000, 100000)))
        query = ['4', ssn, teamn, newsal]
    elif qnum == 5:
        query = ['5'] + createPerson()
        query = query + [str(random.randint(10000, 100000)), random.choice(['married', 'single', 'divorced']), fake.date()]
        query = query + [random_item_indb("Team", "teamname"), 'n']
    elif qnum == 6:
        query = ['6', random_item_indb("Employee", "ssn"), fake.date(), str(random.randint(5, 25000)), 'transportation'] 
    elif qnum == 7:
        query = ['7'] + createOrg()
        query = query + [random_item_indb("Team", "teamname"), 'n']
    elif qnum == 8:
        query = ['8'] + createPerson()
        query = query + createDonation() + ['n']
    elif qnum == 9:
        query = ['9'] + createOrg()
        query = query + createDonation() + ['n']
    elif qnum == 10:
        query = ['10', random_item_indb("Client", "ssn")]
    elif qnum == 11:
        query = ['11', fake.date_between('-50y','-10y').strftime("%Y-%m-%d"), fake.date_between('-10y','today').strftime("%Y-%m-%d")]
    elif qnum == 12:
        query = ['12', random_item_indb("Client", "ssn")]
    elif qnum == 13:
        query = ['13']
    elif qnum == 14:
        query = ['14']
    elif qnum == 15:
        query = ['15', fake.date()]
    elif qnum == 16:
        query = ['16']
    elif qnum == 17:
        query = ['17']
    elif qnum == 18:
        query = ['18', 'teams.csv']
    elif qnum == 19:
        query = ['19', 'namelist.csv']
    elif qnum == 20:
        query = ['20']

    return query

def fwriteline(fileString):
    f.write(fileString + "\n")

def sickofthisshit(table_string):
    fwriteline("\n" + table_string)
    with pyodbc.connect('DRIVER='+driver+';SERVER='+server+';PORT=1433;DATABASE='+database+';UID='+username+';PWD='+ password) as conn:
        with conn.cursor() as cursor:
            cursor.execute("SELECT * FROM " + table_string)
            colString = ""
            for column in cursor.description:
                colString = colString + column[0] + ", "
            fwriteline(colString[:-2])
            row = cursor.fetchone()
            while row:
                string = ""
                for item in row:
                    #print (str(row[0]) + " " + str(row[1]))
                    string = string + str(item) + ", "
                fwriteline(string[:-2])
                row = cursor.fetchone()

def queries():
    i = 0
    j = 16
    bigqlist = queryReturn(j+1)
    bigqlist.append('20')
    #for k in range(len(queryruns)):
    #    for l in range(queryruns[k]):
    #        bigqlist.append(queryReturn(k+1))

    #print(bigqlist)

    #idx = 0
    #for m in range(len(bigqlist)):
    #    idx = idx + len(bigqlist[m])*1.25

    #print("{} seconds".format( idx ))
    #print(bigqlist[j][0])

    while True:
        time.sleep(1.25)
        fwriteline(getdata(q).decode())
        if not t.is_alive():
            break
        print("i: {} j: {}".format(i,j))
        fwriteline(bigqlist[i])
        p.stdin.write(bytes(bigqlist[i],'utf-8'))
        p.stdin.write(b'\n')
        p.stdin.flush()
        i = i + 1
        if i == len(bigqlist):
            i = 0
            for table in qlist[int(bigqlist[0])-1]:
                sickofthisshit(table)
            #j = j + 1
            break
            

    
    #queryStr = ""
    #for i in range(len(query1)):
    #    queryStr = queryStr + query1[i] + "\n"
    #prgm_stdout = p.communicate(input=queryStr.encode('utf-8'))[0]
    #fwriteline(prgm_stdout.decode())
    

#for q in qlist:
#    for item in q:
#        sickofthisshit(item)

queries()

#sickofthisshit("Team")

#sickofthisshit("Expense")
f.close()