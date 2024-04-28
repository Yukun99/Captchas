package me.yukun.captchas.util;

import java.io.IOException;

@FunctionalInterface
public interface IOExceptionConsumer<T> {

  void accept(T t) throws IOException;
}
