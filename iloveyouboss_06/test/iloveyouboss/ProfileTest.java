/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
***/
package iloveyouboss;

import static org.junit.Assert.assertTrue;

import org.junit.*;

public class ProfileTest {

// 준비-실행-단언(AAA) 중 1) 테스트 준비 단계(Arrange)
   @Test
   public void matchAnswersFalseWhenMustMatchCriteriaNotMet() {

      Profile profile = new Profile("Bull Hockey, Inc.");
      Question question = new BooleanQuestion(1, "Got bonuses?");
      
      // 질문에 대한 실제 대답 
      Answer profileAnswer = new Answer(question, Bool.FALSE);
      profile.add(profileAnswer); 
      
      // 질문에 대한 기대하는 대답  
      Answer criteriaAnswer = new Answer(question, Bool.TRUE);
      
      // 실제 대답에 대한 가중치 저장 
      Criterion criterion = new Criterion(criteriaAnswer, Weight.MustMatch);
      
      Criteria criteria = new Criteria();
      criteria.add(criterion);
   }
   
   @Test
   public void matchAnswersTrueForAnyDontCareCriteria() {
      Profile profile = new Profile("Bull Hockey, Inc.");
      Question question = new BooleanQuestion(1, "Got milk?");
      Answer profileAnswer = new Answer(question, Bool.FALSE);
      profile.add(profileAnswer);      
      Answer criteriaAnswer = new Answer(question, Bool.TRUE);
      
      // 실제 대답에 대한 가중치 저장
      Criterion criterion = new Criterion(criteriaAnswer, Weight.DontCare);
      Criteria criteria = new Criteria();
      criteria.add(criterion);

      // 1번 테스트에서 추가된 2줄 
      boolean matches = profile.matches(criteria);
      assertTrue(matches);
   }
}
