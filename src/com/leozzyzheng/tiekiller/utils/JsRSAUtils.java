package com.leozzyzheng.tiekiller.utils;

import java.util.Vector;

/**
 * Created by leozzyzheng on 2015/3/11.
 * Copyright 1998-2005 David Shapiro.
 * Dave Shapiro
 * dave@ohdave.com
 * 这是百度贴吧所用的RSA加密JS代码的Java翻译版本，虽然使用的是不安全的RSA算法，但是反正不是我自己的数据，能登陆就是好代码
 * <p/>
 * 示例
 * String mod = "B3C61EBBA4659C4CE3639287EE871F1F48F7930EA977991C7AFE3CC442FEA49643212E7D570C853F368065CC57A2014666DA8AE7D493FD47D171C0D894EEE3ED7F99F6798B7FFD7B5873227038AD23E3197631A8CB642213B9F27D4901AB0D92BFA27542AE890855396ED92775255C977F5C302F1E7ED4B1E369C12CB6B1822F";
 * String exp = "10001";
 * String str = "这里是需要加密的明文";
 * KeyPair keyPair = JsRSAUtils.getInstance().generateKeyPair(exp, "", mod);
 * System.out.println(JsRSAUtils.getInstance().encryptedString(keyPair, str));
 */
public class JsRSAUtils {
    private static final double biRadixBase = 2;
    private static final double biRadixBits = 16;
    private static final double bitsPerDigit = biRadixBits;
    private static final double biRadix = 1 << 16; //= 2^16 = 65536
    private static final double biHalfRadix = (int) biRadix >>> 1;
    private static final double biRadixSquared = biRadix * biRadix;
    private static final double maxDigitVal = biRadix - 1;
    private static final long maxInteger = 9999999999999998l;

    //maxDigits:
    //Change this to accommodate your largest number size. Use setMaxDigits()
    //to change it!
    //
    //In general, if you're working with numbers of size N bits, you'll need 2*N
    //bits of storage. Each digit holds 16 bits. So, a 1024-bit key will need
    //
    //1024 * 2 / 16 = 128 digits of storage.
    //

    private int maxDigits;
    private BigInt bigZero, bigOne;

    //下面的函数是为了方便在Java中执行Js的位操作
    //Js的位操作是32位精度的，Java的Int实际上因为符号占一位，不能表示完全的32位
    //所以在小于31位的时候，全部当int处理，大于31位但是小于32位的当long处理，大于32位的还是当做int处理

    private double doOR(double x, double y) {
        if (x > Integer.MAX_VALUE && x <= 1l << 32) {
            if (y > Integer.MAX_VALUE && y <= 1l << 32) {
                return (long) x | (long) y;
            } else {
                return (long) x | (int) y;
            }
        } else {
            return (int) x | (int) y;
        }
    }

    private double doAnd(double x, double y) {
        if (x > Integer.MAX_VALUE && x <= 1l << 32) {
            if (y > Integer.MAX_VALUE && y <= 1l << 32) {
                return (long) x & (long) y;
            } else {
                return (long) x & (int) y;
            }
        } else {
            return (int) x & (int) y;
        }
    }

    private double doLeftShift(double x, int y) {
        if (x > Integer.MAX_VALUE && x <= 1l << 32) {
            return (long) x << y;
        } else {
            return (int) x << y;
        }
    }

    private double doSignPropagatingRightShift(double x, int y) {
        if (x > Integer.MAX_VALUE && x <= 1l << 32) {
            return (long) x >> y;
        } else {
            return (int) x >> y;
        }
    }

    private double doZeroFillRightShift(double x, int y) {
        if (x > Integer.MAX_VALUE && x <= 1l << 32) {
            return (long) x >>> y;
        } else {
            return (int) x >>> y;
        }
    }

    private class BigInt {
        public double[] digits;
        public boolean isNeg;

        public BigInt(boolean flag) {
            if (flag) {
                digits = null;
            } else {
                digits = new double[maxDigits];
            }

            isNeg = false;
        }

        public BigInt() {
            this(false);
        }
    }

    private class BarrettMu {
        public BigInt modulus;
        public int k;
        public BigInt mu;
        public BigInt bkplus1;

        public BarrettMu(BigInt m) {
            this.modulus = biCopy(m);
            this.k = biHighIndex(this.modulus) + 1;
            BigInt b2k = new BigInt();
            b2k.digits[2 * this.k] = 1; // b2k = b^(2k)
            this.mu = biDivide(b2k, this.modulus);
            this.bkplus1 = new BigInt();
            this.bkplus1.digits[this.k + 1] = 1; // bkplus1 = b^(k+1)
        }

