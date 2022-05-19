/***
 * Excerpted from "Pragmatic Unit Testing in Java with JUnit",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/utj2 for more book information.
***/
package scratch;

import static org.hamcrest.CoreMatchers.*;

// import static org.junit.Assert.assertThat; <- depressed로 아래로 대체 
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.*;
import java.util.*;
import org.junit.*;
import static scratch.PointMatcher.isNear;
// ...
import org.junit.rules.*;

// JUnit 단언(1. 전통-assertTrue 2. 햄크레스트-assertThat)
public class AssertTest {

   class InsufficientFundsException extends RuntimeException {
      public InsufficientFundsException(String message) {
         super(message);
      }

      private static final long serialVersionUID = 1L;
   }

   class Account {
      int balance;
      String name;

      Account(String name) {
         this.name = name;
      }

      void deposit(int dollars) {
         balance += dollars;
      }

      void withdraw(int dollars) {
         if (balance < dollars) {
            throw new InsufficientFundsException("balance only " + balance);
         }
         balance -= dollars;
      }

      public String getName() {
         return name;
      }

      public int getBalance() {
         return balance;
      }

      public boolean hasPositiveBalance() {
         return balance > 0;
      }
   }

   class Customer {
      List<Account> accounts = new ArrayList<>();

      void add(Account account) {
         accounts.add(account);
      }

      Iterator<Account> getAccounts() {
         return accounts.iterator();
      }
   }

   private Account account;

   // @Before: 초기화 담당 
   @Before
   public void createAccount() {
      account = new Account("an account name");
   }


   // JUnit 단언
   // 1) assertTrue: 전통적 단언. 가장 일반적 단언 
   @Test
   public void hasPositiveBalance() {
      account.deposit(50);
      assertTrue(account.hasPositiveBalance());
   }

   
   @Test
   public void depositIncreasesBalance() {
      int initialBalance = account.getBalance();
      account.deposit(100);
      // 1) assertTrue: 입금후엔 잔고가 0보다 크다고 단언 
      assertTrue(account.getBalance() > initialBalance);
      
      // 2) assertThat: 햄크레스트 단언. 명확한 값을 비교. 자주 사용.
      // 햄크레스트 단언 assertThat(검증하고자 하는 값, matcher)
      // matcher: 실제 값 == 표현식 값 인지 비교. 가독성 향상 목적.
      // assertThat는 실패시 기댓값, 실제값을 오류 메세지로 출력해줘 유용. cf)assertTrue는 실패시 스택트레이스 출력.

      // assertThat의 사용 예 1 
      // equalTo 매처: 자바 인스턴스, 기본형 값 모두 넣을 수 있다. (자바기본형은 오토박싱되어 어떤 타입과도 비교가능)
      // 명확한 값을 비교
      assertThat(account.getBalance(), equalTo(100));
   }

   // assertThat의 사용 예 2
   // (비권장)assertThat를 boolean표현식에 assertTrue대신 사용. 상세 실패 내용 보기 위해. 
   @Test
   public void depositIncreasesBalance_hamcrestAssertTrue() {
      account.deposit(50);
      assertThat(account.getBalance() > 0, is(true));
   }
   
   // assertThat의 사용 예 4
   // 자바 배열, 컬렉션 객체를 비교할 때 equalTo 메서드 사용 
   // 성공
   @Test
   public void comparesArraysPassing() {
      assertThat(new String[] {"a", "b"}, equalTo(new String[] {"a", "b"}));
   }
   
   // 성공
   @Test
   public void comparesCollectionsPassing() {
	   assertThat(Arrays.asList(new String[] {"a"}), 
			   equalTo(Arrays.asList(new String[] {"a"})));
   }
   
   // 실패
   @Ignore
   @ExpectToFail
   @Test
   public void comparesArraysFailing() {
	   assertThat(new String[] {"a", "b", "c"}, equalTo(new String[] {"a", "b"}));
   }
   
   // 실패
   @Ignore
   @ExpectToFail
   @Test
   public void comparesCollectionsFailing() {
      assertThat(Arrays.asList(new String[] {"a"}), 
            equalTo(Arrays.asList(new String[] {"a", "ab"})));
   }

   // (비권장)단언 설명: 선택적으로 assertThat의 첫 인자로 단언 설명을 위한 message 추가가능. 하지만 테스트 코드 그 자체로 이해되게 작성하는 것이 낫다.
   @Ignore
   @Test
   public void testWithWorthlessAssertionComment() {
      account.deposit(50);
      assertThat("account balance is 100", account.getBalance(), equalTo(50));
   }

   @Test
   @ExpectToFail
   @Ignore
   public void assertFailure() {
      assertTrue(account.getName().startsWith("xyz"));
   }

   // assertThat의 사용 예 3
   // CoreMatchers 클래스의 startsWith 매처를 사용 
   @Test
   @ExpectToFail
   @Ignore
   public void matchesFailure() {
      assertThat(account.getName(), startsWith("xyz"));
   }

