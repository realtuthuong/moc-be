package com.example.MocBE.security;

public class Endpoints {

    // PUBLIC
    public static final String[] PUBLIC_GET_ENDPOINTS = {
            "api/menu-items/**",
            "api/category-menu-items/**",
            "api/location/**",
            "api/auth/verify-email/**",
            "api/payment/info/**",
            "api/payment/info/**",
            "api/payment/orderReservation",
            "api/table/**",
            "/api/category-ingredient/**",
            "/api/ingredient/**",
            "api/account/*/location",
            "api/account/*/statistics",
            "api/orders/**",

    };
    public static final String[] PUBLIC_POST_ENDPOINTS = {
            "/api/auth/**",
            "api/payment/customerOrder/**"
    };

    // CHEF
    public static final String[] CHEF_GET_ENDPOINTS = {
            "api/orders/today/**",
    };
    public static final String[] CHEF_PUT_ENDPOINTS = {
            "api/orders/*/complete"
    };

    // STAFF
    public static final String[] STAFF_GET_ENDPOINTS = {
            "api/orders",
            "/api/account/*"
    };
    public static final String[] STAFF_PUT_ENDPOINTS = {

    };
    public static final String[] STAFF_POST_ENDPOINTS = {
            "api/orders/staffOrder"

    };

    // DATA_ENTRY
    public static final String[] DATA_ENTRY_GET_ENDPOINTS = {
            "api/menu-items/**",
            "api/unit/**",
            "api/category-ingredient",
            "api/ingredient",
            "api/category-menu-items/**",

    };
    public static final String[] DATA_ENTRY_PUT_ENDPOINTS = {
            "api/menu-items/**",
            "api/category-menu-items/**",
            "api/category-ingredient",
            "api/unit/**",
            "api/ingredient",
    };
    public static final String[] DATA_ENTRY_POST_ENDPOINTS = {
            "api/menu-items/**",
            "api/category-menu-items/**",
            "api/category-ingredient",
            "api/unit/**",
            "api/ingredient",
    };

    public static final String[] DATA_ENTRY_DELETE_ENDPOINTS = {
            "api/menu-items/**",
            "api/category-menu-items/**",
            "api/category-ingredient",
            "api/unit/**",
            "api/ingredient",
    };

    // Manager
    public static final String[] MANAGER_GET_ENDPOINTS = {
            "api/menu-items/**",
            "api/category-menu-items/**",
            "api/category-ingredient",
            "api/unit/**",
            "api/ingredient",
            "api/location/**",
            "api/table/**",


    };
    public static final String[] MANAGER_PUT_ENDPOINTS = {
            "api/menu-items/**",
            "api/category-menu-items/**",
            "api/category-ingredient",
            "api/unit/**",
            "api/ingredient",
            "api/location/**",
            "api/table/**",
    };
    public static final String[] MANAGER_POST_ENDPOINTS = {
            "api/menu-items/**",
            "api/category-menu-items/**",
            "api/category-ingredient",
            "api/unit/**",
            "api/ingredient",
            "api/location/**",
            "api/table/**",
    };
    public static final String[] MANAGER_DELETE_ENDPOINTS = {
            "api/menu-items/**",
            "api/category-menu-items/**",
            "api/category-ingredient",
            "api/unit/**",
            "api/ingredient",
            "api/location/**",
            "api/table/**",
    };
}