        public BigInt modulo(BigInt x) {
            BigInt q1 = biDivideByRadixPower(x, this.k - 1);
            BigInt q2 = biMultiply(q1, this.mu);
            BigInt q3 = biDivideByRadixPower(q2, this.k + 1);
            BigInt r1 = biModuloByRadixPower(x, this.k + 1);
            BigInt r2term = biMultiply(q3, this.modulus);
            BigInt r2 = biModuloByRadixPower(r2term, this.k + 1);
            BigInt r = biSubtract(r1, r2);
            if (r.isNeg) {
                r = biAdd(r, this.bkplus1);
            }
            boolean rgtem = biCompare(r, this.modulus) >= 0;
            while (rgtem) {
                r = biSubtract(r, this.modulus);
                rgtem = biCompare(r, this.modulus) >= 0;
            }
            return r;
        }

        public BigInt multiplyMod(BigInt x, BigInt y) {
            BigInt xy = biMultiply(x, y);
            return this.modulo(xy);
        }

        public BigInt powMod(BigInt x, BigInt y) {
            BigInt result = new BigInt();
            result.digits[0] = 1;
            BigInt a = x;
            BigInt k = y;
            while (true) {
                if (doAnd((k.digits[0]), 1) != 0) result = this.multiplyMod(result, a);
                k = biShiftRight(k, 1);
                if (k.digits[0] == 0 && biHighIndex(k) == 0) break;
                a = this.multiplyMod(a, a);
            }
            return result;
        }
    }

    public class KeyPair {
        public BigInt e;
        public BigInt d;
        public BigInt m;
        public int chunkSize;
        public int radix;
        public BarrettMu barrett;

        public KeyPair(String encryptionExponent, String decryptionExponent, String modulus) {
            this.e = biFromHex(encryptionExponent);
            this.d = biFromHex(decryptionExponent);
            this.m = biFromHex(modulus);
            // We can do two bytes per digit, so
            // chunkSize = 2 * (number of digits in modulus - 1).
            // Since biHighIndex returns the high index, not the number of digits, 1 has
            // already been subtracted.
            this.chunkSize = 2 * biHighIndex(this.m);
            this.radix = 16;
            this.barrett = new BarrettMu(this.m);
        }
    }

    public void setMaxDigits(int value) {
        maxDigits = value;
    }

    private JsRSAUtils() {
        setMaxDigits(130);
    }

    private static JsRSAUtils instance = null;

    public static JsRSAUtils getInstance() {
        synchronized (JsRSAUtils.class) {
            if (instance == null) {
                instance = new JsRSAUtils();
            }
        }

        return instance;
    }

    private BigInt biFromHex(String s) {
        BigInt result = new BigInt();
        for (int i = s.length(), j = 0; i > 0; i -= 4, ++j) {
            int beginIndex = Math.max(i - 4, 0);
            int endIndex = beginIndex + Math.min(i, 4);
            result.digits[j] = hexToDigit(s.substring(beginIndex, endIndex));
        }

        return result;
    }

    private double hexToDigit(String s) {
        double result = 0;
        int sl = Math.min(s.length(), 4);
        for (int i = 0; i < sl; ++i) {
            result = doLeftShift(result, 4);
            result = doOR(result, charToHex(s.charAt(i)));
        }

        return result;
    }

    private int charToHex(char c) {
        int ZERO = 48;
        int NINE = ZERO + 9;
        int littleA = 97;
        int littleZ = littleA + 25;
        int bigA = 65;
        int bigZ = 65 + 25;
        int result;

        if (c >= ZERO && c <= NINE) {
            result = c - ZERO;
        } else if (c >= bigA && c <= bigZ) {
            result = 10 + c - bigA;
        } else if (c >= littleA && c <= littleZ) {
            result = 10 + c - littleA;
        } else {
            result = 0;
        }
        return result;
    }

    private int biHighIndex(BigInt x) {
        int result = x.digits.length - 1;
        while (result > 0 && x.digits[result] == 0) --result;
        return result;
    }

    private BigInt biDivideByRadixPower(BigInt x, int n) {
        BigInt result = new BigInt();
        System.arraycopy(x.digits, n, result.digits, 0, result.digits.length - n);
        return result;
    }

