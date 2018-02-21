package ua.nure.skibnev.SummaryTask4;


import org.junit.Test;

import ua.nure.skibnev.SummaryTask4.db.entity.FullUser;

public class EntityTest {
@Test
public void entity(){
	FullUser fus=new FullUser();
	fus.setLogin("asd");
	fus.setAddress("asd");
	fus.setFirstName("asd");
	fus.setLastName("asd");
	fus.setEmail("asd@mail.ru");
	fus.setPassword("asd");
	fus.setRates("asd");
	fus.setRole("asd");
	fus.setCount(12);
	fus.setTelephone("0995342178");
	fus.setStatus("blocked");
}
}
