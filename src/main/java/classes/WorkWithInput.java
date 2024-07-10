package classes;

import exceptions.InputException;

import java.util.Arrays;

public class WorkWithInput {
    private static final char[] alphabet = "-abcdefghijklm".toCharArray();
    private static final char[] nums = "0123456789".toCharArray();

    public static int[] convert(String s) throws InputException {
        char[] inp = s.toLowerCase().toCharArray();
        checker(inp);
        char letter = inp[0];
        int flag = inp[inp.length - 1] == 'f' ? 1 : 0;
        int index1 = Arrays.binarySearch(alphabet, letter);
        int index2 = 0;
        for (int i = 1; i < inp.length - flag; i++) {
            int d = Arrays.binarySearch(nums, inp[i]);
            index2 *= 10;
            index2 += d;
        }
        return new int[]{index2, index1, flag};
    }

    private static void checker(char[] charInp) throws InputException {
        for (int i = 0; i < charInp.length; i++) {
            if (i == 0 && Arrays.binarySearch(alphabet, charInp[i]) == -1) throw new InputException();
            else if (i == charInp.length - 1 && Arrays.binarySearch(nums, charInp[i]) == -1 && charInp[i] != 'f') throw new InputException();
            else if (Arrays.binarySearch(nums, charInp[i]) == -1) throw new InputException();
        }
    }
}
