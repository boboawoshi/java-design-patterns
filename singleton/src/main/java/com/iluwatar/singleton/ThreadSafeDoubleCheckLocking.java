/*
 * The MIT License
 * Copyright © 2014-2021 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.iluwatar.singleton;

import lombok.var;

/**
 * <p>Double check locking.</p>
 *
 * <p>http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html</p>
 *
 * <p>Broken under Java 1.4.</p>
 *
 * @author mortezaadi@gmail.com
 */
public final class ThreadSafeDoubleCheckLocking {

  private static volatile ThreadSafeDoubleCheckLocking instance;

  private static boolean flag = true;

  /**
   * private constructor to prevent client from instantiating.
   */
  private ThreadSafeDoubleCheckLocking() {
    // to prevent instantiating by Reflection call
    if (flag) {
      flag = false;
    } else {
      throw new IllegalStateException("Already initialized.");
    }
  }

  /**
   * Public accessor.
   *
   * @return an instance of the class.
   */
  public static ThreadSafeDoubleCheckLocking getInstance() {
    // local variable increases performance by 25 percent
    // Joshua Bloch "Effective Java, Second Edition", p. 283-284

    var result = instance;
    // 检查单例对象是否被初始化.
    // 如果初始化了，我们可以返回这个实例
    if (result == null) {
      // 如果没有初始化，但我们不能确定 因为一些其他线程在其间可能初始化了
      // 所以为了确保，我们需要去锁定一个对象来获得互斥
      synchronized (ThreadSafeDoubleCheckLocking.class) {
        //再次将实例赋值给局部变量，以检查当当前线程被阻塞进入锁定区域时，它是否被其他线程初始化。
        // 如果它已经初始化，那么我们可以返回先前创建的实例，就像先前的null检查一样。
        result = instance;
        if (result == null) {
          // The instance is still not initialized so we can safely
          // (no other thread can enter this zone)
          // create an instance and make it our singleton instance.
          instance = result = new ThreadSafeDoubleCheckLocking();
        }
      }
    }
    return result;
  }
}
