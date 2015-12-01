package com.peleton.cheque.model.fake;

import java.util.ArrayList;
import java.util.List;

import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.StoreArticle;
import com.peleton.cheque.model.StoreInfoFindResult;
import com.peleton.cheque.model.StoreInfoSelectResult;
import com.peleton.cheque.model.User;

public class FakeShop implements Shop {

	private int id;
	private String name;

	public int getId() {
		return id;
	}

	public String getName() {

		if(name == null) {

			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			switch(getId()) {
				case 1:
					name = "Обои";
					break;
				case 2:
					name = "Двери";
					break;
				case 3:
					name = "Шторы";
					break;
			}
		}
		return name;
	}

	public FakeShop(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return ((Integer)getId()).hashCode();
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (o instanceof FakeShop) {
			return getId() == ((FakeShop)o).getId();
		}

		return false;

	}

	@Override
	public List<User> getUsers() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<User> users = new ArrayList<User>();
		switch(getId()) {
			case 1 :
				users.add(new FakeUser(1));
				users.add(new FakeUser(2));
				users.add(new FakeUser(3));
				break;
			case 2 :
				users.add(new FakeUser(1));
				users.add(new FakeUser(3));
				break;
			case 3 :
				users.add(new FakeUser(2));
				users.add(new FakeUser(3));
				break;

		};

		return users;
	}

	@Override
	public StoreInfoFindResult getStoreInfo(int articleId) {
		// TODO Auto-generated method stub
		switch (articleId) {
			case 5:
				return new StoreInfoFindResult(true,false,false, "Неверный артикул",14562);
			case 6:
				return new StoreInfoFindResult(true,false,false, "Не продается в данном магазине.",14562);
			case 8:
				return new StoreInfoFindResult(false,false,true, "Только в занзибаре.",145629);
			default:
				return new StoreInfoFindResult(false,true,true, "Артикул введен успешно",142412);
		}
	}

	@Override
	public StoreInfoSelectResult selectStoreInfo(int articleId) {
		// TODO Auto-generated method stub
		return new FakeStoreInfoSelectResult(articleId);
	}

	@Override
	public List<StoreArticle> searchByArticle(String filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void revise(int articleId, int price) {
		// TODO Auto-generated method stub

	}

}
