package cn.sagacloud.utils;
/*
 * Author: Jxing
 * Create Time: 2019/3/12
 */

import cn.sagacloud.pojo.Command;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class CommonUtil {
    public static Set<Command> createSet(Command... elements) {
        Set<Command> set = new HashSet<>();
        for (Command e : elements) {
            set.add(e);
        }
        return set;
    }

    public static byte[] getBytes(char[] chars) {
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = CharBuffer.allocate(chars.length);
        cb.put(chars);
        cb.flip();
        ByteBuffer bb = cs.encode(cb);
        return bb.array();
    }
    public static long getTime() {
        return System.currentTimeMillis()/1000;
    }

}
