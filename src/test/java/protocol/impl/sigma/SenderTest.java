package protocol.impl.sigma;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.math.BigInteger;
import crypt.factories.ElGamalAsymKeyFactory;
import model.entity.ElGamalKey;
import protocol.impl.sigma.ResponsesCCE;
import protocol.impl.sigma.ResponsesSchnorr;
import protocol.impl.sigma.Sender;
import util.TestInputGenerator;

/**
 * Sender unit test
 * @author denis.arrivault[@]univ-amu.fr
 */
public class SenderTest {
	Sender sender;
	ElGamalKey senderKey;
	ElGamalKey recieverKey;
	byte[] message;
	
	@Before
	public void instantiate(){
		senderKey = ElGamalAsymKeyFactory.create(false);
		recieverKey = ElGamalAsymKeyFactory.create(false);
		sender = new Sender(senderKey);
		message = TestInputGenerator.getRandomBytes(100);
	}
	
	@Test
	public void schnorrResponseTest(){
		ResponsesSchnorr response = sender.SendResponseSchnorr(message);
		assertTrue(response.Verifies(senderKey, null));
		BigInteger challenge = sender.SendChallenge(sender.SendMasksSchnorr(), message);
		ResponsesSchnorr response2 = sender.SendResponseSchnorr(message, challenge);
		assertTrue(response2.Verifies(senderKey, null));
	}
	
	@Test
	public void CCEResponseTest(){
		ResEncrypt encryptMessage = sender.Encryption(message, recieverKey);		
		ResponsesCCE response = sender.SendResponseCCE(message, recieverKey);
		assertTrue(response.Verifies(recieverKey, encryptMessage));
		BigInteger challenge = sender.SendChallenge(response.getMasks(), message);
		ResponsesCCE response2 = sender.SendResponseCCE(message, recieverKey, challenge);
		assertTrue(response2.Verifies(recieverKey, encryptMessage));
	}
	
	@Test
	public void getKeyTest(){
		assertTrue(sender.getKeys().equals(senderKey));
	}
}