    private BigInt biMultiply(BigInt x, BigInt y) {
        BigInt result = new BigInt();
        double c;
        int n = biHighIndex(x);
        int t = biHighIndex(y);
        double uv;
        int k;

        for (int i = 0; i <= t; ++i) {
            c = 0;
            k = i;
            for (int j = 0; j <= n; ++j, ++k) {
                uv = result.digits[k] + x.digits[j] * y.digits[i] + c;
                result.digits[k] = doAnd(uv, maxDigitVal);
                c = doZeroFillRightShift(uv, (int) biRadixBits);
            }
            result.digits[i + n + 1] = c;
        }

        result.isNeg = x.isNeg != y.isNeg;
        return result;
    }

    private BigInt biModuloByRadixPower(BigInt x, int n) {
        BigInt result = new BigInt();
        System.arraycopy(x.digits, 0, result.digits, 0, n);
        return result;
    }

    private int number(boolean b) {
        return b ? 1 : 0;
    }

    private BigInt biSubtract(BigInt x, BigInt y) {
        BigInt result;
        if (x.isNeg != y.isNeg) {
            y.isNeg = !y.isNeg;
            result = biAdd(x, y);
            y.isNeg = !y.isNeg;
        } else {
            result = new BigInt();
            double n, c;
            c = 0;
            for (int i = 0; i < x.digits.length; ++i) {
                n = x.digits[i] - y.digits[i] + c;
                result.digits[i] = n % biRadix;
                // Stupid non-conforming modulus operation.
                if (result.digits[i] < 0) result.digits[i] += biRadix;
                c = 0 - number(n < 0);
            }
            // Fix up the negative sign, if any.
            if (c == -1) {
                c = 0;
                for (int i = 0; i < x.digits.length; ++i) {
                    n = 0 - result.digits[i] + c;
                    result.digits[i] = n % biRadix;
                    // Stupid non-conforming modulus operation.
                    if (result.digits[i] < 0) result.digits[i] += biRadix;
                    c = 0 - number(n < 0);
                }
                // Result is opposite sign of arguments.
                result.isNeg = !x.isNeg;
            } else {
                // Result is same sign.
                result.isNeg = x.isNeg;
            }
        }
        return result;
    }

    private BigInt biAdd(BigInt x, BigInt y) {
        BigInt result;

        if (x.isNeg != y.isNeg) {
            y.isNeg = !y.isNeg;
            result = biSubtract(x, y);
            y.isNeg = !y.isNeg;
        } else {
            result = new BigInt();
            double c = 0;
            double n;
            for (int i = 0; i < x.digits.length; ++i) {
                n = x.digits[i] + y.digits[i] + c;
                result.digits[i] = n % biRadix;
                c = number(n >= biRadix);
            }
            result.isNeg = x.isNeg;
        }
        return result;
    }

    private int biCompare(BigInt x, BigInt y) {
        if (x.isNeg != y.isNeg) {
            return 1 - 2 * number(x.isNeg);
        }
        for (int i = x.digits.length - 1; i >= 0; --i) {
            if (x.digits[i] != y.digits[i]) {
                if (x.isNeg) {
                    return 1 - 2 * number(x.digits[i] > y.digits[i]);
                } else {
                    return 1 - 2 * number(x.digits[i] < y.digits[i]);
                }
            }
        }
        return 0;
    }

    private int[] lowBitMasks = {0x0000, 0x0001, 0x0003, 0x0007, 0x000F, 0x001F,
            0x003F, 0x007F, 0x00FF, 0x01FF, 0x03FF, 0x07FF,
            0x0FFF, 0x1FFF, 0x3FFF, 0x7FFF, 0xFFFF};

    private BigInt biShiftRight(BigInt x, double n) {
        double digitCount = Math.floor(n / bitsPerDigit);
        BigInt result = new BigInt();
        System.arraycopy(x.digits, (int) digitCount, result.digits, 0, x.digits.length - (int) digitCount);
        double bits = n % bitsPerDigit;
        double leftBits = bitsPerDigit - bits;
        for (int i = 0, i1 = i + 1; i < result.digits.length - 1; ++i, ++i1) {
            result.digits[i] = doOR(
                    doZeroFillRightShift(result.digits[i], (int) bits),
                    doLeftShift(
                            doAnd(result.digits[i1], lowBitMasks[((int) bits)]),
                            (int) leftBits)
            );
        }
        result.digits[result.digits.length - 1] = doZeroFillRightShift(result.digits[result.digits.length - 1], (int) bits);
        result.isNeg = x.isNeg;
        return result;
    }

