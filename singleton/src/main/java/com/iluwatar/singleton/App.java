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

import lombok.extern.slf4j.Slf4j;
import lombok.var;

/**
 * <p>单例模式确保每个Java类加载器实例只能有一个现有实例，并提供对它的全局访问  .</p>
 *
 * <p>这种模式的风险之一是，在分布式环境中设置单例对象所导致的错误调试起来可能很棘手，
 * 因为如果使用单个类加载器进行调试，它就可以正常工作。
 * 此外，这些问题可能会在实现单例对象后一段时间内突然出现，
 * 因为它们可能一开始是同步的，但随着时间的推移会变成异步的，所以你可能不清楚为什么会看到某些行为的变化.</p>
 *
 * <p>有很多方法可以实现单例. The first one is the eagerly initialized
 * instance in {@link IvoryTower}. 预加载意味着线程是安全的. If you can afford giving up control of the instantiation moment, then this implementation
 * will suit you fine.</p>
 *
 * <p>The other option to implement eagerly initialized Singleton is enum based Singleton. The
 * example is found in {@link EnumIvoryTower}. At first glance the code looks short and simple.
 * However, you should be aware of the downsides including committing to implementation strategy,
 * extending the enum class, serializability and restrictions to coding. These are extensively
 * discussed in Stack Overflow: http://programmers.stackexchange.com/questions/179386/what-are-the-downsides-of-implementing
 * -a-singleton-with-javas-enum</p>
 *
 * <p>{@link ThreadSafeLazyLoadedIvoryTower} is a Singleton implementation that is initialized on
 * demand. The downside is that it is very slow to access since the whole access method is
 * synchronized.</p>
 *
 * <p>Another Singleton implementation that is initialized on demand is found in
 * {@link ThreadSafeDoubleCheckLocking}. It is somewhat faster than {@link
 * ThreadSafeLazyLoadedIvoryTower} since it doesn't synchronize the whole access method but only the
 * method internals on specific conditions.</p>
 *
 * <p>Yet another way to implement thread safe lazily initialized Singleton can be found in
 * {@link InitializingOnDemandHolderIdiom}. However, this implementation requires at least Java 8
 * API level to work.</p>
 */
@Slf4j
public class App {

  /**
   * Program entry point.
   *
   * @param args command line args
   */
  public static void main(String[] args) {

    // 饿汉模式
    var ivoryTower1 = IvoryTower.getInstance();
    var ivoryTower2 = IvoryTower.getInstance();
    LOGGER.info("ivoryTower1={}", ivoryTower1);
    LOGGER.info("ivoryTower2={}", ivoryTower2);

    // 懒汉模式
    var threadSafeIvoryTower1 = ThreadSafeLazyLoadedIvoryTower.getInstance();
    var threadSafeIvoryTower2 = ThreadSafeLazyLoadedIvoryTower.getInstance();
    LOGGER.info("threadSafeIvoryTower1={}", threadSafeIvoryTower1);
    LOGGER.info("threadSafeIvoryTower2={}", threadSafeIvoryTower2);

    // 枚举单例
    var enumIvoryTower1 = EnumIvoryTower.INSTANCE;
    var enumIvoryTower2 = EnumIvoryTower.INSTANCE;
    LOGGER.info("enumIvoryTower1={}", enumIvoryTower1);
    LOGGER.info("enumIvoryTower2={}", enumIvoryTower2);

    // 双重检查锁定
    var dcl1 = ThreadSafeDoubleCheckLocking.getInstance();
    LOGGER.info(dcl1.toString());
    var dcl2 = ThreadSafeDoubleCheckLocking.getInstance();
    LOGGER.info(dcl2.toString());

    // initialize on demand holder idiom
    var demandHolderIdiom = InitializingOnDemandHolderIdiom.getInstance();
    LOGGER.info(demandHolderIdiom.toString());
    var demandHolderIdiom2 = InitializingOnDemandHolderIdiom.getInstance();
    LOGGER.info(demandHolderIdiom2.toString());
  }
}
