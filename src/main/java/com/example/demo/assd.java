package com.example.demo;

import io.ebean.Ebean;

import java.util.List;

public class assd {
    public static void main(String[] args) {
       List<oldt> list = Ebean.find(oldt.class).findList();

        System.out.println(list);
    }
}
