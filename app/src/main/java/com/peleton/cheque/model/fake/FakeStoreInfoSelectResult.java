package com.peleton.cheque.model.fake;

import java.util.ArrayList;
import java.util.List;

import com.peleton.cheque.model.StoreInfo;
import com.peleton.cheque.model.StoreInfoSelectResult;

public class FakeStoreInfoSelectResult implements StoreInfoSelectResult {

	private int articleId;

	@Override
	public int getArticleId() {
		// TODO Auto-generated method stub
		return articleId;
	}

	@Override
	public String getArticle() {
		// TODO Auto-generated method stub
		return "Фигня какая-то";
	}

	@Override
	public List<StoreInfo> getStoreInfoList() {
		// TODO Auto-generated method stub
		ArrayList<StoreInfo> list = new ArrayList<StoreInfo>();
		list.add(new FakeStoreInfo(5, "Оттенок", "124", "ТК-ЮН", 10.0f));
		list.add(new FakeStoreInfo(5, "Оттенок", "708", "ТК-МИ", 15.0f));
		list.add(new FakeStoreInfo(5, "Оттенок", "624", "ТК-КЕ", 3.0f));
		return list;
	}

	public FakeStoreInfoSelectResult(int articleId) {
		// TODO Auto-generated constructor stub
		this.articleId = articleId;
	}
}
