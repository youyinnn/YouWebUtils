package com.github.youyinnn.youwebutils.fourth;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * 这个类用于执行SQL脚本,搬运自:
    <dependency>
    <groupId>com.ibatis</groupId>
    <artifactId>ibatis2-common</artifactId>
    <version>2.1.7.597</version>
    </dependency>
 */
public class NestedRuntimeException extends RuntimeException {
    private static final String CAUSED_BY = "\nCaused by: ";
    private Throwable cause = null;

    public NestedRuntimeException() {
    }

    public NestedRuntimeException(String msg) {
        super(msg);
    }

    public NestedRuntimeException(Throwable cause) {
        this.cause = cause;
    }

    public NestedRuntimeException(String msg, Throwable cause) {
        super(msg);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return this.cause;
    }

    @Override
    public String toString() {
        return this.cause == null ? super.toString() : super.toString() + "\nCaused by: " + this.cause.toString();
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        if (this.cause != null) {
            System.err.println("\nCaused by: ");
            this.cause.printStackTrace();
        }

    }

    @Override
    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (this.cause != null) {
            ps.println("\nCaused by: ");
            this.cause.printStackTrace(ps);
        }

    }

    @Override
    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (this.cause != null) {
            pw.println("\nCaused by: ");
            this.cause.printStackTrace(pw);
        }

    }
}