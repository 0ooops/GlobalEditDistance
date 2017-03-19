# -*- coding:utf-8 -*-
import Levenshtein
import json

train_file = open('train.txt','r')
dic_file = open('names.txt','r')
result_file = open('result.txt','w')
log_file = open('log.txt','w')

correct_predict = 0
total_predict = 0
correct_pair = {}

for line in train_file:
    init_word = line.split("\t")[0].lower()
    target_word = line.split("\t")[1].split("\n")[0].lower()
    # print(init_word)
    min_dis = 20
    current_best = []
    for name in dic_file:
        dic_word = name.split("\n")[0]
        distance = Levenshtein.distance(init_word,dic_word)
        # print(init_word, name, distance)
        if distance == min_dis:
            current_best.append(dic_word)
        if distance < min_dis:
            del current_best[:]
            min_dis = distance
            current_best.append(dic_word)

    # print(init_word, current_best, target_word, min_dis)
    log_file.write(init_word + "\t" + target_word + "\t" + str(min_dis) + "\t")
    for i in current_best:
        log_file.write(i)
        log_file.write("\t")
    log_file.write("\n")

    total_predict += len(current_best)
    if target_word in current_best:
        correct_predict += 1
        # print(init_word, target_word)
        correct_pair[init_word] = target_word
    # print("total predict: " + str(total_predict) + " correct predict: " + str(correct_predict) + "\n")

    dic_file.seek(0,0)

# print("total predict: " + str(total_predict) + " correct predict: " + str(correct_predict) + "\n")

result_file.write("total predict: " + str(total_predict) + " correct predict: " + str(correct_predict) + "\n")
jsObj = json.dumps(correct_pair)
result_file.write(jsObj)

train_file.close()
dic_file.close()
result_file.close()
log_file.close()