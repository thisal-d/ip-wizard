package com.example.ipwizard20;

public class Valid {
    public static String[] split(String value, Character splitBy, Integer size) {
        String subValue = "";
        int index = 0;
        String[] strSplit = new String[size];
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == '.') {
                strSplit[index] = subValue;
                index++;
                subValue = "";
            } else {
                subValue += value.charAt(i);
            }
        }
        strSplit[index] = subValue;
        return strSplit;
    }

    public static Boolean ipFormatIsValid(String ipAddress) {
        int dots = 0;
        for (int i = 0; i < ipAddress.length(); i++) {
            if (ipAddress.charAt(i) == '.') {
                dots++;
            }
        }

        String[] ipAddressSplitStr = new String[4];
        try {
            ipAddressSplitStr = split(ipAddress, '.', 4);
        } catch (Exception e) {
            return false;
        }

        int index = 0;
        Integer[] ipAddressSplitInt = new Integer[4];
        index = 0;
        if (dots == 3) {
            for (int i = 0; i < 4; i++) {
                try {
                    int intIp = Integer.parseInt(ipAddressSplitStr[i]);
                    ipAddressSplitInt[index] = intIp;
                    index++;
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            return false;
        }
        for (Integer intIp : ipAddressSplitInt) {
            if (intIp > 255 || intIp < 0) {
                return false;
            }
        }
        return ipAddressSplitInt[0] != 127 && ipAddressSplitInt[0] >= 1 && ipAddressSplitInt[0] <= 223;
    }

    public static Boolean isValidSubnetMaskValue(Integer value) {
        int[] VALID_SUBNET_MASK_VALUES = { 255, 254, 252, 248, 240, 224, 192, 128, 0 };
        for (Integer validValue : VALID_SUBNET_MASK_VALUES) {
            if (validValue.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean isValidSubnetMask(String subnetMask, String ipClass) {
        int dots = 0;

        for (int i = 0; i < subnetMask.length(); i++) {
            if (subnetMask.charAt(i) == '.') {
                dots++;
            }
        }
        String[] subnetMaskSplitStr = new String[4];
        try {
            subnetMaskSplitStr = split(subnetMask, '.', 4);
        } catch (Exception e) {
            return false;
        }
        int index = 0;
        Integer[] subnetMaskSplitInt = new Integer[4];
        index = 0;
        if (dots == 3) {
            for (int i = 0; i < 4; i++) {
                try {
                    int intSubMask = Integer.parseInt(subnetMaskSplitStr[i]);
                    subnetMaskSplitInt[index] = intSubMask;
                    index++;
                } catch (Exception e) {
                    return false;
                }
            }
        } else {
            return false;
        }
        for (Integer intSubMask : subnetMaskSplitInt) {
            if (intSubMask > 255 || intSubMask < 0) {
                return false;
            }
        }

        if (ipClass.equals("C")) {
            if (subnetMaskSplitInt[0] == 255 && subnetMaskSplitInt[1] == 255 && subnetMaskSplitInt[2] == 255) {
                return isValidSubnetMaskValue(subnetMaskSplitInt[3]);
            } else {
                return false;
            }
        } else if (ipClass.equals("B")) {
            if (subnetMaskSplitInt[0] == 255 && subnetMaskSplitInt[1] == 255) {
                if (subnetMaskSplitInt[2] == 255) {
                    return isValidSubnetMaskValue(subnetMaskSplitInt[3]);
                } else
                    return isValidSubnetMaskValue(subnetMaskSplitInt[2]) && subnetMaskSplitInt[3] == 0;
            } else {
                return false;
            }
        } else if (ipClass.equals("A")) {
            if (subnetMaskSplitInt[0] == 255) {
                if (subnetMaskSplitInt[1] == 255) {
                    if (subnetMaskSplitInt[2] == 255) {
                        return isValidSubnetMaskValue(subnetMaskSplitInt[3]);
                    } else
                        return isValidSubnetMaskValue(subnetMaskSplitInt[2]) && subnetMaskSplitInt[3] == 0;
                } else
                    return isValidSubnetMaskValue(subnetMaskSplitInt[1]) && subnetMaskSplitInt[2] == 0
                            && subnetMaskSplitInt[3] == 0;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Boolean isValidSubnetCount(String strSubnetCount, Integer maxSubnetCount) {
        int subnetCount;
        try {
            subnetCount = Integer.parseInt(strSubnetCount);
        } catch (Exception e) {
            return false;
        }
        return subnetCount <= maxSubnetCount && subnetCount > 0;
    }

    public static Boolean isValidHostCount(String strHostCount, Integer maxHostCount) {
        int hostCount;
        try {
            hostCount = Integer.parseInt(strHostCount);
        } catch (Exception e) {
            return false;
        }
        return hostCount + 2 <= maxHostCount && hostCount > 0;
    }

    public static String getIpClass(String ipAddress) {
        int ip1 = Integer.parseInt(split(ipAddress, '.', 4)[0]);
        if (ip1 > 191) {
            return "C";
        } else if (ip1 > 127) {
            return "B";
        } else {
            return "A";
        }
    }

    public static boolean isValidIpRangeCount(String strRangeCount) {
        try {
            Integer.parseInt(strRangeCount);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}