   // 햄크레스트의 중요 매처들 확인하기 
   @Test
   public void variousMatcherTests() {
      Account account = new Account("my big fat acct");
      
      // 1) (비권장)is 장식자: 가독성을 위해 선택적으로 사용. is는 넘겨받은 매처를 반환할 뿐 아무것도 x.
      assertThat(account.getName(), is(equalTo("my big fat acct")));

      assertThat(account.getName(), allOf(startsWith("my"), endsWith("acct")));

      assertThat(account.getName(), anyOf(startsWith("my"), endsWith("loot")));

      // 2) not 매처: 어떤 것을 부정하는 단언 생성
      assertThat(account.getName(), not(equalTo("plunderings")));

      // not 매처를 사용해 null 값이나 null 아닌 값을 검사하는 경우 
      assertThat(account.getName(), is(not(nullValue())));
      assertThat(account.getName(), is(notNullValue()));

      assertThat(account.getName(), isA(String.class));
      
      // not helpful
      // null 인지 자주 확인할 필요는 x.
      assertThat(account.getName(), is(notNullValue())); 
      // null 이면 equalTo는 실행x.
      assertThat(account.getName(), equalTo("my big fat acct"));
   }

   @Test
   public void sameInstance() {
      Account a = new Account("a");
      Account aPrime = new Account("a");
      // TODO why needs to be fully qualified??
      assertThat(a, not(org.hamcrest.CoreMatchers.sameInstance(aPrime)));
   }

   @Test
   public void moreMatcherTests() {
      Account account = new Account(null);
      assertThat(account.getName(), is(nullValue()));
   }

   @Test
   @SuppressWarnings("unchecked")
   public void items() {
      List<String> names = new ArrayList<>();
      names.add("Moe");
      names.add("Larry");
      names.add("Curly");

      assertThat(names, hasItem("Curly"));

      assertThat(names, hasItems("Curly", "Moe"));

      assertThat(names, hasItem(endsWith("y")));

      assertThat(names, hasItems(endsWith("y"), startsWith("C"))); //warning!

      assertThat(names, not(everyItem(endsWith("y"))));
   }

   @Test
   @ExpectToFail @Ignore
   public void location() {
      Point point = new Point(4, 5);
      
      // closeTo: 부동소수점 2개를 비교. 별도 원본 햄크레스트 매처 다운받아 연결후 사용가능.
      // 컴퓨터는 부동소수점을 근사치로 계산. 따라서 equalTo대신 isCloseTo 메서드 사용 
      // assertThat(point, isNear(3.9, 5.2));
   }
   
   @Test
   @ExpectToFail @Ignore
   public void classicAssertions() {
      Account account = new Account("acct namex");
      assertEquals("acct name", account.getName());
   }

   
   // 예외를 기대하는 3가지방법: 1) @Test 사용 2) try/catch, fail 3) ExpectedException 규칙
   
   // 예외를 기대하는 3가지방법
   // 1) @Test 사용: 기대한 예외를 지정할 수 있는 인자 제공 
   @Test(expected=InsufficientFundsException.class)
   public void throwsWhenWithdrawingTooMuch() {
      account.withdraw(100);
   }
   
   // 예외를 기대하는 3가지방법
   // 2) (옛방식) try/catch, fail
   // 예외가 발생하지 않으면 fail()메서드 호출하여 강제로 실패 
   @Test
   public void throwsWhenWithdrawingTooMuchTry() {
      try {
         account.withdraw(100);
         fail();
      }
      
      // 예외 발생하면 테스트 통과 
      catch (InsufficientFundsException expected) {
         assertThat(expected.getMessage(), equalTo("balance only 0"));
      }
   }
   
   // 예외 무시 
   // 검증된 예외를 처리할 땐 throws로 예외 무시 
   @Test
   public void readsFromTestFile() throws IOException {
      String filename = "test.txt";
      BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
      writer.write("test data");
      writer.close();
      // ...
   }
   
   @After
   public void deleteForReadsFromTestFile() {
      new File("test.txt").delete();
   }
   
   @Test
   @Ignore("don't forget me!")
   public void somethingWeCannotHandleRightNow() {
      // ...
   }
   
   // 예외를 기대하는 3가지방법
   // 3) (새로운 방식)ExpectedException 규칙
   // 사용하려면 테스트 클래스에 ExpectedException를 public으로 선언하고 @Rule 추가해야.
   @Rule
   public ExpectedException thrown = ExpectedException.none();  
   
   // 테스트 셋업 단계에서 나머지 테스트를 실행시 발생할 수 있는 일을 규칙에 알림 
   @Test
   public void exceptionRule() {
	   
	  // thrown규칙 인스턴스는 InsufficientFundsException 예외가 발생함을 알림 
      thrown.expect(InsufficientFundsException.class); 
      
      // 예외 객체에 적절항 메세지가 포함되어 있는지 검사 
      thrown.expectMessage("balance only 0");  
      
      // 예외가 발생하길 기대하는 테스트 실행 
      account.withdraw(100);  
   }
   
   // 부동소수점 비교 
   @Test
   public void doubles() {
      assertEquals(9.7, 10.0 - 0.3, 0.005);
   }
}