    private double[] highBitMasks = {0x0000, 0x8000, 0xC000, 0xE000, 0xF000, 0xF800,
            0xFC00, 0xFE00, 0xFF00, 0xFF80, 0xFFC0, 0xFFE0,
            0xFFF0, 0xFFF8, 0xFFFC, 0xFFFE, 0xFFFF};

    private BigInt biShiftLeft(BigInt x, double n) {
        int digitCount = (int) Math.floor(n / bitsPerDigit);
        BigInt result = new BigInt();
        System.arraycopy(x.digits, 0, result.digits, digitCount,
                result.digits.length - digitCount);
        double bits = n % bitsPerDigit;
        double rightBits = bitsPerDigit - bits;
        int i = result.digits.length - 1;
        for (int i1 = i - 1; i > 0; --i, --i1) {
            result.digits[i] = doOR(
                    doAnd(
                            doLeftShift(result.digits[i], (int) bits)
                            , maxDigitVal
                    ),
                    doZeroFillRightShift(
                            doAnd(result.digits[i1], highBitMasks[(int) bits])
                            , ((int) rightBits)
                    )
            );
        }
        result.digits[0] = doAnd(doLeftShift(result.digits[i], (int) bits), maxDigitVal);
        result.isNeg = x.isNeg;
        return result;
    }

    private BigInt biCopy(BigInt bi) {
        BigInt result = new BigInt(true);
        result.digits = bi.digits.clone();
        result.isNeg = bi.isNeg;
        return result;
    }

    private BigInt biDivide(BigInt x, BigInt y) {
        return biDivideModulo(x, y)[0];
    }

    private BigInt[] biDivideModulo(BigInt x, BigInt y) {
        double nb = biNumBits(x);
        double tb = biNumBits(y);
        boolean origYIsNeg = y.isNeg;
        BigInt q, r;
        if (nb < tb) {
            if (x.isNeg) {
                q = biCopy(bigOne);
                q.isNeg = !y.isNeg;
                x.isNeg = false;
                y.isNeg = false;
                r = biSubtract(y, x);
                // Restore signs, 'cause they're references.
                x.isNeg = true;
                y.isNeg = origYIsNeg;
            } else {
                q = new BigInt();
                r = biCopy(x);
            }
            return new BigInt[]{q, r};
        }

        q = new BigInt();
        r = x;

        // Normalize Y.
        int t = (int) (Math.ceil(tb / bitsPerDigit) - 1);
        int lambda = 0;
        while (y.digits[t] < biHalfRadix) {
            y = biShiftLeft(y, 1);
            ++lambda;
            ++tb;
            t = (int) (Math.ceil(tb / bitsPerDigit) - 1);
        }
        // Shift r over to keep the quotient constant. We'll shift the
        // remainder back at the end.
        r = biShiftLeft(r, lambda);
        nb += lambda; // Update the bit count for x.
        int n = (int) (Math.ceil(nb / bitsPerDigit) - 1);

        BigInt b = biMultiplyByRadixPower(y, n - t);
        while (biCompare(r, b) != -1) {
            ++q.digits[n - t];
            r = biSubtract(r, b);
        }
        for (int i = n; i > t; --i) {
            double ri = (i >= r.digits.length) ? 0 : r.digits[i];
            double ri1 = (i - 1 >= r.digits.length) ? 0 : r.digits[i - 1];
            double ri2 = (i - 2 >= r.digits.length) ? 0 : r.digits[i - 2];
            double yt = (t >= y.digits.length) ? 0 : y.digits[t];
            double yt1 = (t - 1 >= y.digits.length) ? 0 : y.digits[t - 1];
            if (ri == yt) {
                q.digits[i - t - 1] = maxDigitVal;
            } else {
                q.digits[i - t - 1] = (int) Math.floor((ri * biRadix + ri1) / yt);
            }

            double c1 = q.digits[i - t - 1] * ((yt * biRadix) + yt1);
            double c2 = (ri * biRadixSquared) + ((ri1 * biRadix) + ri2);
            while (c1 > c2) {
                --q.digits[i - t - 1];
                c1 = q.digits[i - t - 1] * doOR(yt * biRadix, yt1);
                c2 = (ri * biRadix * biRadix) + ((ri1 * biRadix) + ri2);
            }

            b = biMultiplyByRadixPower(y, i - t - 1);
            r = biSubtract(r, biMultiplyDigit(b, q.digits[i - t - 1]));
            if (r.isNeg) {
                r = biAdd(r, b);
                --q.digits[i - t - 1];
            }
        }
        r = biShiftRight(r, lambda);
        // Fiddle with the signs and stuff to make sure that 0 <= r < y.
        q.isNeg = x.isNeg != origYIsNeg;
        if (x.isNeg) {
            if (origYIsNeg) {
                q = biAdd(q, bigOne);
            } else {
                q = biSubtract(q, bigOne);
            }
            y = biShiftRight(y, lambda);
            r = biSubtract(y, r);
        }
        // Check for the unbelievably stupid degenerate case of r == -0.
        if (r.digits[0] == 0 && biHighIndex(r) == 0) r.isNeg = false;

        return new BigInt[]{q, r};
    }

