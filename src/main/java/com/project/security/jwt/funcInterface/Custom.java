package com.project.security.jwt.funcInterface;

@FunctionalInterface
public interface Custom<T> {

    void test(T t);

    static<T> Custom<T> withDefaults() {
        return (t)->{};
    }

//    static<T> Custom<T> withDefaults() {
//        return new Custom<>() {
//            @Override
//            public void test(T t) {
//            }
//        };
//    }
//
//    static<T> Custom<T> withDefaults() {
//        return new Impl<>();
//    }
}

// class Impl<T> implements Custom<T>{
//
//     @Override
//     public void test(T t) {
//     }
// }
