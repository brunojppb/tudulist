package tudulist.activities;

import br.tudulist.R;

public enum Option {
	NotImportant(R.id.rd_not_important),
	Important(R.id.rd_important),
	VeryImportant(R.id.rd_very_important);
	
	int idInLayout;
	
	private Option(int idLayout){
		this.idInLayout = idLayout;
	}
	
	public int getIdInLayout(){
		return this.idInLayout;
	}
}
