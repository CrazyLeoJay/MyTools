/**
 * 这是对数据库封装的基本包，所有的需要的基础类的父类都在这里.
 * <p>
 * 他们是：
 * <br>
 * <br>abstract class DatabaseFactory
 * <br>abstract class DatabaseObject
 * <br>abstract class MyOperation
 * <br>interface MyConnection
 * <br>(选)interface MyConfig
 * <br>
 * <br>三个基础抽象类和两个接口，在继承时，只有继承于抽象类 <B>DatabaseObject</B> 的类是public的。
 * <br>这里需要注意的是，DatabaseObject类是基础类，
 * 有一些数据库的操作方式，都是代理的类MyOperation，所有数据库操作都在MyOperation里完成
 * 其中，接口MyConfig根据实际使用可以删减，一般情况下MyConfig只配置数据库链接信息。
 * </p>
 * <p>
 * time:16/12/3__17:46
 *
 * @author:leojay
 * @see leojay.tools.database2
 */
package leojay.tools.java.database2.base;