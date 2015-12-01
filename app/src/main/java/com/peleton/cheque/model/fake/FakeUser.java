package com.peleton.cheque.model.fake;

import com.peleton.cheque.model.CheckResult;
import com.peleton.cheque.model.Shop;
import com.peleton.cheque.model.User;

public class FakeUser implements User {

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
					name = "Казанцев";
					break;
				case 2:
					name = "Петрова";
					break;
				case 3:
					name = "Сидорова";
					break;
			}
		}
		return name;
	}

	public FakeUser(int id) {
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
		if (o instanceof FakeUser) {
			return getId() == ((FakeShop)o).getId();
		}

		return false;

	}

	@Override
	public CheckResult checkPassword(Shop shop, String password) {
		// TODO Auto-generated method stub

		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean hasSuccess =
				getId() == 1 && password.equals("1122") ||
						getId() == 2 && password.equals("1111") ||
						getId() == 3 && password.equals("2222");
		if(hasSuccess)
			return new CheckResult(false, "Пароль введен успешно.");
		else
			return new CheckResult(true, "Неправильный пароль.");
	}

}