    private int biNumBits(BigInt x) {
        int n = biHighIndex(x);
        double d = x.digits[n];
        int m = (int) ((n + 1) * bitsPerDigit);
        int result;
        for (result = m; result > m - bitsPerDigit; --result) {
            if (doAnd(d, 0x8000) != 0) break;
            d = doLeftShift(d, 1);
        }
        return result;
    }

    private BigInt biMultiplyByRadixPower(BigInt x, int n) {
        BigInt result = new BigInt();
        System.arraycopy(x.digits, 0, result.digits, n, result.digits.length - n);
        return result;
    }

    private BigInt biMultiplyDigit(BigInt x, double y) {
        double n, c, uv;
        BigInt result = new BigInt();
        n = biHighIndex(x);
        c = 0;
        for (int j = 0; j <= n; ++j) {
            uv = result.digits[j] + x.digits[j] * y + c;
            result.digits[j] = doAnd(uv, maxDigitVal);
            c = doZeroFillRightShift(uv, (int) biRadixBits);
            //c = Math.floor(uv / biRadix);
        }
        result.digits[((int) (1 + n))] = c;
        return result;
    }

    public String encryptedString(KeyPair key, String s) {
        Vector<Integer> a = new Vector<Integer>();
        int sl = s.length();
        int i = 0;
        while (i < sl) {
            a.add((int) s.charAt(i));
            i++;
        }

        while (a.size() % key.chunkSize != 0) {
            a.add(0);
        }

        int al = a.size();
        String result = "";
        int j, k;
        BigInt block;
        for (i = 0; i < al; i += key.chunkSize) {
            block = new BigInt();
            j = 0;
            for (k = i; k < i + key.chunkSize; ++j) {
                block.digits[j] = a.get(k++);
                block.digits[j] += doLeftShift(a.get(k++), 8);
            }
            BigInt crypt = key.barrett.powMod(block, key.e);
            String text = key.radix == 16 ? biToHex(crypt) : biToString(crypt, key.radix);
            result += text + " ";
        }
        return result.substring(0, result.length() - 1); // Remove last space.
    }

    private char[] hexToChar =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f'};

    private String digitToHex(double n) {
        int mask = 0xf;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; ++i) {
            result.append(hexToChar[((int) doAnd(n, mask))]);
            n = doZeroFillRightShift(n, 4);
        }
        return result.reverse().toString();
    }

    private String biToHex(BigInt x) {
        String result = "";
        for (int i = biHighIndex(x); i > -1; --i) {
            result += digitToHex(x.digits[i]);
        }
        return result;
    }

    private char[] hexatrigesimalToChar = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'
    };

    private String biToString(BigInt x, int radix) { // 2 <= radix <= 36
        BigInt b = new BigInt();
        b.digits[0] = radix;
        BigInt[] qr = biDivideModulo(x, b);
        StringBuilder result = new StringBuilder("" + hexatrigesimalToChar[((int) qr[1].digits[0])]);
        while (biCompare(qr[0], bigZero) == 1) {
            qr = biDivideModulo(qr[0], b);
            //digit = qr[1].digits[0];
            result.append(hexatrigesimalToChar[((int) qr[1].digits[0])]);
        }
        return (x.isNeg ? "-" : "") + result.reverse().toString();
    }

    public KeyPair generateKeyPair(String encryptionExponent, String decryptionExponent, String modulus) {
        return new KeyPair(encryptionExponent, decryptionExponent, modulus);
    }
}
