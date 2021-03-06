package protocol.impl.sigma;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import protocol.impl.sigma.ResEncrypt;
import protocol.impl.sigma.Fabric;
import crypt.factories.ElGamalAsymKeyFactory;
import model.entity.ElGamalKey;
import protocol.impl.sigma.ResponsesCCE;
import util.TestInputGenerator;

/**
 * ResponsesCCE unit test
 * @author denis.arrivault[@]univ-amu.fr
 */
public class ResponsesCCETest {

	private byte[] msg;
	private ElGamalKey key;
	private ElGamalKey badKey;
	private ResponsesCCE response;
	ResEncrypt res;
	ResEncrypt badRes;
	
	@Before
	public void instantiate(){
		key = ElGamalAsymKeyFactory.create(false);
		badKey = ElGamalAsymKeyFactory.create(false);
		msg = TestInputGenerator.getRandomBytes(100);
		Sender sender = new Sender(key);
		res = sender.Encryption(msg, key);
		badRes = sender.Encryption(msg, badKey);
		response = (new Fabric()).SendResponseCCEFabric(res, key);
	}
	

	@Test
	public void verifyTest() {
		assertTrue(response.Verifies(key, res));
		assertFalse(response.Verifies(badKey, res));
		assertFalse(response.Verifies(key, badRes));

	}

	
}

