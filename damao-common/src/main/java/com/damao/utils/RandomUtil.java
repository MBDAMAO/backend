package com.damao.utils;

import java.util.Arrays;
import java.util.Random;

public class RandomUtil {
    /**
     * 一个随机权重算法，按照权重概率返回一个限定个数和的分类数组，在count较低的情况下，
     * 可以不考虑采用高级的数据结构，count数量级较大的情况可以采用二叉树优化查找
     */
    public static int[] randomByWeight(Long[] userVector, int count) {
        Double[] weight = new Double[userVector.length];
        Random random = new Random();
        double sum = 0D, pre = 0D;
        for (Long num : userVector) {
            sum += num;
        }
        for (int i = 0; i < userVector.length; i++) {
            pre += userVector[i] / sum;
            weight[i] = pre;
        }
        System.out.println(Arrays.toString(weight));
        int[] res = new int[userVector.length];
        for (int i = 0; i < count; i++) {
            double choice = random.nextDouble();
            for (int j = 0; j < userVector.length; j++) {
                // 首次遇见
                if (choice <= weight[j]) {
                    res[j] += 1;
                    // 妈的忘了break了
                    break;
                }
            }
        }
        return res;
    }
}
