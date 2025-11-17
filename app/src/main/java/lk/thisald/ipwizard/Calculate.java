package lk.thisald.ipwizard;
import java.util.Objects;

public class Calculate {

    public static String getFormattedAddressByClass(String address, String ipClass) {
        String[] splitIp = Valid.split(address, '.', 4);
        StringBuilder formattedAddress = new StringBuilder();
        Integer end = 0;
        if (Objects.equals(ipClass, "C"))
            end = 3;
        if (Objects.equals(ipClass, "B"))
            end = 2;
        if (Objects.equals(ipClass, "A"))
            end = 1;
        for (int i = 0; i < end; i++) {
            formattedAddress.append(Integer.parseInt(splitIp[i])).append(".");
        }
        formattedAddress.append(strMultiply("0.", (4 - end)));
        formattedAddress = new StringBuilder(formattedAddress.substring(0, formattedAddress.length() - 1));
        return formattedAddress.toString();
    }

    public static String getFormattedAddress(String address) {
        String formattedAddress = "";
        String[] splitIp = Valid.split(address, '.', 4);
        for (Integer i = 0; i < 3; i++) {
            formattedAddress += Integer.parseInt(splitIp[i]) + ".";
        }
        formattedAddress += String.valueOf(Integer.valueOf(splitIp[3]));
        return formattedAddress;
    }

    public static Integer twosPower(Integer value) {
        Double base = 2.0;
        Double exponent = 0.0;
        Double total = Math.pow(base, exponent);
        while (total.intValue() < value) {
            exponent += 1.0;
            total = Math.pow(base, exponent);
        }
        return exponent.intValue();
    }

    public static String strMultiply(String value, Integer multiply) {
        String multiplyValue = "";
        for (int i = 0; i < multiply; i++) {
            multiplyValue += value;
        }
        return multiplyValue;
    }

    public static String binaryToDecimal(String binVal) {
        Double exponent = 2.0;
        Double power = 0.0;
        Double decimal = 0.0;
        for (Integer i = binVal.length() - 1; i >= 0; i--) {
            decimal += Math.pow(exponent, power) * Double.valueOf(binVal.substring(i, i + 1));
            power += 1.0;
        }
        return String.valueOf(decimal.intValue());
    }

    public static String decimalToBinary(Integer number) {
        String binaryVal = "";
        while (number > 0) {
            binaryVal = number % 2 + binaryVal;
            number = (int) number / 2;
        }
        return binaryVal;
    }

    public static Integer generateIpAddressCount(String subnetMask) {
        Double exponent = 0.0;
        for (Integer i = 9; i < subnetMask.length(); i++) {
            if (subnetMask.charAt(i) == '0') {
                exponent++;
            }
        }
        Double ipAddressCount = Math.pow(2.0, exponent);
        return ipAddressCount.intValue();
    }

    public static Integer generateSubnetsCount(String subnetMask) {
        Double exponent = 0.0;
        for (Integer i = 9; i < subnetMask.length(); i++) {
            if (subnetMask.charAt(i) == '1') {
                exponent++;
            }
        }
        Double subNetsCount = Math.pow(2.0, exponent);
        return subNetsCount.intValue();
    }

