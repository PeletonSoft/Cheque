package com.peleton.cheque.model.fake;

import java.util.ArrayList;
import java.util.Iterator;
import com.peleton.cheque.model.ChequePosition;
import com.peleton.cheque.model.ChequePositions;

public class FakeChequePositions implements ChequePositions {

	private ArrayList<ChequePosition> innerChequePositions;


	@Override
	public void delete(ChequePosition chequePosition) {

		innerChequePositions.remove(chequePosition);
	}


	@Override
	public Iterator<ChequePosition> iterator() {
		// TODO Auto-generated method stub
		return innerChequePositions.iterator();
	}
	
	public FakeChequePositions() {

		innerChequePositions = new ArrayList<ChequePosition>();

	}


	@Override
	public void add(int articleId) {
		
		innerChequePositions.add(new FakeChequePosition(articleId));
	}


	@Override
	public ChequePosition[] toArray() {

		
		ArrayList<ChequePosition> list = new ArrayList<ChequePosition>();
		for (ChequePosition chequePosition : this) {
			
			list.add(chequePosition);
		}
		
		ChequePosition[] array = new ChequePosition[list.size()];
		
		int i=0;
		for(ChequePosition chequePosition : this){
		  array[i++] = chequePosition;
		}
		
		//ChequePosition[] array = new ChequePosition[0];
		return array; 
	}

}
