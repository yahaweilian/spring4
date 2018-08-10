package com.myapp;

import spittr.util.PropertiesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestProperties {
    public static void main(String[] args) {

        Properties properties = PropertiesUtil.getProperties();

        String ss = properties.getProperty("appGroup");
        System.out.println(ss);

        String[] gg = ss.split("#");
        for(String g :gg){
            System.out.println(g);
        }
    }
}
