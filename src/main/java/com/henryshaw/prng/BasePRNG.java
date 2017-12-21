package com.henryshaw.prng;

public abstract class BasePRNG implements java.io.Serializable {

    private static final double DOUBLE_UNIT = 0x1.0p-53;
    /**
     * The canonical name prefix of the PRNG algorithm.
     */
    protected String name;

    /**
     * Indicate if this instance has already been initialised or not.
     */
    protected boolean initialised;

    /**
     * A temporary buffer to serve random bytes.
     */
    protected byte[] buffer;

    /**
     * The index into buffer of where the next byte will come from.
     */
    protected int ndx;

    /**
     * Trivial constructor for use by concrete subclasses.
     *
     * @param name the canonical name of this instance.
     */
    protected BasePRNG(String name) {
        super();

        this.name = name;
        initialised = false;
        buffer = new byte[0];
    }

    public String name() {
        return name;
    }

    public void init(byte[] seed) {
        this.setup(seed);

        ndx = 0;
        initialised = true;
    }

    final protected int next(int numBits) {
        int numBytes = (numBits + 7) / 8;
        byte b[] = new byte[numBytes];
        int next = 0;

        nextBytes(b);
        for (int i = 0; i < numBytes; i++) {
            next = (next << 8) + (b[i] & 0xFF);
        }

        return next >>> (numBytes * 8 - numBits);
    }

    public byte nextByte() throws IllegalStateException {
        if (!initialised)
            throw new IllegalStateException();

        return nextByteInternal();
    }

    public void nextBytes(byte[] out) throws IllegalStateException {
        nextBytes(out, 0, out.length);
    }

    public void nextBytes(byte[] out, int offset, int length)
            throws IllegalStateException {
        if (!initialised)
            throw new IllegalStateException("not initialized");

        if (length == 0)
            return;

        if (offset < 0 || length < 0 || offset + length > out.length)
            throw new ArrayIndexOutOfBoundsException("offset=" + offset + " length="
                    + length + " limit="
                    + out.length);
        if (ndx >= buffer.length) {
            fillBlock();
            ndx = 0;
        }
        int count = 0;
        while (count < length) {
            int amount = Math.min(buffer.length - ndx, length - count);
            System.arraycopy(buffer, ndx, out, offset + count, amount);
            count += amount;
            ndx += amount;
            if (ndx >= buffer.length) {
                fillBlock();
                ndx = 0;
            }
        }
    }

    public int nextInt() {
        return next(32);
    }

    public int nextInt(int bound) {
        if (bound <= 0)
            throw new IllegalArgumentException("bound must be positive");

        int r = next(31);
        int m = bound - 1;
        if ((bound & m) == 0)  // i.e., bound is a power of 2
            r = (int) ((bound * (long) r) >> 31);
        else {
            for (int u = r;
                 u - (r = u % bound) + m < 0;
                 u = next(31))
                ;
        }
        return r;
    }

    public double nextDouble() {
        return (((long) (next(26)) << 27) + next(27)) * DOUBLE_UNIT;
    }

    public boolean isInitialised() {
        return initialised;
    }

    private byte nextByteInternal() {
        if (ndx >= buffer.length) {
            this.fillBlock();
            ndx = 0;
        }

        return buffer[ndx++];
    }

    public abstract void setup(byte[] seed);

    public abstract void fillBlock();
}
