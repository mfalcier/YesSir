package com.mfalcier.yessir

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.CodeSignature
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit


@Aspect
class YesSir {

    @Pointcut("within(@com.mfalcier.yessir.LogMe *)")
    fun withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    fun methodInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.mfalcier.yessir.LogMe * *(..)) || methodInsideAnnotatedType()")
    fun method() {
    }

    @Around("method()")
    fun logMe(joinPoint: ProceedingJoinPoint): Any? {
        beforeExecution(joinPoint)

        val startNanos = System.nanoTime()
        val result = joinPoint.proceed()
        val stopNanos = System.nanoTime()
        val lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos)

        afterExecution(joinPoint, lengthMillis)

        return result
    }

    private fun beforeExecution(joinPoint: JoinPoint) {

        val codeSignature = joinPoint.signature as CodeSignature
        val cls = codeSignature.declaringType.name
        val methodName = codeSignature.name
        val log = LoggerFactory.getLogger(cls)

        log.info("$cls.$methodName has started its execution")
    }

    private fun afterExecution(joinPoint: JoinPoint, lengthMillis: Long) {

        val signature = joinPoint.signature
        val cls = signature.declaringType.name
        val methodName = signature.name
        val log = LoggerFactory.getLogger(cls)

        log.info("$cls.$methodName has ended its execution after ${lengthMillis}ms")
    }
}