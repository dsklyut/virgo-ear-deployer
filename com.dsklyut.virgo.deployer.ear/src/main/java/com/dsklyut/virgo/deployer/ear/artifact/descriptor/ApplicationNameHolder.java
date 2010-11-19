package com.dsklyut.virgo.deployer.ear.artifact.descriptor;

/**
 * User: dsklyut
 * Date: 11/18/10
 * Time: 4:31 PM
 */
class ApplicationNameHolder {

    private final static ThreadLocal<String> store = new ThreadLocal<String>();


    public static void set(String name) {
        store.set(name);
    }

    public static String get() {
        return store.get();
    }

    public static void reset() {
        store.remove();
    }
}
