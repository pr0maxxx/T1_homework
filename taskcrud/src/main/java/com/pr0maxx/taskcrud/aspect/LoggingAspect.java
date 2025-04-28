package com.pr0maxx.taskcrud.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // Before - перед выполнением методов TaskService
    @Before("@annotation(com.pr0maxx.taskcrud.aspect.Loggable)")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("[Before] Вызов метода: {}", methodName);
    }

    // AfterReturning - после успешного выполнения методов TaskService
    @AfterReturning("@annotation(com.pr0maxx.taskcrud.aspect.Loggable)")
    public void logAfterReturning(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        log.info("[AfterReturning] Метод {} успешно завершен.", methodName);
    }

    // AfterThrowing - если метод TaskService выбрасывает исключение
    @AfterThrowing(pointcut = "@annotation(com.pr0maxx.taskcrud.aspect.Loggable)", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        log.error("[AfterThrowing] Ошибка в методе {}: {}",methodName, ex.getMessage());
    }

    // Around - оборачивает выполнение метода и замеряет время
    @Around("@annotation(com.pr0maxx.taskcrud.aspect.Loggable)")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        log.info("[Around] Старт выполнения: {}", joinPoint.getSignature());

        Object result = joinPoint.proceed(); // вызываем целевой метод

        long duration = System.currentTimeMillis() - start;
        log.info("[Around] Завершено выполнение: {} за {} мс", joinPoint.getSignature(), duration);

        return result;
    }
}
