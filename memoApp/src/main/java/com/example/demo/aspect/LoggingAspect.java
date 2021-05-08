package com.example.demo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
	
	private final String pointCut = "execution(* com.example.demo..*(..))";
	
	@Before(pointCut)
	public void startLog(JoinPoint jp) {
		
		log.info("処理を開始しました:{}" , jp.getSignature());
		
	}
	
	@AfterReturning(pointcut = pointCut , returning = "result")
	public void afterReturning(JoinPoint jp , Object result) {
		
		log.info("正常終了しました:{}" , jp.getSignature());
		
	}
	
	@AfterThrowing(pointcut = pointCut , throwing = "e")
	public void afterThrowing(JoinPoint jp , Throwable e) {
		
		log.info("例外終了しました:{} 詳細:{}" , jp.getSignature() , e.getStackTrace());
		
	}
	
}