    public static String[][] generateRange(String subnetMask, String ipClass, String ipAddress, Integer arraySize) {
        Double blockSizeDouble = 0.0;
        Integer blockBits = 0;
        Integer blockGet = 1;
        Integer blockSize = 0;
        String[] subnetValues = new String[4];
        String binaryVal = "";
        subnetValues = Valid.split(subnetMask, '.', 4);
        for (Integer i = 0; i < 4; i++) {
            binaryVal = decimalToBinary(Integer.valueOf(subnetValues[i]));
            binaryVal = binaryVal + strMultiply("0", 8 - binaryVal.length());
            for (Integer i2 = 0; i2 < binaryVal.length(); i2++) {
                if (binaryVal.charAt(i2) == '0')
                    blockBits++;
            }
            if (!(blockBits == 0)) {
                break;
            }
            blockGet++;
        }

        blockSizeDouble = Math.pow(2, blockBits);
        blockSize = blockSizeDouble.intValue();
        String[][] ipRange = new String[arraySize][2];

        String[] ipAddressValues = Valid.split(ipAddress, '.', 4);
        String startIp = "";
        String endIp = "";
        String ipTemp = "";
        Integer index = 0;
        if (ipClass.equals("C")) {
            ipTemp = ipAddressValues[0] + "." + ipAddressValues[1] + "." + ipAddressValues[2];
            for (Integer ip4 = 0; ip4 < 256; ip4 += blockSize) {
                if (index >= arraySize)
                    break;
                startIp = ipTemp + "." + ip4;
                endIp = ipTemp + "." + (ip4 + blockSize - 1);
                ipRange[index] = new String[] { startIp, endIp };
                index++;
            }
        }
        if (ipClass.equals("B")) {
            ipTemp = ipAddressValues[0] + "." + ipAddressValues[1];

            if (blockGet == 3) {
                for (Integer ip3 = 0; ip3 < 256; ip3 += blockSize) {
                    if (index >= arraySize)
                        break;
                    startIp = ipTemp + "." + ip3 + "." + "0";
                    endIp = ipTemp + "." + (ip3 + blockSize - 1) + "." + "255";
                    ipRange[index] = new String[] { startIp, endIp };
                    index++;
                }
            }
            if (blockGet == 4) {
                for (Integer ip3 = 0; ip3 < 256; ip3++) {
                    if (index >= arraySize)
                        break;
                    for (Integer ip4 = 0; ip4 < 256; ip4 += blockSize) {
                        if (index >= arraySize)
                            break;
                        startIp = ipTemp + "." + ip3 + "." + ip4;
                        endIp = ipTemp + "." + ip3 + "." + (ip4 + blockSize - 1);
                        ipRange[index] = new String[] { startIp, endIp };
                        index++;
                    }
                }
            }
        }
        if (ipClass.equals("A")) {
            ipTemp = ipAddressValues[0];
            if (blockGet == 2) {
                for (Integer ip2 = 0; ip2 < 256; ip2 += blockSize) {
                    if (index >= arraySize)
                        break;
                    startIp = ipTemp + "." + ip2 + "." + "0" + "." + "0";
                    endIp = ipTemp + "." + (ip2 + blockSize - 1) + "." + "255" + "." + "255";
                    ipRange[index] = new String[] { startIp, endIp };
                    index++;
                }
            }
            if (blockGet == 3) {
                for (Integer ip2 = 0; ip2 < 256; ip2++) {
                    if (index >= arraySize)
                        break;
                    for (Integer ip3 = 0; ip3 < 256; ip3 += blockSize) {
                        if (index >= arraySize)
                            break;
                        startIp = ipTemp + "." + ip2 + "." + ip3 + "." + "0";
                        endIp = ipTemp + "." + ip2 + "." + (ip3 + blockSize - 1) + "." + "255";
                        ipRange[index] = new String[] { startIp, endIp };
                        index++;
                    }
                }
            }
            if (blockGet == 4) {
                for (Integer ip2 = 0; ip2 < 256; ip2++) {
                    if (index >= arraySize)
                        break;
                    for (Integer ip3 = 0; ip3 < 256; ip3++) {
                        if (index >= arraySize)
                            break;
                        for (Integer ip4 = 0; ip4 < 256; ip4 += blockSize) {
                            if (index >= arraySize)
                                break;
                            startIp = ipTemp + "." + ip2 + "." + ip3 + "." + ip4;
                            endIp = ipTemp + "." + ip2 + "." + ip3 + "." + (ip4 + blockSize - 1);
                            ipRange[index] = new String[] { startIp, endIp };
                            index++;
                        }
                    }
                }
            }
        }
        return ipRange;
    }

    public static Integer[] getNetBitsHostBits(String ipClass, Integer subnets, Integer hosts) {
        Integer subnetBits = 0;
        Integer hostBits = 0;
        if (ipClass.equals("C")) {
            if (!subnets.equals(-1)) {
                subnetBits = twosPower(subnets);
                hostBits = 8 - subnetBits;
            } else {
                hostBits = twosPower(hosts);
                subnetBits = 8 - hostBits;
            }
        } else if (ipClass.equals("B")) {
            if (!subnets.equals(-1)) {
                subnetBits = twosPower(subnets);
                hostBits = 16 - subnetBits;
            } else {
                hostBits = twosPower(hosts);
                subnetBits = 16 - hostBits;
            }
        } else if (ipClass.equals("A")) {
            if (!subnets.equals(-1)) {
                subnetBits = twosPower(subnets);
                hostBits = 24 - subnetBits;
            } else {
                hostBits = twosPower(hosts);
                subnetBits = 24 - hostBits;
            }
        }
        Integer[] subHostBits = new Integer[] { subnetBits, hostBits };
        return subHostBits;
    }

