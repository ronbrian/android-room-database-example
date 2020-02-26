package com.ron.mytodo.rest;

//public class UserApiResponse
//{
//    private Task data;
//
//    //Setters and getters
//
//    public String toString() {
//        return ""+data;
//    }
//}


public class UserApiResponse
{
    private User data;
    private String status;

    //Setters and getters

    public String toString() {
        return "" + data + status;
    }

    public String getStatus() {
        return status;
    }


}