package com.peleton.cheque.model.fake;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.peleton.cheque.model.Cheque;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.ShopFactory;
import com.peleton.cheque.model.User;

public class FakeShopFactory implements ShopFactory {

	public List<Shop> getShops() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Shop[] shopArray = new Shop[] {
				createShop(1), 
				createShop(2), 
				createShop(3)
			};
		
		return new ArrayList<Shop>(Arrays.asList(shopArray)); 
		
	}
	
	public Shop createShop(int id) {
		
		return new FakeShop(id);
	}
	
	public User createUser(int id) {
		
		return new FakeUser(id);
	}
	
	
	public Cheque createCheque(Shop shop, int number) {
		
		return new FakeCheque(shop, number);
	}

	@Override
	public Cheque getLastCheque(Shop shop, User user) {
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return createCheque(shop, 5);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}
