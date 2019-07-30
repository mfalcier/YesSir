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

        afterExecution(joinPoint, lengthMillis, result)

        return result
    }

    private fun beforeExecution(joinPoint: JoinPoint) {

        val codeSignature = joinPoint.signature as CodeSignature
        val cls = codeSignature.declaringType.name
        val methodName = codeSignature.name
        val parameterNames = codeSignature.parameterNames
        val parameterValues = joinPoint.args

        val parameters = mutableMapOf<String, Any?>()
        parameterNames.forEachIndexed { index, name ->
            parameters[name] = parameterValues[index]
        }

        val log = LoggerFactory.getLogger(cls)
        if (parameters.isEmpty()) {
            log.info("[$cls.$methodName] has started its execution")
        } else {
            log.info("[$cls.$methodName] has started its execution with parameters: $parameters")
        }
    }

    private fun afterExecution(joinPoint: JoinPoint, lengthMillis: Long, result: Any?) {

        val signature = joinPoint.signature
        val cls = signature.declaringType.name
        val methodName = signature.name
        val log = LoggerFactory.getLogger(cls)

        if (result == null) {
            log.info("[$cls.$methodName] has ended its execution after ${lengthMillis}ms")
        } else {
            log.info("[$cls.$methodName] has ended its execution after ${lengthMillis}ms with result: [$result]")
        }
    }
}