    public static Integer[] getNetBitsHostBits(String subnetMask, String ipClass) {
        Integer start = 0;
        if (ipClass.equals("A"))
            start = 1;
        if (ipClass.equals("B"))
            start = 2;
        if (ipClass.equals("C"))
            start = 3;
        String[] subnetValues = new String[4];
        String binaryVal = "";
        Integer subnetBits = 0;
        Integer hostBits = 0;
        subnetValues = Valid.split(subnetMask, '.', 4);
        for (Integer i = start; i < 4; i++) {
            binaryVal = decimalToBinary(Integer.valueOf(subnetValues[i]));
            binaryVal = binaryVal + strMultiply("0", 8 - binaryVal.length());
            for (Integer i2 = 0; i2 < binaryVal.length(); i2++) {
                if (binaryVal.charAt(i2) == '1')
                    subnetBits++;
                else if (binaryVal.charAt(i2) == '0')
                    hostBits++;
            }
        }
        Integer[] subHostBits = new Integer[] { subnetBits, hostBits };
        return subHostBits;
    }

    public static String generateSubnetMask(String ipClass, Integer subnetBits, Integer hostBits) {
        String sub1 = "255";
        String sub2 = "";
        String sub3 = "";
        String sub4 = "";

        String sub2Bin, sub3Bin, sub4Bin;
        sub2Bin = "";
        sub3Bin = "";
        sub4Bin = "";
        if (ipClass.equals("C")) {
            sub2 = "255";
            sub3 = "255";
            sub4Bin = strMultiply("1", subnetBits) + strMultiply("0", hostBits);
            sub4 = binaryToDecimal(sub4Bin);
        } else if (ipClass.equals("B")) {
            sub2 = "255";
            if (subnetBits >= 8) {
                sub3Bin = strMultiply("1", 8);
                sub4Bin = strMultiply("1", subnetBits - 8) + strMultiply("0", hostBits);
                sub3 = binaryToDecimal(sub3Bin);
                sub4 = binaryToDecimal(sub4Bin);
            } else {
                sub3Bin = strMultiply("1", subnetBits) + strMultiply("0", 8 - subnetBits);
                sub4Bin = strMultiply("0", subnetBits);
                sub3 = binaryToDecimal(sub3Bin);
                sub4 = binaryToDecimal(sub4Bin);
            }
        } else if (ipClass.equals("A")) {
            if (subnetBits >= 16) {
                sub2Bin = strMultiply("1", 8);
                sub3Bin = strMultiply("1", 8);
                sub4Bin = strMultiply("1", subnetBits - 16) + strMultiply("0", hostBits);
                sub2 = binaryToDecimal(sub2Bin);
                sub3 = binaryToDecimal(sub3Bin);
                sub4 = binaryToDecimal(sub4Bin);
            } else if (subnetBits >= 8) {
                sub2Bin = strMultiply("1", 8);
                sub3Bin = strMultiply("1", subnetBits - 8) + strMultiply("0", hostBits - 8);
                sub4Bin = strMultiply("0", 8);
                sub2 = binaryToDecimal(sub2Bin);
                sub3 = binaryToDecimal(sub3Bin);
                sub4 = binaryToDecimal(sub4Bin);
            } else {
                sub2Bin = strMultiply("1", subnetBits) + strMultiply("0", 8 - subnetBits);
                sub3Bin = strMultiply("0", 8);
                sub4Bin = strMultiply("0", 8);
                sub2 = binaryToDecimal(sub2Bin);
                sub3 = binaryToDecimal(sub3Bin);
                sub4 = binaryToDecimal(sub4Bin);
            }
        }
        return (sub1 + "." + sub2 + "." + sub3 + "." + sub4);
    }
}