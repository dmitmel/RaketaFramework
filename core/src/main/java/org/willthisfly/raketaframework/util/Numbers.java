package org.willthisfly.raketaframework.util;

public class Numbers {
    private Numbers() {
        throw new RuntimeException("Can\'t create instance of Numbers");
    }
    
    public static int length(int n) {
        if (n < 100000) {
            // 5 or less
            if (n < 100) {
                // 1 or 2
                if (n < 10)
                    return 1;
                else
                    return 2;
            } else {
                // 3 or 4 or 5
                if (n < 1000) {
                    return 3;
                } else {
                    // 4 or 5
                    if (n < 10000)
                        return 4;
                    else
                        return 5;
                }
            }
        } else {
            // 6 or more
            if (n < 10000000) {
                // 6 or 7
                if (n < 1000000)
                    return 6;
                else
                    return 7;
            } else {
                // 8 to 10
                if (n < 100000000) {
                    return 8;
                } else {
                    // 9 or 10
                    if (n < 1000000000)
                        return 9;
                    else
                        return 10;
                }
            }
        }
    }
}
