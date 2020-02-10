package com.ron.mytodo.rest;

import com.ron.mytodo.model.Task;

public class UserApiResponse
{
    private Task data;

    //Setters and getters

    public String toString() {
        return ""+data;
    }
}