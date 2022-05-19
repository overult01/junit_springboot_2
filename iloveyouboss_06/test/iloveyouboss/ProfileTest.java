/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
***/
package iloveyouboss;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.*;

public class ProfileTest {
	
	private Profile profile;
	private BooleanQuestion question;
	private Criteria criteria;
	
	// @Before: 각 JUnit 테스트 진행시 필요한 공통적인 초기화 담당 (가독성 향상)
	@Before
	public void create() { // profile, question, criteria 변수를 초기화 
		profile = new Profile("Bull Hockey, Inc.");
	    question = new BooleanQuestion(1, "Got bonuses?");
	    criteria = new Criteria();
	}	
	

   @Test
   public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {

	   // 준비-실행-단언(AAA) 중 1) 테스트 준비 단계(Arrange)
	   
	   // @Before가 대신해주는 덕분에 초기화 부분 생략 
	   
      // 질문에 대한 실제 대답 
      Answer profileAnswer = new Answer(question, Bool.FALSE);
      profile.add(profileAnswer); 
      
      // 질문에 대한 기대하는 대답  
      Answer criteriaAnswer = new Answer(question, Bool.TRUE);
      
      // 실제 대답에 대한 가중치 저장 
      Criterion criterion = new Criterion(criteriaAnswer, Weight.MustMatch);
      
      criteria.add(criterion);
      
      // 준비-실행-단언(AAA) 중 2) 테스트 실행 단계(Action)
      boolean matches = profile.matches(criteria);
      
      // 준비-실행-단언(AAA) 중 3) 테스트 단언 단계(Assert) 
      // 실패한 단언문 아래의 코드는 수행x. 따라서 단언문은 ㅌ스트의 가장 아래에 넣는다.
      assertFalse(matches);
   }
   
   // 또 다른 테스트이기 때문에 JUnit은 ProfileTest 인스턴스를 새롭게 생성 
   @Test
   public void matchAnswersTrueForAnyDontCareCriteria() {

	   // @Before가 대신해주는 덕분에 초기화 부분 생략 

      Answer profileAnswer = new Answer(question, Bool.FALSE);
      profile.add(profileAnswer);      
      Answer criteriaAnswer = new Answer(question, Bool.TRUE);
      
      // 실제 대답에 대한 가중치 저장
      Criterion criterion = new Criterion(criteriaAnswer, Weight.DontCare);
      criteria.add(criterion);

      // 1번 테스트에서 추가된 2줄 
      boolean matches = profile.matches(criteria);
      assertTrue(matches);
   }
}
