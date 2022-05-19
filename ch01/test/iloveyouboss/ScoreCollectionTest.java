package iloveyouboss;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class ScoreCollectionTest {

	@Test
	public void answerArithmeticMeanOfTwoNumbers() {
		
		// 준비 
		ScoreCollection collection = new ScoreCollection();
		collection.add(()->5);
		collection.add(()->7);

		// 실행
		int actualResult = collection.arithmeticMean();
		
		// 단언문은 테스트의 가장 아래에 넣기 
		// 단언 (Deprecated된 assertThat메서드 대체)
		assertSame(actualResult, 6); // assertSame(Object expected, Object actual)
 
		}	
}
