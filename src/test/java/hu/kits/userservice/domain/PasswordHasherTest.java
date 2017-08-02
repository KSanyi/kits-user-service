package hu.kits.userservice.domain;

import org.junit.Assert;
import org.junit.Test;

public class PasswordHasherTest {

	@Test
	public void correctPassword() {
		String passwordHash = PasswordHasher.createNewPasswordHash("Alma1234");
		Assert.assertTrue(PasswordHasher.checkPassword(passwordHash, "Alma1234"));
	}
	
	@Test
	public void incorrectPassword() {
		String passwordHash = PasswordHasher.createNewPasswordHash("Alma1234");
		Assert.assertFalse(PasswordHasher.checkPassword(passwordHash, "Korte1234"));
	}
	
}
