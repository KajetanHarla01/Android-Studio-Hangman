fileName = "words.txt"
file = open(fileName, 'r', encoding='utf8')
fileOutput = open('result.txt', 'w', encoding='utf8')
res = '<string-array name="wordsENG">\n'
count = 0
for word in file:
    res +=  "    <item>" + word.strip('\n').upper() + "</item>"
    res += '\n'
res += "</string-array>"
fileOutput.write(res)
fileOutput.close()
file.close()