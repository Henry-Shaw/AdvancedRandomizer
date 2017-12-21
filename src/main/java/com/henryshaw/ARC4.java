package com.henryshaw;

import com.henryshaw.prng.BasePRNG;

public class ARC4 extends BasePRNG implements java.io.Serializable
{
    /** The size of the internal S-box. */
    private static final int ARC4_SBOX_SIZE = 256;
    /** The S-box. */
    private byte[] s;
    private byte m, n;

    public ARC4()
    {
        super("ARC4");
    }

    public ARC4(byte[] seed)
    {
        super("ARC4");
        init(seed);
    }

    public ARC4(String seed)
    {
        super("ARC4");
        init(seed.getBytes());
    }

    public void setup(byte[] seed)
    {
        if (seed == null)
            throw new IllegalArgumentException("ARC4 needs a key");

        s = new byte[ARC4_SBOX_SIZE];
        m = n = 0;
        byte[] k = new byte[ARC4_SBOX_SIZE];

        for (int i = 0; i < ARC4_SBOX_SIZE; i++)
            s[i] = (byte)i;

        if (seed.length > 0)
            for (int i = 0, j = 0; i < ARC4_SBOX_SIZE; i++)
            {
                k[i] = seed[j++];
                if (j >= seed.length)
                    j = 0;
            }
        for (int i = 0, j = 0; i < ARC4_SBOX_SIZE; i++)
        {
            j = j + s[i] + k[i];
            byte temp = s[i];
            s[i] = s[j & 0xff];
            s[j & 0xff] = temp;
        }
        buffer = new byte[ARC4_SBOX_SIZE];
        fillBlock();
    }

    public void fillBlock()
    {
        for (int i = 0; i < buffer.length; i++)
        {
            m++;
            n = (byte)(n + s[m & 0xff]);
            byte temp = s[m & 0xff];
            s[m & 0xff] = s[n & 0xff];
            s[n & 0xff] = temp;
            temp = (byte)(s[m & 0xff] + s[n & 0xff]);
            buffer[i] = s[temp & 0xff];
        }
    }

}