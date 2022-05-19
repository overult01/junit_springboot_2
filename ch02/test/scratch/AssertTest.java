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

   @Test
   public void variousMatcherTests() {
      Account account = new Account("my big fat acct");
      
      // is 장식자: 가독성을 위해 선택적으로 사용. is는 넘겨받은 매처를 반환할 뿐 아무것도 x.
      assertThat(account.getName(), is(equalTo("my big fat acct")));

      assertThat(account.getName(), allOf(startsWith("my"), endsWith("acct")));

      assertThat(account.getName(), anyOf(startsWith("my"), endsWith("loot")));

      assertThat(account.getName(), not(equalTo("plunderings")));

      assertThat(account.getName(), is(not(nullValue())));
      assertThat(account.getName(), is(notNullValue()));

      assertThat(account.getName(), isA(String.class));

      assertThat(account.getName(), is(notNullValue())); // not helpful
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
      
      // WTF why do JUnit matches not include closeTo
//      assertThat(point.x, closeTo(3.6, 0.2));
//      assertThat(point.y, closeTo(5.1, 0.2));
      
      assertThat(point, isNear(3.6, 5.1));
   }
   
   @Test
   @ExpectToFail @Ignore
   public void classicAssertions() {
      Account account = new Account("acct namex");
      assertEquals("acct name", account.getName());
   }
   
   @Test(expected=InsufficientFundsException.class)
   public void throwsWhenWithdrawingTooMuch() {
      account.withdraw(100);
   }
   
   @Test
   public void throwsWhenWithdrawingTooMuchTry() {
      try {
         account.withdraw(100);
         fail();
      }
      catch (InsufficientFundsException expected) {
         assertThat(expected.getMessage(), equalTo("balance only 0"));
      }
   }
   
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

   @Rule
   public ExpectedException thrown = ExpectedException.none();  
   
   @Test
   public void exceptionRule() {
      thrown.expect(InsufficientFundsException.class); 
      thrown.expectMessage("balance only 0");  
      
      account.withdraw(100);  
   }
   
   @Test
   public void doubles() {
      assertEquals(9.7, 10.0 - 0.3, 0.005);
   }
}
