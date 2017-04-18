# -*- coding:utf-8 -*-

# This class is for search for the patterns between the first letter of a Persian name
# and the first letter of the corresponding name. The output is a Matrix with frequency
# of each possible pair.
# @Author: Jiayu Wang
# @Date: Mar 22, 2017

import json


class searchPatterns:

    def replaceMatrix(self):
        train_file = open('.\\train.txt', 'r')
        matrix_file = open('.\\count_matrix.json', 'w')
        percentage_file = open('.\\percentage_matrix.json', 'w')
        replace_matrix = []

        for i in range(27):
            replace_matrix.append([])
            for j in range(26):
                replace_matrix[i].append(0)

        for line in train_file:
            init_first = line.split("\t")[0][0].lower()
            target_first = line.split("\t")[1].split("\n")[0][0].lower()

            if init_first.isalpha():
                row = ord(init_first) - ord('a')
            elif init_first == "'":
                row = 26
            else:
                print("New character identified: " + init_first)

            if target_first.isalpha():
                col = ord(target_first) - ord('a')
            else:
                print("New character identified: " + target_first)

            replace_matrix[row][col] += 1

        total = []
        for p in range(27):
            subtotal = 0
            for q in range(26):
                subtotal += replace_matrix[p][q]
            total.append(subtotal)

        percentage_matrix = []
        for m in range(27):
            percentage_matrix.append([])
            for n in range(26):
                if total[m] == 0:
                    percentage_matrix[m].append(0)
                else:
                    percentage_matrix[m].append(replace_matrix[m][n]/total[m])

        # print(replace_matrix)
        json_str1 = json.dumps(replace_matrix)
        json_str2 = json.dumps(percentage_matrix)
        matrix_file.write(json_str1)
        percentage_file.write(json_str2)

        train_file.close()
        matrix_file.close()
        percentage_file.close()


# Test Cases
test = searchPatterns()
test.replaceMatrix()