package com.github.youyinnn.youwebutils.exceptions;

/**
 * 构建Map的时候出现的异常,比如奇数个参数,比如键值位不是String类型.
 *
 * @author youyinnn
 */
public class YouMapException extends Exception {

    public YouMapException(String msg) {
        super(msg);
    }

}
